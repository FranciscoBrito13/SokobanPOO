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
    List<Alvo> alvos;
	private int level = 0;
	//private List<ImageTile> tileList;	
	private Empilhadora bobcat = null;	  
	private HashMap<Point2D, GameElement> tileMap; //COLOCAR APENAS OS OBJETOS IMPORTANTES NO HASH
	

	// Construtor - neste exemplo apenas inicializa uma lista de ImageTiles
	private GameEngine() {
		
		gui = ImageMatrixGUI.getInstance();    // 1. obter instancia ativa de ImageMatrixGUI	
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);  // 2. configurar as dimensoes 
		gui.registerObserver(this);            // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go();                              // 4. lancar a GUI

		
		//tileList = new ArrayList<>();   
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
	    Direction d = Direction.directionFor(key);
	    
	    if(d == null)return;
	    
	    // Verifica se o bobcat não tem uma parede naquela posição
	    Point2D nextP = bobcat.getPosition().plus(d.asVector());
	    GameElement g = getGameElement(nextP);
	    boolean canMove = g == null ? true :  g.interact(bobcat);
	    //interactWithObjects(key);
	    if(canMove)bobcat.move(nextP);
	    bobcat.changeDirection(key);
	    
	    
	    if (isGameOver()) {
	        displayGameOverPanel();
	        restartLevel();
	    }
	        
	        
	    if(boxInPlace()){
	    	startAnotherLevel();
	    }
	        
	    

	    // O jogo é resetado ao carregar na tecla R
	    else if (key == KeyEvent.VK_R) {
	        restartLevel();
	        
	        
	    } else if(key == KeyEvent.VK_N){
	    	startAnotherLevel();
	    }

	    gui.setStatusMessage("Level: " + level + " Energy:" + bobcat.getBateria() + " Moves: " + bobcat.getMoves());
	    gui.update(); // Update the GUI after the movement
	}

    private boolean boxInPlace() { 
        List<Caixote> caixotes = new ArrayList<>();

        for (GameElement ge : tileMap.values()) {//Fazer filtro x2
        	if (ge instanceof Caixote) {
                caixotes.add((Caixote) ge);
            }
        }

        int qntAlvosAtivos = 0;

        for (Alvo a : alvos) {
            boolean isTargetActive = false;
            for (Caixote c : caixotes) {
                if (c.getPosition().equals(a.getPosition())) {
                    isTargetActive = true;
                    break;
                }
            }
            if (isTargetActive) {
                qntAlvosAtivos++;
            }
        }
        
        System.out.println(alvos.size() + "  " + qntAlvosAtivos);
        return alvos.size() == qntAlvosAtivos;
    }

	private void displayGameOverPanel() {
		System.out.println("Ainda nao estou implementado");
	}
	

	
	public void createGame() {
		
	    try {
	    	List<ImageTile> tileList = new ArrayList<>();
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
	                    	tileList.add(new Chao(ponto));
	                        bobcat = Empilhadora.getInstance(ponto);
	                        bobcat.setInitialPosition(ponto);
	                    	tileMap.put(ponto, bobcat);
	                        tileList.add(bobcat);
	                        break;
	                    case 'C':
	                    	Caixote caixote = new Caixote(ponto);
	                        tileMap.put(ponto, caixote);
	                        tileList.add(caixote);
	                        tileList.add(new Chao(ponto));
	                        
	                        break;
	                    case '=':
	                        tileList.add(new Vazio(ponto));
	                        break;
	                    case 'B':
	                    	Bateria b = new Bateria(ponto);
	                    	tileMap.put(ponto, b);
	                        tileList.add(b);
	                        break;
	                    case 'X':
	                    	Alvo a = new Alvo(ponto);
	                    	tileMap.put(ponto, a);
	                        tileList.add(a);
	                    default:

	                        tileList.add(new Chao(ponto));
	                        break;
	                }
	            }
	            y++;
	            
	        }
	        alvos = loadAlvos();
	        gui.addImages(tileList);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean isGameOver(){
		return bobcat.getBateria() <= 0;
	}
	
	public void startAnotherLevel() {
		level++;
	    tileMap.clear();
	    gui.clearImages();
	    createGame(); 
	    bobcat.resetEmpilhadora();
	}

	public void restartLevel() {
	    tileMap.clear();
	    gui.clearImages();
	    createGame();
	    bobcat.resetEmpilhadora();
	    gui.update();
	}

	
	public void relocateObject(Point2D old, Point2D newP, GameElement g) {
	    tileMap.remove(old); // Clear the old position
	    tileMap.put(newP, g); // Update the tileMap with the existing Caixote in the new position
	}
	

	private List<Alvo> loadAlvos() {
	    List<Alvo> alvos = new ArrayList<>();
	    for (GameElement ge : tileMap.values()) {
	        if (ge instanceof Alvo) {
	            alvos.add((Alvo) ge);
	        }
	    }
	    return alvos;
	}
	
	public GameElement getGameElement(Point2D p) {
		return tileMap.get(p);
	}

	public void removeElement(Point2D p) {
		tileMap.remove(p);
	}

}
