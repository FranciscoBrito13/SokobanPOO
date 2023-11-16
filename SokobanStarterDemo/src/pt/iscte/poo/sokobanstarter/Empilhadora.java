package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement implements Movable {

    public static Empilhadora INSTANCE; //Instancia da empilhadora       depois temos de por private
    private String imageName = "Empilhadora_U";
    private Point2D initialPosition;
    private int moves;
    private int maxBattery = 100;
    private int curBattery;
    

    private Empilhadora(Point2D position) {
        super(position);
        initialPosition = position;
    }

    public static Empilhadora getInstance(Point2D initialPosition) {
        if (INSTANCE == null)
            INSTANCE = new Empilhadora(initialPosition);
        return INSTANCE;
    }

    public void changeDirection(int lastKeyPressed) {
        switch (lastKeyPressed) {
            case KeyEvent.VK_UP:
                imageName = "Empilhadora_U";
                break;
            case KeyEvent.VK_DOWN:
                imageName = "Empilhadora_D";
                break;
            case KeyEvent.VK_LEFT:
                imageName = "Empilhadora_L";
                break;
            case KeyEvent.VK_RIGHT:
                imageName = "Empilhadora_R";
                break;
        }
    }

//    public void move(Direction dir, HashMap<Point2D, GameElement> tileMap) {
//        //Point2D newPosition = getNewPointFromKey(keyCode);
//    	Point2D newPosition = getPosition().plus(dir.asVector());
//        
//        if (tileMap.get(newPosition) instanceof Caixote) {
//            return;
//        }
//        setPosition(newPosition);
//        decBateria();
//        moves++;
//    }
//    


    public boolean MovingToWall(int key, HashMap<Point2D, GameElement> tileMap) {
        GameElement elementAtNewPoint = tileMap.get(getNewPointFromKey(key));
        changeDirection(key); //Faz um changeDirection para qualquer movimento
        return elementAtNewPoint instanceof Parede;
    }

    public boolean movingToBoxValid(int key, HashMap<Point2D, GameElement> tileMap) {
        Point2D point = getPosition();
        Direction dir = Direction.directionFor(key);
        Point2D newPoint = point.plus(dir.asVector()); 
        Point2D newBoxPoint = newPoint.plus(dir.asVector());

        GameElement elementAtNewPoint = tileMap.get(newPoint);

        if (elementAtNewPoint instanceof Caixote) {
            Caixote c = (Caixote) elementAtNewPoint;
            return c.isPositionValid(newBoxPoint, tileMap);
        }
        return false;
    }

    public boolean movingToBattery(int key, HashMap<Point2D, GameElement> tileMap) {
        return tileMap.get(getNewPointFromKey(key)) instanceof Bateria;
    }
    
    public void resetEmpilhadora(){
    	curBattery = maxBattery;
    	setPosition(initialPosition);
    	imageName = "Empilhadora_U";
    	moves = 0;
    }
    
    private Point2D getNewPointFromKey(int key){
        Point2D point = getPosition();
        Direction dir = Direction.directionFor(key);
        Point2D newPoint = point.plus(dir.asVector());
        return newPoint;
    }

    
    public void setInitialPosition(Point2D position){
    	initialPosition = position;
    }

    public int getBateria() {
        return curBattery;
    }

    public void decBateria() {
        curBattery--;
    }

    public void addBattery(int batteryAmount) {
        setBateria(Math.min(getBateria() + batteryAmount, maxBattery + 1));
    }

    private void setBateria(int batteryAmount) {
        curBattery = batteryAmount;
    }

    public void consumeBattery(Bateria b) {
        addBattery(b.getBattery());
    }

	public int getMoves() {
		return moves;
	}
	
    @Override
    public String getName() {
        return imageName;
    }

    @Override
    public int getLayer() {
        return 1;
    }

	@Override
	public void move(Point2D p) {
	    Point2D oldPosition = getPosition();
	    setPosition(p);
	    GameEngine.getInstance().relocateObject(oldPosition, p, this);
	    decBateria();
        moves++;
	}
}
