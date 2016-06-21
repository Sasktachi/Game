import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;
//This is outdated, we can rewrite it later.
public class Boss extends BasicEnemy {
	private Handler handler;
	private Random r;
	public int HEALTH;
	private int width, height;
	private  boolean canjump = false;
	public Boss(int x, int y, ID id, Handler handler, int HEALTH, int width, int height) {
		super(x, y, id, handler, HEALTH, width, height);
		this.handler = handler;
		this.HEALTH = HEALTH;
		setHEALTH(HEALTH);
		r = new Random();
	}
	public Rectangle getBounds(){
    	return new Rectangle(x, y, width, height);
	}
	public void tick() {
		x += velX + spdx;
		y += velY + spdy;
		velY += grav;
		
		ai();
		collision();
	}
	public void ai() {
		int random = r.nextInt(60);
		if(checkSeen() == true){
			if(random == 1) velX = 5;
			if(random == 0) velX = -5;
		}
		int rand = r.nextInt(60);
		if(rand == 0 && canjump == true){
			velY = -18;
			canjump = false;
		}
	}
	public void collision(){
    	for(int i = 0; i < handler.object.size(); i++){
    		GameObject tempObject = handler.object.get(i);
    		if(getBounds().intersects(tempObject.getBounds())){
	    		if(tempObject.getId() != ID.PlayerProj && tempObject.getId() != ID.Mana && tempObject.getId() != ID.Key && tempObject.getId() != ID.Player && tempObject.getId() != ID.Boss){    			
	    			hitBoundary(tempObject.getBounds());    			
	    		}
	    		if(tempObject.getId() == ID.Boss && this.getObjnum() != tempObject.getObjnum()){
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
			if(intersection.getX() == x + width || intersection.getX() == x) velX *= -1; 
    	}
    	if(intersection.getHeight() < intersection.getWidth()){
			if(intersection.getY() > y && intersection.getY() < y + height) y = (int) (intersection.getY() - height);;
			if(intersection.getY() <  y + height && intersection.getY() > y){
			 	y = (int) (intersection.getY() + height);
				velY = 2;
			}
			if(intersection.getY() == y + height){
				velY = 0; 
				canjump = true;
	    	}
			if(intersection.getY() == y){
				velY = 2; 
	    	}
		}

    }
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		g.setColor(Color.RED);
		g.drawRect(x, y, width, height);
		g.drawRect(x + 1, y - 1, width - 2, height);
		g.drawRect(x + 2, y - 2, width - 4, height);
		g.fillRect(x + width * 1 / 8, y + height / 4, width / 4, height / 4);
		g.fillRect(x + width * 5 / 8, y + height / 4, width / 4, height / 4);
		
	}
	   public void died(){
	   }
}
