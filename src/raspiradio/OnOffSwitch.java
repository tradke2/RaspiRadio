package raspiradio;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class OnOffSwitch {

	private final Logger logger = Logger.getLogger(OnOffSwitch.class.getName());

	private final GpioPinDigitalInput switchPin;
	private final boolean invert;
	private final List<OnOffSwitchListener> listeners = new ArrayList<OnOffSwitchListener>();

	public OnOffSwitch(String pinName) {
		this(pinName, false);
	}

	public OnOffSwitch(String pinName, boolean invert) {
		this.invert = invert;
		GpioController gpio = GpioFactory.getInstance();
		switchPin = gpio.provisionDigitalInputPin(RaspiPin.getPinByName(pinName.replace('_', ' ')),
				invert ? PinPullResistance.PULL_UP : PinPullResistance.PULL_DOWN);

		setStateFromPinState(switchPin.getState());

		switchPin.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				setStateFromPinState(event.getState());
			}
		});
	}

	private void setStateFromPinState(PinState state) {
		setOn(invert ? state.isLow() : state.isHigh());
	}

	public boolean isOn() {
		boolean isOn = invert ? switchPin.isLow() : switchPin.isHigh();
		logger.fine("Returning switch state " + (isOn ? "on" : "off"));
		return isOn;
	}

	private void setOn(boolean isOn) {
		logger.fine("Informing listeners about state change (new state is " + (isOn ? "on" : "off") + ")");
		for (OnOffSwitchListener onOffSwitchListener : listeners) {
			onOffSwitchListener.stateChanged(this);
		}
	}

	public void addListener(OnOffSwitchListener listener) {
		if (listener == null)
			return;
		listeners.add(listener);
	}
	
	public void removeListener(OnOffSwitchListener listener) {
		if (listener == null)
			return;
		listeners.remove(listener);
	}

}
