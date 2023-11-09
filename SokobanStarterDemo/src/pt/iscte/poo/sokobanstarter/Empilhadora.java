package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement implements Movable{
	private String imageName = "Empilhadora_D";

	public Empilhadora(Point2D initialPosition){
		super(initialPosition);
	}
	
	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public Point2D getPosition() {
		return position;
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

	public void move(int keyCode) {
			Direction dir = Direction.directionFor(keyCode);
			changeDirection(keyCode);
			Point2D newPosition = super.getPosition().plus(dir.asVector());
			if (canMoveTo(newPosition) ) {
				setPosition(newPosition);
			}
	}
}