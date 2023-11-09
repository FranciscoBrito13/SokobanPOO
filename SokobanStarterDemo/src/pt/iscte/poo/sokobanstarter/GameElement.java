package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.*;
import pt.iscte.poo.gui.ImageTile;


public abstract class GameElement implements ImageTile{


	public Point2D position;
	private String name;
	private int layer;
	
	public GameElement(Point2D position) {
		this.position = position;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return layer;
	}
	
	
	//Define uma nova posi��o para um objeto
	public void setPosition(Point2D position) {
		this.position=position;
	}
}
