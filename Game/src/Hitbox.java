import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Hitbox extends GameObject {
	Handler handler;
	int height, width, lifetime = 6;
	public Hitbox(int x, int y, int width, int height, ID id, Handler handler) {
		super(x, y, width, height, id);
		this.handler = handler;
		this.height = height;
		this.width = width;
	}

	public void tick() {
		--lifetime;
		if(lifetime == 0) handler.removeObject(this);
	}

	public void render(Graphics g) {
//		g.setColor(Color.green);
//		g.fillRect(x, y, width, height);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
		
	}

	public void died() {

		
	}

}
