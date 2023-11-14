package pt.iscte.poo.sokobanstarter;

import java.util.HashMap;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public interface Pushable {

	public void push(Direction dir, HashMap<Point2D, GameElement> tileMap);
	
	public boolean isPushPositionValid(Point2D position, HashMap<Point2D, GameElement> tileMap);
}
