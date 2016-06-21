import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Handler {
    LinkedList<GameObject> object = new LinkedList<GameObject>();
    LinkedList<Clock> clock = new LinkedList<Clock>();
    public void tick() {
    	//Cycles through all game objects/clocks in their respective linked lists and calls their tick method.
        for(int i = 0; i < object.size(); i++){
            GameObject tempObject = object.get(i);
            tempObject.tick();
        }
        for(int i = 0; i < clock.size(); i++){
        	Clock tempClock = clock.get(i);
        	tempClock.tick();
        }
    }    
    public void render(Graphics g){  
    	//Cycles through all game objects and calls their render method
        for(int i = 0; i < object.size(); i++){
            GameObject tempObject = object.get(i);
            tempObject.render(g);            
        }
    }
    
    public void addObject(GameObject newobject){
    	//If there are already objects, adds the object passed to Handler with an 
    	//object number 1 higher than the object number of the next newest object.
    	//If the list is empty, adds the object passed to it with an object number of 0.
    	if(object.size() > 0){
	        GameObject tempObject = object.get(object.size() - 1); 
	        newobject.setObjnum(tempObject.getObjnum() + 1);
	        this.object.add(newobject);
    	}
    	else {
    		newobject.setObjnum(0);
    		this.object.add(newobject);
    	}
    }
    public void addClock(Clock clock){
        this.clock.add(clock);
    }
    
    public void removeObject(GameObject object){
        this.object.remove(object);
    }
}