package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
	private int level = 1;
	private List<ImageTile> tileList;	// Lista de imagens
	private Empilhadora bobcat = null;	        // Referencia para a empilhadora

	
	
	

	// Construtor - neste exemplo apenas inicializa uma lista de ImageTiles
	private GameEngine() {
		
		gui = ImageMatrixGUI.getInstance();    // 1. obter instancia ativa de ImageMatrixGUI	
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);  // 2. configurar as dimensoes 
		gui.registerObserver(this);            // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go();                              // 4. lancar a GUI

		
		tileList = new ArrayList<>();   
		
	}

	
	
	public static GameEngine getInstance() {
		if (INSTANCE==null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	public void start(){
		
		createGame();  //PORQUE É QUE NAO PRECISO DE UM GUI.UPDATE()????

		gui.setStatusMessage("Sokoban Starter - demo");
	}
	
	@Override
	public void update(Observed source) {
		int key = gui.keyPressed();

		//Se o jogador carregou numa das teclas direcionais
		if(Direction.isDirection(key)){
			bobcat.move(key);
			System.out.println("OLA");
		}
		//Se o jogador carregou na tecla R
		else if (key==KeyEvent.VK_R)
			restartLevel();
		gui.update();
	}
	
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
	                        tileList.add(new Parede(ponto));
	                        break;
	                    case 'E':
	                    	tileList.add(new Chao(ponto));
	                        bobcat = new Empilhadora(ponto);
	                        tileList.add(bobcat);
	                        break;
	                    case 'C':
	                        tileList.add(new Caixote(ponto));
	                        break;
	                    case '=':
	                        tileList.add(new Vazio(ponto));
	                        break;
	                    case 'B':
	                        tileList.add(new Bateria(ponto));
	                        break;
	                    default:
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
}
