import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1550691097823471818L;
    
    public static final int WIDTH = 800, HEIGHT = 600;   
    private Thread thread;
    private boolean running = false;
    private Handler handler;
    private HUD hud;
    private KeyInput key;
    private BufferedImage background;
    
    public Game() {
    	//Creates a window and an instance of the game in that window
    	new Window(WIDTH, HEIGHT, "Game", this);
    	//The handler deals with spawning and updating all game objects
        handler = new Handler();  
        //KeyInput deals with all player input
        this.addKeyListener(new KeyInput(handler));
        key = new KeyInput(handler);                
        hud = new HUD(handler);
    	levelone(); 
    }
    
    public void levelone(){
    	//Here we spawn all the objects in our first level
        handler.addObject(new Player(100, 532, ID.Player, handler, 45, 68 ));
        handler.addObject(new Wall(-4100, 600, ID.Wall, handler, 10000, 72));
        handler.addObject(new BasicEnemy(600, 500, ID.BasicEnemy, handler, 100, 100, 100));
        handler.addObject(new EnemyWall(880, 100, ID.EnemyWall, handler, 20, 1720));
        handler.addObject(new EnemyWall(2180, 100, ID.EnemyWall, handler, 20, 1720));
        //////////platforms///////////
        
        //////////walls///////

        //////////floors//////////////

        /////////enemywalls///////////Invisible walls that only stop enemies to stop them from running off cliffs

    }
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    
    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
    	this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0; 
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                tick();
                delta--;
            }
            if(running)
                render();
        }
        stop();
    }   
    
    private void tick() {    	
    	handler.tick();
        hud.tick();
        key.tick();       
    }
    
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();           
        try{
        	background = ImageIO.read(new File("background.png"));
        } catch (IOException e){     
        	e.printStackTrace();
        }
        g.drawImage(background, 0, 0, null);        
        handler.render(g);         
        hud.render(g);		        
        g.dispose();
        bs.show();
    }
    
    public static int clamp(int var, int min, int max){
    	//If a variable is outside the specified range, it puts it back inside the range.
    	if(var >= max)
    		return var = max;
    		
    	else if(var <= min)
    		return var = min;
    	else
    		return var;
    	
		
    }
    
    public static void main(String args[]) {
        new Game();
    }
    
}
