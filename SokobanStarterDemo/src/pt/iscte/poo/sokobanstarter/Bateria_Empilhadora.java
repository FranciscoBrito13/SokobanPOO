package pt.iscte.poo.sokobanstarter;

public class Bateria_Empilhadora {
	
	private int initialBattery;
	private int bateria;
	
	
	public Bateria_Empilhadora(int bateria) {
		this.bateria = bateria;
		initialBattery = bateria;
	}
	
	public void bobcatMoved(){
		bateria--;
	}
	
//	private void setBattery(int bateria){
//		this.bateria = bateria;
//	}

	
	public void add(int bateria){
		this.bateria += bateria;
	}
	
	public int getBateria(){
		return bateria;
	}
	
	public void resetBattery(){
		bateria = initialBattery;
	}
	
}
