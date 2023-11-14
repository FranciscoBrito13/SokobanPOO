package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement implements Movable{
	
	private Bateria_Empilhadora bateria = new Bateria_Empilhadora(50);
	
	private String imageName = "Empilhadora_D";

	public Empilhadora(Point2D initialPosition){
		super(initialPosition);
	}
	
	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public int getLayer() {
		return 1;
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

	
	public void move(int keyCode, HashMap<Point2D, GameElement> tileMap) {
		
		//Confirma se a bateria está acima de 0, se estiver move e decrementa, se não dá erro
		Direction dir = Direction.directionFor(keyCode);
		Point2D newPosition = super.getPosition().plus(dir.asVector());
		if(tileMap.get(newPosition) instanceof Caixote){
			return;
		}
		setPosition(newPosition); 
		decBateria();
	}

	public Direction getDirection() {
	    switch (imageName) {
	        case "Empilhadora_D":
	            return Direction.DOWN;
	        case "Empilhadora_U":
	            return Direction.UP;
	        case "Empilhadora_L":
	            return Direction.LEFT;
	        case "Empilhadora_R":
	            return Direction.RIGHT;
	        default:
	            throw new IllegalArgumentException("Invalid imageName: " + imageName);
	    }
	}
	
	public boolean MovingToWall(int key, HashMap<Point2D, GameElement> tileMap) {
	    Point2D point = getPosition();
	    Direction dir = Direction.directionFor(key);
	    Point2D newPoint = point.plus(dir.asVector());

	    GameElement elementAtNewPoint = tileMap.get(newPoint);
	    
	    changeDirection(key);
	    
	    // Check if the new position is blocked by a Wall
	    if (elementAtNewPoint instanceof Parede){ 

	        return true;
		   
	    }
	    return false;
	}
	

	public boolean movingToBoxValid(int key, HashMap<Point2D, GameElement> tileMap) {
	    Point2D point = getPosition();
	    Direction dir = Direction.directionFor(key);
	    Point2D newPoint = point.plus(dir.asVector());
	    Point2D newBoxPoint = newPoint.plus(dir.asVector());
	    
	    
	    GameElement elementAtNewPoint = tileMap.get(newPoint);
	    
	    changeDirection(key);
	    
	    if (elementAtNewPoint instanceof Caixote){
	    	Caixote c = (Caixote) elementAtNewPoint;
	    	if(c.isPushPositionValid(newBoxPoint, tileMap)){
	    		return true;	    	
	    	}
		    
	    }

	    return false;
	}
	
	public boolean movingToBattery(int key, HashMap<Point2D, GameElement> tileMap){
	    Point2D point = getPosition();
	    Direction dir = Direction.directionFor(key);
	    Point2D newPoint = point.plus(dir.asVector());
	    
	    GameElement elementAtNewPoint = tileMap.get(newPoint);
	    
	    changeDirection(key);
	    
	    if (elementAtNewPoint instanceof Bateria){
	    	elementAtNewPoint.setPosition(new Point2D(-1, -1));
	    	return true;
		    
	    }

	    return false;
	}
	

	public int getBateria() {
		return bateria.getBateria();
	}
	public void decBateria(){
		bateria.bobcatMoved();
	}

	public void addBattery(int batteryAmount) {
		setBateria(getBateria() + batteryAmount);
		
	}

	private void setBateria(int batteryAmount) {
		bateria.add(batteryAmount);
		
	}
	
	public void consumeBattery(Bateria b){
		bateria.add(b.getBattery());
	}

}