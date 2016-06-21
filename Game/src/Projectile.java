import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Projectile extends GameObject {
	Handler handler;
	protected int width, height;
	Clock clock;
	public boolean dir;
	public Sprite sprite = new Sprite();
	public BufferedImage[] shot = {sprite.getSprite(484, 583, 25, 16, "sorlosheet"), sprite.getSprite(518, 583, 25, 16, "sorlosheet"), 
			sprite.getSprite(551, 583, 25, 16, "sorlosheet")};	
	public Animation shoot = new Animation(shot, 3, true);
	
	public Projectile(int x, int y, ID id, Handler handler, int width, int height){
		super(x, y, width, height, id);
		this.width = width;
		this.height = height;
		this.handler = handler;		
    		if(Player.facingforward == true){
    			velX = 22;
    			dir = true;
    		}
    		else{ 
    			velX = -22;
    			dir = false;
    		}
		clock = new Clock(15);
		handler.addClock(clock);
		shoot.reset();
		shoot.start();
	}

	public void tick() {
		x += velX + spdx;
		y += velY + spdy;
		collision();
		if(clock.getTime() <= 0){
			handler.removeObject(this);
			clock.setTime(15);
		}
		shoot.update();
	}

	public void collision() {
		for(int i = 0; i < handler.object.size(); i++){
    		GameObject tempObject = handler.object.get(i);
    		//Find any enemies
			if(tempObject.getId() == ID.BasicEnemy || tempObject.getId() == ID.Boss || tempObject.getId() == ID.DoorEnemy){
				//If we hit an enemy destroy this projectile and damage the enemy
				if(getBounds().intersects(tempObject.getBounds())){
					tempObject.setHEALTH(tempObject.getHEALTH() - 25) ;
					handler.removeObject(this);
					//If the enemy we hit is dead get rid of it and call its died() method
					if(tempObject.getHEALTH() <= 0){
						handler.removeObject(tempObject);
						tempObject.died();
						//Spawn a key if the enemy is supposed to drop one
						//This will have to move to the died() method for enemies that drop keys
						if(tempObject.getId() == ID.DoorEnemy || tempObject.getId() == ID.Boss){
							handler.addObject(new Key(x, y, 32, 32, handler, ID.Key));
						}
					}
					
				}
			}
			if(tempObject.getId() == ID.Wall || tempObject.getId() == ID.Door){
				if(getBounds().intersects(tempObject.getBounds())){
					hitBoundary(tempObject.getBounds());
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
				velY = 0;
			}
			if(intersection.getY() == y + height){				
				velY = 0; 
	    	}
			if(intersection.getY() == y){
				velY = 2; 
	    	}
		}
    }
	public void render(Graphics g) {
		if(dir == true) g.drawImage(shoot.getSprite(), x, y, width, height, null);
		if(dir == false) g.drawImage(shoot.getSprite(), x + width, y, -width, height, null);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public void died() {
	}
}
