import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Animation {

    private int frameCount;                 // Counts ticks for change
    private int frameDelay;                 // frame delay 1-12 (You will have to play around with this)
    private int currentFrame;               // animations current frame
    private int animationDirection;         // animation direction (i.e counting forward or backward)
    private int totalFrames;                // total amount of frames for your animation

    public boolean stopped, loop, done;                // has animations stopped

    private List<Frame> frames = new ArrayList<Frame>();    // Arraylist of frames 

    public Animation(BufferedImage[] frames, int frameDelay, boolean loop) {
    	//We pass this an array of buffered images created with our Sprite class, we set a frame delay and tell it whether the animation
    	//loops or not
        this.frameDelay = frameDelay;
        this.stopped = true;
        this.loop = loop;
        this.done = false;
        //Internalizes the frames for the animation by putting our array of BufferedImages through the Frame class
        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i], frameDelay);
        }

        this.frameCount = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.animationDirection = 1;
        this.totalFrames = this.frames.size();

    }


	public void start() {
        if (!stopped) {
            return;
        }

        if (frames.size() == 0) {
            return;
        }

        stopped = false;
        done = false;
    }

    public void stop() {
        if (frames.size() == 0) {
            return;
        }

        stopped = true;
    }

    public void restart() {
        if (frames.size() == 0) {
            return;
        }

        stopped = false;
        currentFrame = 0;
        done = false;
    }

    public void reset() {
        this.stopped = true;
        this.frameCount = 0;
        this.currentFrame = 0;
        this.done = false;
    }

    private void addFrame(BufferedImage frame, int duration) {
        if (duration <= 0) {
            System.err.println("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }

        frames.add(new Frame(frame, duration));
        currentFrame = 0;
    }

    public BufferedImage getSprite() {
    	//Pulls the current frame
        return frames.get(currentFrame).getFrame();
    }

    public void update() {
        if (!stopped) {
            frameCount++;
        //If the animation is running, count up to the frame delay then go to the next frame, or previous frame if running backward
            if (frameCount > frameDelay) {
                frameCount = 0;
                currentFrame += animationDirection;
                //If we are looping the animation then go back to the beginning when the animation ends
                if(loop == true){
	                if (currentFrame > totalFrames - 1) {
	                    currentFrame = 0;
	                }
	                else if (currentFrame < 0) {
	                    currentFrame = totalFrames - 1;
	                }
                }
                //If we aren't looping the animation stop when we get to the last frame
                else{ 
                	if(currentFrame > totalFrames - 1){
                	currentFrame = totalFrames - 1;
                	stop();
                	done = true;
                	}
                	if(currentFrame < 0) {
                		currentFrame = 0;
                		stop();
                		done = true;
                	}
                }
            }
        }

    }
    //Decide whether we play this backward or forward.
    public void setAnimationDirection(int animationDirection) {
		this.animationDirection = animationDirection;
	}

}