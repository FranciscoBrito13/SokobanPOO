package pt.iscte.poo.sokobanstarter;

import java.util.HashMap;

import pt.iscte.poo.utils.Point2D;

//Interface que implementa as classes que tem movimento
public interface Movable {
	
	//Procedimento que implementa o movimento dos objetos que implementam esta interface
	public void move(int keyCode, HashMap<Point2D, GameElement> tileMap);
	

}
