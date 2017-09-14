package raspiradio;

import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class Main {

	public static void main(String[] args) throws UnsupportedBusNumberException {

		Radio radio = new Radio(new Configuration());
		radio.run();
	}

}
