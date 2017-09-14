package raspiradio;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class ScaleIllumination {

	private final Logger logger = Logger.getLogger(ScaleIllumination.class.getName());
	private final boolean invert;
	private final GpioPinDigitalOutput switchPin;

	public ScaleIllumination(String pinName) {
		this(pinName, false);
	}

	public ScaleIllumination(String pinName, boolean invert) {
		this.invert = invert;
		GpioController gpio = GpioFactory.getInstance();
		switchPin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(pinName.replace('_', ' ')),
				invert ? PinState.HIGH : PinState.LOW);
		setOn(false);
	}

	public void setOn(boolean isOn) {
		logger.log(Level.FINE, "Switched illumination " + (isOn ? "on" : "off") + ".");
		switchPin.setState(invert ? !isOn : isOn);
	}

	public boolean isOn() {
		boolean isOn = invert ? switchPin.isLow() : switchPin.isHigh();
		logger.log(Level.FINE, "Returning illumination state " + (isOn ? "on" : "off") + ".");
		return isOn;
	}
}
