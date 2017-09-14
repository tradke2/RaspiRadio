package raspiradio;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Player {

	private final Logger logger = Logger.getLogger(Player.class.getName());
	private double volume;
	
	public Player() {
		volume = 0;
	}
	
	public void start() {
		logger.log(Level.FINE, "Start playing");
	}
	
	public void stop() {
		logger.log(Level.FINE, "Stop playing");		
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		logger.log(Level.FINE, "Setting volume to " + volume + ".");		
		this.volume = volume;
	}
}
