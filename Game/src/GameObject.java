import java.awt.Graphics;
import java.awt.Rectangle;
public abstract class GameObject {
    //An abstract class with getters and setters for universal object attributes and a died() method. All game objects extend this.
    protected int x, y, spdx, spdy, width, height, objnum = 0;
	protected ID id;
    protected int velX, velY;
    protected int grav = 1;
    protected int HEALTH;
    protected int center = x + (width / 2);
    public GameObject(int x,int y, int width, int height, ID id){
        this.x = x;
        this.y = y;
        this.id = id;
        this.width = width;
        this.height = height;
    }
    
    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();
    
    public void scrollX(int s){
    	spdx = s;
    }
    public void scrollY(int s){
    	spdy = s;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    public void setId(ID id) {
        this.id = id;
    }
    
    public ID getId() {
        return id;
    }
    
    public void setVelX(int velX) {
        this.velX = velX;
    }
    
    public void setVelY(int velY) {
        this.velY = velY;
    }
    
    public int getVelX() {
        return velX;
    }
    
    public int getVelY() {
        return velY;
    }
    public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setHEALTH(int hp){
		HEALTH = hp;
	}
	public int getHEALTH(){
		return HEALTH;
	}
	public int getObjnum() {
		return objnum;
	}

	public void setObjnum(int objnum) {
		this.objnum = objnum;
	}

	public abstract void died();
}