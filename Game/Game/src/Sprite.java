import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	private BufferedImage spriteSheet;
	public Sprite() {
		super();
	}   
	
    public BufferedImage loadSprite(String file) {
    	//Loads our spritesheet
        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(new File(file + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sprite;
    }

    public BufferedImage getSprite(int x, int y, int width, int height, String name) {
    	//Pulls a sprite off the spritesheet
        if (spriteSheet == null) {
            spriteSheet = loadSprite(name);
        }
        return spriteSheet.getSubimage(x, y, width, height);
    }
}