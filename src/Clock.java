public class Clock {
	Handler handler;
	private int time;
	private String name;
	public Clock(int time){
		this.time = time;
		this.handler = handler;
	}
	public void setTime(int time){
		this.time = time;
	}
	public int getTime(){
		return time;
	}
	public void tick(){
		--time;
		Game.clamp(time, 0, 9999999);
	}
}
