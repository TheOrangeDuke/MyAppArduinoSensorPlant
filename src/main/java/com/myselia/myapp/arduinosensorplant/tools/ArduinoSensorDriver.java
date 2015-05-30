package com.myselia.myapp.arduinosensorplant.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;

import com.google.gson.Gson;
import com.myselia.myapp.arduinosensorplant.structures.ArduinoTransmission;

public class ArduinoSensorDriver implements SerialPortEventListener {
	
	private Gson jsonInterpreter = new Gson();
	private ArduinoTransmission at;

	/** The port we're normally going to use. */
	private static SerialPort serialPort;
	
	private static final boolean rpi = true;
	
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1",
		"/dev/ttyACM0", // Raspberry Pi
		"/dev/ttyACM1", // Raspberry Pi
		"/dev/ttyACM2", // Raspberry Pi
		"/dev/ttyUSB0", // Linux
		"/dev/ttyUSB1", // Linux
		"/dev/ttyUSB2", // Linux
		"/dev/ttyUSB3", // Linux
		"/dev/ttyUSB4", // Linux
		"/dev/ttyUSB5", // Linux
		"COM3", // Windows
		"COM4", // Windows
		"COM5", // Windows
		"COM6", // Windows
		"COM7", // Windows
		"COM8", // Windows
		"COM9", // Windows
		"/dev/ttyS80" //symbolic
	};

	private static BufferedReader input;
	private static OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	public void initialize() {
		System.out.println("INITIALIZING...");
		
		if(rpi){
			System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
		}
		
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		
		try {
			portId = CommPortIdentifier.getPortIdentifier(PORT_NAMES[1]);
		} catch (NoSuchPortException e) {
			System.err.println("No such port exception");
		}
		
		
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			
			//System.out.println("CHECKING ENUMERATION ELEMENTS : " + currPortId.getName());
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				} else {
					//System.out.println("Checking port : " + portName);
				}
			}
		}
		
		if (portId == null) {
			System.err.println("Could not find serial port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public static synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			String inputLine = "";
			try {
				inputLine = input.readLine();
				setArduinoTransmission(jsonInterpreter.fromJson(inputLine, ArduinoTransmission.class));
				
				System.out.println("New Arduino Transmission : length=" +inputLine.length() + " : transmission_nb=" + at.getTransmission());
			} catch (Exception e) {
				System.err.println("Error interpreting Arduino transmission.");
				System.err.println("||" + inputLine + "||");
			}
		}
	}

	public void roll() { 
		Thread t = new Thread() {
			public void run() {
				
				int n = 5 * 60; //alive minutes ( append '*60' for alive hours 
				try {
					Thread.sleep(n * 60 * 1000);
				} catch (InterruptedException ie) {
				}
				close();
			}
		};
		t.start();

		System.out.println("Driver Started");
	}

	public ArduinoTransmission getArduinoTransmission() {
		return at;
	}
	
	private void setArduinoTransmission(ArduinoTransmission at){
		this.at = at;
	}

}
