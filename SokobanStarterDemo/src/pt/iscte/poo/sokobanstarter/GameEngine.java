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

	private int level = 5;
	private List<ImageTile> tileList;	
	private Empilhadora bobcat = null;	  
	private HashMap<Point2D, GameElement> tileMap; //COLOCAR APENAS OS OBJETOS IMPORTANTES NO HASH
	

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
		
		createGame(); 

		gui.setStatusMessage("Nivel:" + level + " Bateria:" + bobcat.getBateria());

	}

	
	@Override
	public void update(Observed source) {
	    int key = gui.keyPressed();

	    // Verifica se o bobcat não tem uma parede naquela posição
	    if (Direction.isDirection(key) && !(bobcat.MovingToWall(key, tileMap))) {
	        Direction directionFromKey = Direction.directionFor(key);

	        if (bobcat.movingToBoxValid(key, tileMap)) {
	            Caixote c = (Caixote) tileMap.get(bobcat.getPosition().plus(directionFromKey.asVector()));
	            c.push(directionFromKey, tileMap);
	        }
	        if (bobcat.movingToBattery(key, tileMap)) {
	            Point2D batteryPosition = bobcat.getPosition().plus(directionFromKey.asVector());
	            Bateria battery = (Bateria) tileMap.get(batteryPosition);

	            bobcat.consumeBattery(battery);
	            
	            
	        }
	        bobcat.move(key, tileMap);
	        
	        
	        if (isGameOver()) {
	        	displayGameOverPanel();
	        	restartLevel();
	        }
	        
	        
	        if(boxInPlace()){
	        	level++;
	        	restartLevel();
	        }
	        
	    }

	    // O jogo é resetado ao carregar na tecla R
	    else if (key == KeyEvent.VK_R) {
	        restartLevel();
	    }

	    gui.setStatusMessage("Nivel:" + level + " Bateria:" + bobcat.getBateria());
	    gui.update(); // Update the GUI after the movement
	}

	private boolean boxInPlace() {
		
		return false;
	}


	private void displayGameOverPanel() {

		
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
	                    	//tileMap.put(ponto, new Chao(ponto));
	                    	tileList.add(new Chao(ponto));
	                        bobcat = new Empilhadora(ponto);
	                    	tileMap.put(ponto, bobcat);
	                        tileList.add(bobcat);
	                        break;
	                    case 'C':
	                    	Caixote caixote = new Caixote(ponto);
	                        tileMap.put(ponto, caixote);
	                        tileList.add(caixote);
	                        //tileMap.put(ponto, new Chao(ponto));
	                        tileList.add(new Chao(ponto));
	                        
	                        break;
	                    case '=':
	                    	//tileMap.put(ponto, new Vazio(ponto));
	                        tileList.add(new Vazio(ponto));
	                        break;
	                    case 'B':
	                    	tileMap.put(ponto, new Bateria(ponto));
	                        tileList.add(new Bateria(ponto));
	                        break;
	                    default:
	                    	//tileMap.put(ponto, new Chao(ponto));
	                        tileList.add(new Chao(ponto));
	                        break;
	                }
	            }
	            y++;
	        }
	        //sendImagesToGUI();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
		sendImagesToGUI();
	}
	
	public boolean isGameOver(){
		if(bobcat.getBateria() <= 0){
			return true;
		}
		
		return false;
		
	}
	

	public void startAnotherLevel() {
	    tileList.clear();
	    tileMap.clear();
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
	

}
