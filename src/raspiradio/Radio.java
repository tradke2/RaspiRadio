package raspiradio;

import java.util.logging.Logger;

import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class Radio {

	private final Logger logger = Logger.getLogger(Radio.class.getName());

	private final OnOffSwitch onOffSwitch;
	private final ScaleIllumination scaleIllumination;
	private final VolumeControl volumeControl;
	private final Player player;
	private volatile boolean running;

	private final OnOffSwitchListener onOffListener = new OnOffSwitchListener() {
		@Override
		public void stateChanged(OnOffSwitch source) {
			if (source.isOn())
				switchOn();
			else
				switchOff();
		}
	};

	private final VolumeControlListener volumeListener = new VolumeControlListener() {
		@Override
		public void volumeChanged(VolumeControl source) {
			setVolume(source.getVolume());
		}
	};

	private boolean switchedOn;

	public Radio(Configuration config) throws UnsupportedBusNumberException {
		scaleIllumination = new ScaleIllumination(config.IlluminationPin, config.IlluminationPinInvert);
		onOffSwitch = new OnOffSwitch(config.OnOffPin, config.OnOffPinInvert);
		volumeControl = new VolumeControl(config.VolumeControlChannel);
		player = new Player();
	}

	private void setVolume(double volume) {
		player.setVolume(volume);
	}

	private void switchOff() {
		logger.fine("Switching off");
		switchedOn = false;
		volumeControl.removeListener(volumeListener);
		player.stop();
		player.setVolume(0);
		scaleIllumination.setOn(false);
	}

	private void switchOn() {
		logger.fine("Switching on");
		scaleIllumination.setOn(true);
		player.setVolume(volumeControl.getVolume());
		switchedOn = true;
		volumeControl.addListener(volumeListener);
		player.start();
	}

	public void run() {
		setup();
		running = true;
		while (running)
			;
		shutdown();
	}

	public void stop() {
		running = false;
	}

	private void shutdown() {
		logger.fine("Shutting down");
		switchOff();
		onOffSwitch.removeListener(onOffListener);
	}

	private void setup() {
		logger.fine("Setting up");
		onOffSwitch.addListener(onOffListener);
		if (onOffSwitch.isOn()) {
			switchOn();
		}
	}

	public boolean isSwitchedOn() {
		return switchedOn;
	}
	
}
