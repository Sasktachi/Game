
public class OrderOfCalls {
	//Main method in Game constructs a Game object, which constructs Window, which calls Game.start().
	//Here an instance of Handler, HUD, and KeyInput are created, and all game objects are spawned.
	//Due to something having to do with threads, Game.start() causes Game.run() to be called. 
	//This causes Game.tick() and Game.render() to be called every tick, which calls HUD.render(), 
	//HUD.tick(), Handler.render(), Handler.tick, and KeyInput.tick() as well.
	//From here its just game objects running their own code each tick.
}
