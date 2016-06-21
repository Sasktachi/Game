import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HUD {
	public static int HEALTH = 100;
	private Handler handler;
	private BufferedImage hp, mana, hpmax, hpcur, counter;
	public HUD(Handler handler){
		this.handler = handler;
		try{
			hpmax = ImageIO.read(new File("MaxHP.png"));
			hpcur = ImageIO.read(new File("CurHP.png"));
		} catch (IOException e){	
			e.printStackTrace();
		}
	}
	public void tick(){
		//Our health can't be less than 0 or more than 100, and if it becomes 0 the game ends
		HEALTH = Game.clamp(HEALTH, 0, 100);
		if(HEALTH == 0) System.exit(1);
	}
	
	public void render(Graphics g){
		//Draws our hp and mana bars
		if(HUD.HEALTH > 0 && HUD.HEALTH <= 100) hp = hpcur.getSubimage(0, 0, HUD.HEALTH * 256 / 100, 32);
        if(Player.mana > 0 && Player.mana <= 100)mana = hpcur.getSubimage(0, 32, Player.mana * 256 / 100, 32);
		g.drawImage(hpmax, 32, 32, null);
		g.drawImage(hpmax, 32, 64, null);
		g.drawImage(hp, 32, 32, null);
		g.drawImage(mana, 32, 64, null);
	}
}
