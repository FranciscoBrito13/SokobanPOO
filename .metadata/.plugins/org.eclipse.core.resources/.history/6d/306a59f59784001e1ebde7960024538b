package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.*;

import java.util.HashMap;

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
	
	public void setLayer(int x){
		this.layer = x;
	}
	
	public void interact(GameElement other) {} ;
	
	//public abstract void interact();
	//Verifica se a posicao dada se encontra dentro da grelha de jogo NAO É NECESSÁRIO
//	public boolean doesntPassBorder(Point2D p) {
//		if (p.getX() < 0) return false;
//		if (p.getY() < 0) return false;
//		if (p.getX() >= GameEngine.GRID_WIDTH) return false;
//		if (p.getY() >= GameEngine.GRID_HEIGHT) return false;
//		return true;
//	}
	

}
