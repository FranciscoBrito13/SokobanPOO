package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

//Interface que implementa as classes que tem movimento
public interface Movable {
	
	//Procedimento que implementa o movimento dos objetos que implementam esta interface
	public void move(int keyCode);
	
	
	//Verifica se a posicao dada se encontra dentro da grelha de jogo
	public default boolean doesntPassBorder(Point2D p) {
		if (p.getX() < 0) return false;
		if (p.getY() < 0) return false;
		if (p.getX() >= GameEngine.GRID_WIDTH) return false;
		if (p.getY() >= GameEngine.GRID_HEIGHT) return false;
		return true;
	}
}
