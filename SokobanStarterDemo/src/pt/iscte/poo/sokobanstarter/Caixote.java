package pt.iscte.poo.sokobanstarter;

import java.util.HashMap;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Caixote extends GameElement implements Movable{

	
	public Caixote(Point2D position){
		super(position);
	}
	
	@Override
	public String getName() {
		return "Caixote";
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	
    public void setPosition(Point2D position) {
        this.position = position;
    }

    @Override
	public void move(Point2D p) {
	    Point2D oldPosition = getPosition();
	    setPosition(p);
	    GameEngine.getInstance().relocateObject(oldPosition, p, this);
	}
    
    @Override
    public boolean isPositionValid(Point2D position, HashMap<Point2D, GameElement> tileMap) {
        GameElement elementAtNewPosition = tileMap.get(position);
        return elementAtNewPosition == null || elementAtNewPosition instanceof Bateria || elementAtNewPosition instanceof Empilhadora || elementAtNewPosition instanceof Alvo;
    }
	
    @Override
    public boolean interact(GameElement other) {
    	Vector2D move = Vector2D.movementVector(other.getPosition(), getPosition());
    	Point2D p = getPosition().plus(move);
    	if(GameEngine.getInstance().getGameElement(p) == null){
    		move(p);
    		return true;
    	}
    	return false;
    	
    	
    }
    
    
    
    
}
