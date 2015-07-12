package com.myselia.myapp.arduinosensorplant.structures;

public class ArduinoTransmission {
	private int t = 0;
	private int s = 0;
	private int l = 0;
	private int[] sensor = new int[6];
	
	public int getTransmission() {
		return t;
	}
	public void setTransmission(int t) {
		this.t = t;
	}
	public int getState_machine() {
		return s;
	}
	public void setState_machine(int t) {
		this.s = t;
	}
	public int getLed_intensity() {
		return l;
	}
	public void setLed_intensity(int l) {
		this.l = l;
	}
	public int[] getSensors() {
		return sensor;
	}
}
