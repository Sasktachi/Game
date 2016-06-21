import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Color;

public class Wall extends GameObject{
	Handler handler;
	private int width, height;
	private BufferedImage wall;
	public Wall(int x, int y, ID id, Handler handler, int width, int height){
		super(x, y, height, width, id);
		this.width = width;
		this.height = height;
		this.handler = handler;
		try{
			wall = ImageIO.read(new File("Floor128x72.png"));
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void tick() {
		x += velX + spdx;
		y += velY + spdy;
	}

	public void render(Graphics g) {
		for(int i = 0; i < width / 128; ++i){
			for(int j = 0; j < height / 72; ++j){
				g.drawImage(wall, x + i * 128, y + j * 72, null);
			}			
		}
	}
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
		
		
	}
	public void died() {

	}
}
