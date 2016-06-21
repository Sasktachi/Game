import java.awt.Color;
import java.awt.Graphics;
//Its just an invisible wall with a different ID so that it only affects enemies.
public class EnemyWall extends Wall {

	public EnemyWall(int x, int y, ID id, Handler handler, int width, int height) {
		super(x, y, id, handler, width, height);
		
	}
	public void render(Graphics g) {
		//g.setColor(Color.GREEN);
		//g.fillRect(x, y, width, height);
		
	}
}
