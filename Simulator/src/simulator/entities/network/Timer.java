package simulator.entities.network;

public class Timer {

	public int id;
	public short type;
	public boolean isActive;

	public Timer(int id, short type) {
		this.id = id;
		this.type = type;
		isActive = true;
	}

}