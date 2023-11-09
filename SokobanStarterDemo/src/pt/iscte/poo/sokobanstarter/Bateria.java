package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Bateria extends GameElement {

	public Bateria(Point2D position) {
		super(position);
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Bateria";
	}


	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 1;
	}

}
