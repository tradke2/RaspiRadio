package raspiradio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import i2c.adc.ADS1x15;
import i2c.adc.ADS1x15.Channels;

public class VolumeControl {

	private final Logger logger = Logger.getLogger(VolumeControl.class.getName());

	// number formatters
	final DecimalFormat df = new DecimalFormat("#.##");
	final DecimalFormat pdf = new DecimalFormat("###.#");

	private final int pgaValue = 4096;
	private final int sps = 250;
	private final Channels channel;
	private final List<VolumeControlListener> listeners = new ArrayList<VolumeControlListener>();
	private final ADS1x15 adc;

	/**
	 * Volume in percent.
	 */
	private double percent;

	public VolumeControl(String channelName) throws UnsupportedBusNumberException {

		channel = getPinByName(channelName);

		adc = new ADS1x15(ADS1x15.ICType.IC_ADS1115);

		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				float value = adc.readADCSingleEnded(channel, pgaValue, sps);
				handleVolumeChanged(value);
			}
		}, 0, 1000);

	}

	private Channels getPinByName(String pinName) {
		return Channels.valueOf(pinName);
	}

	private void handleVolumeChanged(double value) {

		double percent = ((value * 100) / pgaValue);

		// approximate voltage ( *scaled based on PGA setting )
		double voltage = value / 1_000f;

		long tid = Thread.currentThread().getId();
		// display output
		logger.finest(" (" + channel + ")(" + tid + ") : VOLTS=" + df.format(voltage) + "  | PERCENT="
				+ pdf.format(percent) + "% | RAW=" + value + "       ");

		setVolume(percent);
	}

	public void addListener(VolumeControlListener listener) {
		if (listener == null)
			return;
		listeners.add(listener);
	}

	public void removeListener(VolumeControlListener listener) {
		if (listener == null)
			return;
		listeners.remove(listener);
	}

	/**
	 * Returns the actually set volume in percent.
	 * 
	 * @return
	 */
	public double getVolume() {
		return percent;
	}

	private void setVolume(double volume) {
		percent = volume;
		for (VolumeControlListener listener : listeners) {
			listener.volumeChanged(this);
		}
	}

}
