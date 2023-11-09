package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class GameEngine implements Observer {

	// Dimensoes da grelha de jogo
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;

	private static GameEngine INSTANCE; // Referencia para o unico objeto GameEngine (singleton)
	private ImageMatrixGUI gui;  		// Referencia para ImageMatrixGUI (janela de interface com o utilizador) 

	private int level = 2;
	private List<ImageTile> tileList;	// Lista de imagens
	private Empilhadora bobcat = null;	        // Referencia para a empilhadora
	private HashMap<Point2D, GameElement> tileMap;

	

	// Construtor - neste exemplo apenas inicializa uma lista de ImageTiles
	private GameEngine() {
		
		gui = ImageMatrixGUI.getInstance();    // 1. obter instancia ativa de ImageMatrixGUI	
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);  // 2. configurar as dimensoes 
		gui.registerObserver(this);            // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go();                              // 4. lancar a GUI

		
		tileList = new ArrayList<>();   
		tileMap = new HashMap<Point2D, GameElement>();
		
	}

	
	
	public static GameEngine getInstance() {
		if (INSTANCE==null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	public void start(){
		
		createGame();  //PORQUE É QUE NAO PRECISO DE UM GUI.UPDATE()????

		gui.setStatusMessage("Nivel" + level);
	}
	public boolean canMoveTo(int key){ //PERGUNTAR AO STOR SE PODE SER AQUI
		Point2D point = bobcat.getPosition();
		Direction dir = Direction.directionFor(key); 
		Point2D newPoint = point.plus(dir.asVector());
		if(tileMap.get(newPoint) instanceof Parede){
			bobcat.changeDirection(key);
			return false;
		} 
		return true;
	} 
	
	
	@Override
	public void update(Observed source) {
		int key = gui.keyPressed();

		//Se o jogador carregou numa das teclas direcionais
		if(Direction.isDirection(key) && canMoveTo(key)){
			bobcat.move(key);

		}
		//Se o jogador carregou na tecla R
		else if (key==KeyEvent.VK_R)
			restartLevel();
		
		gui.update();  //Update na GUI sempre que se move
	}
	
	
	//Cria o jogo tendo em conta o atribuo
	public void createGame() {
	    try {
	        Scanner s = new Scanner(new File("levels/level" + level + ".txt"));
	        int y = 0;

	        while (s.hasNext()) {
	            String line = s.nextLine();
	            for (int x = 0; x < line.length(); x++) {
	                Point2D ponto = new Point2D(x, y);

	                char ch = line.charAt(x);

	                switch (ch) {
	                    case '#':
	                    	tileMap.put(ponto, new Parede(ponto));
	                        tileList.add(new Parede(ponto));
	                        break;
	                    case 'E':
	                    	tileMap.put(ponto, new Chao(ponto));
	                    	tileList.add(new Chao(ponto));
	                        bobcat = new Empilhadora(ponto);
	                    	tileMap.put(ponto, bobcat);
	                        tileList.add(bobcat);
	                        break;
	                    case 'C':
	                    	tileMap.put(ponto, new Caixote(ponto));
	                        tileList.add(new Caixote(ponto));
	                        break;
	                    case '=':
	                    	tileMap.put(ponto, new Vazio(ponto));
	                        tileList.add(new Vazio(ponto));
	                        break;
	                    case 'B':
	                    	tileMap.put(ponto, new Bateria(ponto));
	                        tileList.add(new Bateria(ponto));
	                        break;
	                    default:
	                    	tileMap.put(ponto, new Chao(ponto));
	                        tileList.add(new Chao(ponto));
	                        break;
	                }
	            }
	            y++;
	        }
	        sendImagesToGUI();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
		sendImagesToGUI();
	}



	public void startAnotherLevel() {
	    tileList.clear();
	    gui.clearImages();
	    bobcat = null;
	    createGame(); 
	}

	public void restartLevel() {
	    startAnotherLevel();
	    gui.update();
	}

	private void sendImagesToGUI() {
		gui.addImages(tileList);
	}
	

	// Getter do tileMap
	public HashMap<Point2D, GameElement> getTileMap() {
		return tileMap;
	}
}
