import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Key extends GameObject{
	Handler handler;
	private int width, height;
	public Key(int x, int y, int width, int height, Handler handler, ID id) {
		super(x, y, width, height, id);
		this.handler = handler;
		this.width = width;
		this.height = height;
		velY = -15;
	}
	public void tick() {
    	x = x + velX + spdx; 
        velY = velY + grav;
    	y = y + velY + spdy;
    	
    	collision();
		
	}
	public void collision(){
    	for(int i = 0; i < handler.object.size(); i++){
    		GameObject tempObject = handler.object.get(i);
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
			if(intersection.getX() > x && intersection.getX() < x + 32) x = (int) (x - intersection.getWidth());
			if(intersection.getX() + intersection.getWidth() > x && intersection.getX() + intersection.getWidth() < x + 32) x = (int) (intersection.getX() + intersection.getWidth());
    	}
    	if(intersection.getHeight() < intersection.getWidth()){
			if(intersection.getY() > y && intersection.getY() < y + 32) y = (int) (intersection.getY() - 32);;
			if(intersection.getY() <  y + 32 && intersection.getY() > y){
			 	y = (int) (intersection.getY() + 32);
				velY = 2;
			}
			if(intersection.getY() == y + 32){
				velY = 0; 
	    	}
			if(intersection.getY() == y){
				velY = 2; 
	    	}
		}
    }
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x, y, 32, 32);
	}


	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	public void died() {
	}

}
