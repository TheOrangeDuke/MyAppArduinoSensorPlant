package com.myselia.myapp.arduinosensorplant.structures;

public class Sensor {
	String sensor = "";
	int value = 0;
	int state = 0;
	
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
