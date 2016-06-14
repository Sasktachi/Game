import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
    private Handler handler;
    //public static Clock clock;
    public static boolean canshoot = true;
    public static boolean shiftheld = false, aheld = false, dheld = false, spaceheld = false;
    public KeyInput(Handler handler){
        this.handler = handler;
        //clock = new Clock(0);
        //handler.addClock(clock);
    }
    public void tick() {
    	//Searches all objects for the player object
    	for(int i = 0; i < handler.object.size(); i++){
            GameObject tempObject = handler.object.get(i);
            if(tempObject.getId() == ID.Player){    
            	//Makes the walk animation play backwards if the player is both walking and holding shift
            	//(Shift changes the direction we face and is primarily used for kiting)
            	if(((Player) (tempObject)).animation == ((Player) (tempObject)).walk && shiftheld == true){
            		((Player) (tempObject)).animation.setAnimationDirection(-1);
            	}            	
            	else ((Player) (tempObject)).animation.setAnimationDirection(1);
            	
            	//If the player is moving horizontally and is on the ground and is in the stand animation, 
            	//starts up the walk animation instead
            	if(((Player) (tempObject)).getVelX() != 0){
            		if(Player.canjump == true && ((Player) (tempObject)).animation == ((Player) (tempObject)).stand){
            			((Player) (tempObject)).animation = ((Player) (tempObject)).walk;
            			((Player) (tempObject)).animation.start();
            		}
            	}
            }
    	}
    }
    public void keyPressed(KeyEvent e){
        
    	int key = e.getKeyCode();     
    	//Finds the player
        for(int i = 0; i < handler.object.size(); i++){
            GameObject tempObject = handler.object.get(i);
            
            if(tempObject.getId() == ID.Player){
            	//A causes the player to walk left, sets a boolean saying we are holding A, 
            	//and figures out whether the player is facing left or right based on whether shift is held
                if(key == KeyEvent.VK_A){
                	aheld = true;
                	((Player) (tempObject)).setVelX(-10);                	
                	if(Player.getDir() == true && shiftheld == false) Player.setDir(false);
                	if(Player.getDir() == false && shiftheld == true) Player.setDir(true);
                }
                
                //D mirrors A
                if(key == KeyEvent.VK_D){
                	dheld = true;
                	((Player) (tempObject)).setVelX(10);                	
                	if(Player.getDir() == false && shiftheld == false) Player.setDir(true);
                	if(Player.getDir() == true && shiftheld == true) Player.setDir(false);
                }
                
                //If space is pressed and the player is allowed to jump and is not currently shooting it gives the player an upward velocity,
                //tells us we are holding space, and changes the animation to jump
                if(key == KeyEvent.VK_SPACE && (Player.getJump() == true)){
                	if(((Player) (tempObject)).animation != ((Player) (tempObject)).shoot){
                		((Player) (tempObject)).setVelY(-15);
                	spaceheld = true;
                	((Player) (tempObject)).animation.stop();
                	((Player) (tempObject)).animation = ((Player) (tempObject)).jump;
                	((Player) (tempObject)).animation.reset();
                	((Player) (tempObject)).animation.start();
                	}
                }
                //Shift makes the player face the opposite direction
                if(key == KeyEvent.VK_SHIFT){
                	if(shiftheld == false){
                		if(Player.getDir() == true) Player.setDir(false);
                		else Player.setDir(true);
                	}
                	shiftheld = true;
                }
                //W checks if we can shoot, puts us in the shoot animation, spawns the projectile, charges us 10 mana, 
                //and makes it so we can't shoot (when the animation ends something in the Player class lets us shoot again)
                if(key == KeyEvent.VK_W && canshoot == true && Player.mana >= 10) {
                	((Player) (tempObject)).animation.stop();
                	((Player) (tempObject)).animation = ((Player) (tempObject)).shoot;
                	((Player) (tempObject)).animation.reset();
                	((Player) (tempObject)).animation.start();
        			if (Player.getDir() == true) {
        				handler.addObject(new Projectile(((Player) (tempObject)).getX() + ((Player) (tempObject)).getWidth(), ((Player) (tempObject)).getY() + ((Player) (tempObject)).getHeight() / 3, ID.PlayerProj, handler, 25, 16));
        				Player.mana = Player.mana - 10;
        				canshoot = false;
        			}
        			if (Player.getDir() == false) {
        				handler.addObject(new Projectile(((Player) (tempObject)).getX(), ((Player) (tempObject)).getY() + ((Player) (tempObject)).getHeight() / 3, ID.PlayerProj, handler, 25, 16));
        				Player.mana = Player.mana - 10;
        				canshoot = false;
        			}            		
                }
            }
            
            
        }
        //Escape ends the game
        if(key == KeyEvent.VK_ESCAPE) System.exit(1);
    }
    
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        //Releasing space tells us we aren't holding space anymore
        if(key == KeyEvent.VK_SPACE) spaceheld = false;
        
        //Finds the player
        for(int i = 0; i < handler.object.size(); i++){
            GameObject tempObject = handler.object.get(i);
            
            if(tempObject.getId() == ID.Player){
            	//If we were holding A and moving left, when we release A we stop moving and set the aheld boolean false.
            	//If we're on the ground and not shooting we are put in the stand animation.
                if(key == KeyEvent.VK_A && ((Player) (tempObject)).getVelX() < 0){ 
                	((Player) (tempObject)).setVelX(0);
                	aheld = false;
                	if (Player.canjump == true && ((Player) (tempObject)).animation != ((Player) (tempObject)).shoot) {
						((Player) (tempObject)).animation.stop();
						((Player) (tempObject)).animation.reset();
						((Player) (tempObject)).animation = ((Player) (tempObject)).stand;
					}
            	}
                //If we were holding D and moving right we stop moving etc
                if(key == KeyEvent.VK_D && ((Player) (tempObject)).getVelX() > 0){
                	((Player) (tempObject)).setVelX(0);
                	dheld = false;
                    if (Player.canjump == true && ((Player) (tempObject)).animation != ((Player) (tempObject)).shoot) {
						((Player) (tempObject)).animation.stop();
						((Player) (tempObject)).animation.reset();
						((Player) (tempObject)).animation = ((Player) (tempObject)).stand;
					}
                }
                if(key == KeyEvent.VK_W){ 
                }
                //Switches us back to facing the other way
                if(key == KeyEvent.VK_SHIFT){	
                	shiftheld = false;
                		if(Player.getDir() == true) Player.setDir(false);
                		else Player.setDir(true);
                }
            }
            
        }
    }





}