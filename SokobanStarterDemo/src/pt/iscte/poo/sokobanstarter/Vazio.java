package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Vazio extends GameElement {
	
	public Vazio(Point2D position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Vazio";
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}

}
