import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends GameObject{
	Handler handler;
	private int width, height, h = 0, w = 0;
	public static boolean canjump = false, touchingground, facingforward = true;
	public static int mana = 100, key = 0;
	Clock clock, manaregen, landtimer;
	public Sprite sprite = new Sprite();

	public BufferedImage[] walking = {sprite.getSprite(49, 106, 38, 67, "sorlosheet"), sprite.getSprite(129, 105, 42, 68, "sorlosheet"), 
			sprite.getSprite(210, 106, 44, 67, "sorlosheet"), sprite.getSprite(289, 104, 331 - 289, 172 - 104, "sorlosheet")};
	public BufferedImage[] standing = {sprite.getSprite(72, 16, 46, 68, "sorlosheet")};
	public BufferedImage[] shooting = {sprite.getSprite(84, 550, 133 - 84, 619 - 550, "sorlosheet"), 
			sprite.getSprite(154, 552, 212 - 154, 622 - 552, "sorlosheet"), sprite.getSprite(234, 553, 289 - 234, 622 - 553, "sorlosheet"), 
			sprite.getSprite(317, 554, 365 - 317, 621 - 554, "sorlosheet"), sprite.getSprite(387, 555, 432 - 387, 623 - 555, "sorlosheet")};
	public BufferedImage[] jumping = {sprite.getSprite(44, 200, 89 - 44, 262 - 200, "sorlosheet"), 
			sprite.getSprite(108, 186, 151 - 108, 263 - 186, "sorlosheet"), sprite.getSprite(173, 178, 215 - 173, 265 - 178, "sorlosheet"), 
			sprite.getSprite(108, 186, 151 - 108, 263 - 186, "sorlosheet"), sprite.getSprite(44, 200, 89 - 44, 262 - 200, "sorlosheet")};
	public BufferedImage[] falling = {sprite.getSprite(108, 186, 151 - 108, 263 - 186, "sorlosheet")};
	
	public Animation walk = new Animation(walking, 5, true);
	public Animation stand = new Animation(standing, 10, false);
	public Animation shoot = new Animation(shooting, 3, false);
	public Animation jump = new Animation(jumping, 5, false);
	public Animation fall = new Animation(falling, 10, false);
	
	public Animation animation = stand;
	
    public Player(int x, int y, ID id, Handler handler, int width, int height) {
        super(x, y, width, height, id);
        this.handler = handler;	
		manaregen = new Clock(0);
		clock = new Clock(0);
		handler.addClock(manaregen);
		handler.addClock(clock);
		animation.start();
    }
    public void tick() {
    	//These store the old height and width
    	h = height;
    	w = width;    	
    	
    	x = x + velX + spdx;     	
    	velY = velY + grav;
    	y = y + velY + spdy; 
    	
    	//Set mana regen timer to a valid value
    	manaregen.setTime(Game.clamp(manaregen.getTime(), 0, 60));
    	
    	if (manaregen.getTime() == 0){    		
    		mana = mana + 1;
    		manaregen.setTime(60);
    	}
    	
    	mana = Game.clamp(mana, 0, 100);   	                

    	//Scroll the game if necessary
    	checkScroll();
    	//Don't let the player fall/jump out of the game
        y = Game.clamp(y, 0, Game.HEIGHT - 60);	
        
        collision();    
        
    	if(velY != 0) canjump = false;
    	
    	//Deal with the shoot animation ending
    	if(animation == shoot){ 
    		if(animation.done == true){ 
    			animation.reset();
    			KeyInput.canshoot = true;
    			if(canjump == true) animation = stand;
    			if(canjump == false) animation = fall;
    		}
    	}
    	
    	//Start the fall animation if needed
        if(touchingground == false){
        	if(animation == walk || animation == stand) animation = fall;
        	if((animation == jump || animation == shoot) && animation.done == true) animation = fall;
        }
        //This gets set true elsewhere as long as we are on the ground at the beginning of the next tick
        touchingground = false;
        
        animation.update();
        
    	width = animation.getSprite().getWidth();
    	height = animation.getSprite().getHeight();
    	//After updating the animation we change the size of the object to match the size of the sprite
    	//Because the sprite is drawn left to right from the x coordinate if facing right, and right to left from (x coord + width)
    	//if facing left, we move the object slightly where necessary if the width or height changes so it doesn't look like
    	//its jumping around
    	if(height < h) y = y + (h - height);
    	if(height > h) y = y - (height - h);
    	if(width > w && facingforward == false) x = x - (width - w);
    	if(width < w && facingforward == false) x = x - (width - w);
    }
    
    public void collision(){
    	for(int i = 0; i < handler.object.size(); i++){
    		GameObject tempObject = handler.object.get(i);
    		if(getBounds().intersects(tempObject.getBounds())){
	    		if(tempObject.getId() == ID.BasicEnemy || tempObject.getId() == ID.Boss || tempObject.getId() == ID.DoorEnemy){
	    		}
	    		if(tempObject.getId() == ID.Wall){
	    			hitBoundary(tempObject.getBounds());
	    			touchingground = true;
	    		}
	    		if(tempObject.getId() == ID.Door){
		    		if(key > 0){
		    			clock.setTime(5);
		    			key = key -1;
		    		}
	    			
	    			if(clock.getTime() == 1){ 
	    				handler.removeObject(tempObject);
					} else {
					hitBoundary(tempObject.getBounds());
					}
			
	    		}
	    		if(tempObject.getId() == ID.Mana){
	    			handler.removeObject(tempObject);
	        		mana = mana + 50;
	    		}
	    		if(tempObject.getId() == ID.Key){
	    			handler.removeObject(tempObject);
	        		key = key + 1;
	    		}  
    		}
    	}
    }
    
    private void hitBoundary(Rectangle rect) {
    	Rectangle intersection = getBounds().intersection(rect);
    	if(intersection.getHeight() > intersection.getWidth()){
			if(intersection.getX() > x && intersection.getX() < x + width) x = (int) (x - intersection.getWidth());
			if(intersection.getX() + intersection.getWidth() > x && intersection.getX() + intersection.getWidth() < x + width) x = (int) (intersection.getX() + intersection.getWidth());
    	}
    	if(intersection.getHeight() < intersection.getWidth()){
			if(intersection.getY() > y && intersection.getY() < y + height){
				y = (int) (intersection.getY() - height);			
			}
			if(intersection.getY() <  y + height && intersection.getY() > y){
			 	y = (int) (intersection.getY() + height);
				velY = 2;
			}
			if(intersection.getY() == y + height){				
				velY = 0; 
				canjump = true;
				//Stand on the ground when we land
				if((animation == jump && animation.stopped == true) || animation == fall){					
					animation = stand;
					animation.start();
				}
	    	}
			if(intersection.getY() == y){
				velY = 2; 
	    	}
		}
    }
    public void checkScroll(){
    	//Scrolling left and right
        if(abs(x - Game.WIDTH) > 350 && x >= 350){
        	for(int i = 0; i < handler.object.size(); i++){
        		GameObject tempObject = handler.object.get(i);
        		tempObject.scrollX(0);
        	}
        }
        if(abs(x - Game.WIDTH) <= 350 ){
        	for(int i = 0; i < handler.object.size(); i++){
        		GameObject tempObject = handler.object.get(i);
        		tempObject.scrollX(-10);
        	}
        }
        if(abs(x) < 350 ){
        	for(int i = 0; i < handler.object.size(); i++){
        		GameObject tempObject = handler.object.get(i);
        		tempObject.scrollX(10);
        	}
        }
        //Scrolling up and down
        if(abs(y - Game.HEIGHT) > 200 && y >= 350){
        	for(int i = 0; i < handler.object.size(); i++){
        		GameObject tempObject = handler.object.get(i);
        		tempObject.scrollY(0);
        	}
        }
        if(abs(y - Game.HEIGHT) <= 200 ){
        	for(int i = 0; i < handler.object.size(); i++){
        		GameObject tempObject = handler.object.get(i);
        		if(velY >= 0) tempObject.scrollY(-velY);
        	}
        }
        if(abs(y) < 350 ){
        	for(int i = 0; i < handler.object.size(); i++){
        		GameObject tempObject = handler.object.get(i);
        		if(velY <= 0) tempObject.scrollY(-velY);
        	}
        }
        //For getting player back on screen quickly.
        if(x - Game.WIDTH > 0 ){
        	for(int i = 0; i < handler.object.size(); i++){
        		GameObject tempObject = handler.object.get(i);
        		tempObject.scrollX(-1000);
        	}
        }
        if(y < 0 ){
        	for(int i = 0; i < handler.object.size(); i++){
        		GameObject tempObject = handler.object.get(i);
        		tempObject.scrollY(1000);
        	}
        }
    }

	public void render(Graphics g) {
		if(facingforward == true) g.drawImage(animation.getSprite(), x, y, width, height, null);
		else g.drawImage(animation.getSprite(), x + width, y, -width, height, null);
    }
    public Rectangle getBounds(){
    	return new Rectangle(x, y, width, height);
    }
    
    public static boolean getJump(){
    	return canjump;
    }
    
	public static void setJump(boolean b) {
		canjump = b;
		}
    public static void setDir(boolean dir){
    	facingforward = dir;
    }
    public static boolean getDir(){
    	return facingforward;
    }
    public static int abs(int x) {
    	if(x < 0) x *= -1;
		return x;
	}
	public void died() {
		
	}
}