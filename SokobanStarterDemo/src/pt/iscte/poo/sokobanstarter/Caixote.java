package pt.iscte.poo.sokobanstarter;

import java.util.HashMap;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Caixote extends GameElement implements Pushable{

	
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
	public void push(Direction dir, HashMap<Point2D, GameElement> tileMap) {
	    Point2D oldPosition = super.getPosition();
	    Point2D newPosition = super.getPosition().plus(dir.asVector());

	    setPosition(newPosition);
	    tileMap.remove(oldPosition); // Clear the old position
	    tileMap.put(newPosition, this); // Update the tileMap with the existing Caixote in the new position
	}
    
    @Override
    public boolean isPushPositionValid(Point2D position, HashMap<Point2D, GameElement> tileMap) {
    	
        GameElement elementAtNewPosition = tileMap.get(position);

        return elementAtNewPosition == null || elementAtNewPosition instanceof Bateria || elementAtNewPosition instanceof Empilhadora || elementAtNewPosition instanceof Alvo;
    }
	

}
