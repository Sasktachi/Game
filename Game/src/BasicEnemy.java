import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class BasicEnemy extends GameObject{

	public int HEALTH;
	private Handler handler;
	private Random r;
	public boolean seen = false;
	private  boolean facingforward = false;
	private int width, height, h, w;
	Sprite sprite = new Sprite();
	Clock hitdelay;
	public BufferedImage[] walking = {sprite.getSprite(6, 105, 40 - 6, 137 - 105, "minotuar"), 
			sprite.getSprite(52, 104, 89 - 52, 137 - 104, "minotuar"), 
			sprite.getSprite(99, 103, 137 - 99, 137 - 103, "minotuar"), 
			sprite.getSprite(148, 103, 185 - 144, 137 - 103, "minotuar"), 
			sprite.getSprite(197, 105, 233 - 197, 137 - 105, "minotuar"), 
			sprite.getSprite(246, 105, 281 - 246, 137 - 105, "minotuar"), 
			sprite.getSprite(296, 104, 329 - 296, 137 - 104, "minotuar"), 
			sprite.getSprite(345, 103, 376 - 345, 137 - 103, "minotuar"), 
			sprite.getSprite(393, 103, 424 - 393, 137 - 103, "minotuar"), 
			sprite.getSprite(439, 105,472 - 439, 137 - 105, "minotuar")};
	public BufferedImage[] standing = {sprite.getSprite(7, 8, 33, 33,  "minotuar")};
	public BufferedImage[] attacking = {sprite.getSprite(7, 152, 33, 33, "minotuar"),
			sprite.getSprite(57, 152, 31, 33, "minotuar"),
			sprite.getSprite(108, 152, 28, 33, "minotuar"),
			sprite.getSprite(159, 152, 26, 33, "minotuar"),
			sprite.getSprite(212, 152, 23, 33, "minotuar"),
			sprite.getSprite(257, 148, 26, 37, "minotuar"),
			sprite.getSprite(291, 146, 37, 39, "minotuar"),
			sprite.getSprite(336, 148, 39, 37, "minotuar"),
			sprite.getSprite(383, 153, 41, 32, "minotuar"),
			sprite.getSprite(435, 152, 37, 33, "minotuar")};
	
	public Animation walk = new Animation(walking, 5, true);
	public Animation stand = new Animation(standing, 10, false);
	public Animation attack = new Animation(attacking, 2, false);
	
	public Animation animation = stand;
	
	public BasicEnemy(int x, int y, ID id, Handler handler, int HEALTH, int width, int height) {
		super(x, y, width, height, id);
		this.handler = handler;
		this.HEALTH = HEALTH;
		setHEALTH(HEALTH);
		r = new Random();
		animation.start();
		hitdelay = new Clock(0);
	}
	
	public Rectangle getBounds(){
    	return new Rectangle(x, y, width, height);
    }

	public void tick() {
		//These store the old height and width
    	h = height;
    	w = width;
    	
    	if(height < h) y = y + (h - height);
    	if(height > h) y = y - (height - h);
    	
    	x = x - (width - w);   
    	
		x += velX + spdx;
		y += velY + spdy;
		velY += grav;		
		
		//y = Game.clamp(y, 0, Game.HEIGHT - 60);
		//x = Game.clamp(x, 0, Game.WIDTH - 32);
		collision();
		ai();
		animation.update();
		//All the sprites are doubled in size to make this guy bigger
    	width = 2 * animation.getSprite().getWidth();
    	height = 2 * animation.getSprite().getHeight();
    	//After updating the animation we change the size of the object to match the size of the sprite
    	//Because the sprite is drawn left to right from the x coordinate if facing right, and right to left from (x coord + width)
    	//if facing left, we move the object slightly where necessary if the width or height changes so it doesn't look like
    	//its jumping around
    	
  


   
	}
	public void ai() {    
		//Finds player
		for(int i = 0; i < handler.object.size(); i++){
    		GameObject tempObject = handler.object.get(i);
    		if(tempObject.getId() == ID.Player){
    			//If player is within 200 pixels
    			if(Player.abs((x - tempObject.getX())) <= 200){
    				//If player is on left, enemy moves left
    				if(tempObject.getX() < x) velX = -8;
    				//If player is on right, enemy moves right
    				if(tempObject.getX() > x) velX = 8;
    			}
    			//If player is more than 400 pixels away enemy stops moving
    			if(Player.abs((x - tempObject.getX())) >= 400) velX = 0;
    			//If enemy is moving and isn't in the walking animation, start walking animation
    			if(velX !=0 && animation != walk){
    				animation.stop();
    				animation = walk;
    				animation.reset();
    				animation.start();
    			}
    			//If enemy isn't moving and isn't in the standing animation, put it in the standing animation
    			if(velX == 0 && animation != stand){
    				animation.stop();
    				animation = stand;
    				animation.reset();
    				animation.start();	
    			}
    		}
		}
		//Right is forward
		if(velX > 0) facingforward = true;
		if(velX < 0) facingforward = false;
	}
	public void collision(){
		//Checks each object in the list to see if it intersects us using the getBounds() method 
		//which just returns the rectangle the object is in
    	for(int i = 0; i < handler.object.size(); i++){
    		GameObject tempObject = handler.object.get(i);
    		if(getBounds().intersects(tempObject.getBounds())){
    			//If the object is not a fireball, a mana orb, a key, a player, an enemy, or an enemy hitbox it calls the hitBoundary() method
    			//I totally had a reason to do it this way, I don't remember what it was though.
	    		if(tempObject.getId() != ID.PlayerProj && tempObject.getId() != ID.Mana && tempObject.getId() != ID.Key && tempObject.getId() != ID.Player && tempObject.getId() != ID.BasicEnemy && tempObject.getId() != ID.EnemyHitbox){    			
	    			hitBoundary(tempObject.getBounds());    			
	    		}
	    		//If the object is a basic enemy with a different object number it calls hitBoundary()
	    		if(tempObject.getId() == ID.BasicEnemy && this.getObjnum() != tempObject.getObjnum()){
	    			hitBoundary(tempObject.getBounds());
	    		}
    		}
    	}
    }
    public void died(){
    	//Sometimes (33%) drops mana orb on death
    	int random = r.nextInt(2);
    	if(random == 0) handler.addObject(new Mana(x, y, ID.Mana, 32, 32, handler));
    }
    private void hitBoundary(Rectangle rect) {
    	//collision() passes us the boundary of the object we hit, here we create a rectangle representing the actual area of intersection
    	//between our enemy and the object we collided with
    	Rectangle intersection = getBounds().intersection(rect);
    	//If we collided more in the x direction than the y direction
    	if(intersection.getHeight() > intersection.getWidth()){
    		//If the x coordinate (left side) of the intersection is farther right than our x coordinate (So we are farther left than the other guy)
    		//And the x coordinate of the intersection is farther left than the right side of us (We aren't completely inside it)
    		//(These two requirements signify that we collided from the left)
    		//We move left an amount equal to the width of the intersection
			if(intersection.getX() > x && intersection.getX() < x + width) x = (int) (x - intersection.getWidth());
			//If we came in from the right we move so that our left side is at the right side of the intersection
			if(intersection.getX() + intersection.getWidth() > x && intersection.getX() + intersection.getWidth() < x + width) x = (int) (intersection.getX() + intersection.getWidth());
			//If we are exactly at the boundary, our velocity gets reversed (We bounce off walls)
			if(intersection.getX() == x + width || intersection.getX() == x) velX *= -1; 
    	}
    	//If the intersection is more in the y direction
    	if(intersection.getHeight() < intersection.getWidth()){
    		//If we fall through something move back up
			if(intersection.getY() > y && intersection.getY() < y + height) y = (int) (intersection.getY() - height);;
			//If we go through the ceiling move back down and bounce off a little bit
			if(intersection.getY() <  y + height && intersection.getY() > y){
			 	y = (int) (intersection.getY() + height);
				velY = 2;
			}
			//If we are exactly on the ground don't move down
			if(intersection.getY() == y + height){
				velY = 0; 
	    	}
			//If we are exactly against the ceiling start falling
			if(intersection.getY() == y){
				velY = 2; 
	    	}
		}

    }
    //I forgot I had this
	public boolean checkSeen(){
		//Find the player
		for(int i = 0; i < handler.object.size(); i++){
    		GameObject tempObject = handler.object.get(i);
    		if(tempObject.getId() == ID.Player){
    			//If the player is near us we have seen him
    			if(Player.abs(x - tempObject.getX()) <= 300 && Player.abs(y - tempObject.getY()) <= 700){
    				seen = true;
    			}
    			//If hes not near us we haven't seen him
    			if(Player.abs(x - tempObject.getX()) >= 300 || Player.abs(y - tempObject.getY()) >= 700){
    				seen = false;
    			}
    		}
    	}
		return seen;
	}
	
	public void render(Graphics g) {
		if(facingforward == false) g.drawImage(animation.getSprite(), x, y, width, height, null);
		else g.drawImage(animation.getSprite(), x + width, y, -width, height, null);
		
		g.setColor(Color.red);
		g.drawString(x+", "+y, 305,255);
	}	
}