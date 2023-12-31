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

	    // Verifica se o bobcat não tem uma parede naquela posição
	    if (Direction.isDirection(key) && !(bobcat.MovingToWall(key, tileMap))) {
	    	
	    	interactWithObjects(key, tileMap);
	        
	        if (isGameOver()) {
	        	displayGameOverPanel();
	        	restartLevel();
	        }
	        
	        
	        if(boxInPlace()){
	        	startAnotherLevel();
	        }
	        
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

	private void interactWithObjects(int key, HashMap<Point2D, GameElement> tileMap) {
	    Direction directionFromKey = Direction.directionFor(key);

	    if (bobcat.movingToBoxValid(key, tileMap)) {
	        Caixote c = (Caixote) tileMap.get(bobcat.getPosition().plus(directionFromKey.asVector()));
	        c.push(directionFromKey, tileMap);
	    }
	    
	    //addTargetIfNull(alvos); 

	    if (bobcat.movingToBattery(key, tileMap)) {
	        Point2D batteryPosition = bobcat.getPosition().plus(directionFromKey.asVector());
	        Bateria battery = (Bateria) tileMap.get(batteryPosition);

	        bobcat.consumeBattery(battery);
	        gui.removeImage((Bateria) tileMap.get(batteryPosition));
	        tileMap.remove(batteryPosition);
	        gui.addImage(new Chao(batteryPosition));
	    }

	    bobcat.move(directionFromKey, tileMap);
	}
	
//	private void addTargetIfNull(List<Alvo> alvos) {
//	    for (Alvo a : alvos) {
//	        Point2D targetPosition = a.getPosition();
//	        if (!(tileMap.get(targetPosition) instanceof Alvo || tileMap.get(targetPosition) instanceof Caixote)) {
//	            tileMap.put(targetPosition, new Alvo(targetPosition));
//	            System.out.println("Add um alvo");
//	        }
//	    }
//	}
	
	private List<Alvo> loadAlvos() {
	    List<Alvo> alvos = new ArrayList<>();
	    for (GameElement ge : tileMap.values()) {
	        if (ge instanceof Alvo) {
	            alvos.add((Alvo) ge);
	        }
	    }
	    return alvos;
	}

	

}
