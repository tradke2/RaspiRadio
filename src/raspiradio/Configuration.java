package raspiradio;

/**
 * Configuration for {@link Radio}.
 */
public class Configuration {

	/**
	 * Name of the output GPIO pin used to control the scale illumination (or
	 * whatever you want to control). This GPIO is toggled whenever the radio is
	 * switched on or off. (Example: GPIO_5)
	 */
	public final String IlluminationPin;

	/**
	 * True if the {@link #IlluminationPin} must be inverted. If inverted, the
	 * ON-state is low, otherwise high.
	 */
	public final boolean IlluminationPinInvert;

	/**
	 * Name of the input GPIO pin used to switch the radio on or off. (Example:
	 * GPIO_0)
	 */
	public final String OnOffPin;

	/**
	 * True if the {@link #OnOffPin} must be inverted. If inverted, the
	 * LOW-state is interpreted as ON, otherwise the HIGH-state is on. If
	 * inverted, there pin will be configured with an internal pullup resistor,
	 * otherwise a pulldown resistor will be configured.
	 */
	public final boolean OnOffPinInvert;

	/**
	 * The name of the analog input channel that will be used for volume
	 * control. Th radio uses an ADS1115 analog digital converter connected via
	 * I2C bus #1 to the Raspberry Pi. The channel names are expected in the
	 * form CHANNEL_0, ..., CHANNEL_3.
	 */
	public final String VolumeControlChannel;

	/**
	 * The default constructor. Following defaults will be used:
	 * <p>
	 * <table border=1>
	 * <tr><th>Name</th><th>Value</th></tr>
	 * <tr><td>IlluminationPin</td><td>GPIO_5</td></tr>
	 * <tr><td>IlluminationPinInvert</td><td>false</td></tr>
	 * <tr><td>OnOffPin</td><td>GPIO_0</td></tr>
	 * <tr><td>OnOffPinInvert</td><td>true</td></tr>
	 * <tr><td>VolumeControlChannel</td><td>CHANNEL_0</td></tr>
	 * </table>
	 * </p>
	 */
	public Configuration() {
		IlluminationPin = "GPIO_5";
		IlluminationPinInvert = false;
		OnOffPin = "GPIO_0";
		OnOffPinInvert = true;
		VolumeControlChannel = "CHANNEL_0";
	}

}
