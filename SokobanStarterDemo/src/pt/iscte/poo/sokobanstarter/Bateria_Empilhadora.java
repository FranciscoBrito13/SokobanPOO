package pt.iscte.poo.sokobanstarter;

public class Bateria_Empilhadora {
	
	private int bateria;
	
	private GameEngine INSTANCE;
	
	public Bateria_Empilhadora(int bateria) {
		this.bateria = bateria;
		INSTANCE = GameEngine.getInstance();
	}
	
	public void bobcatMoved(){
		bateria--;
	}

	
	public void add(int bateria){
		this.bateria += bateria;
	}
	
	public int getBateria(){
		return bateria;
	}
	
}
