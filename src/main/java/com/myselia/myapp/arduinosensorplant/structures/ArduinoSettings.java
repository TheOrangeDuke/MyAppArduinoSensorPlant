package com.myselia.myapp.arduinosensorplant.structures;

public class ArduinoSettings {
	int r = 0;
	int g = 0;
	int b = 0;
	int state_machine = 0;
	int sensor_power = 0;
	
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	public int getState_machine() {
		return state_machine;
	}
	public void setState_machine(int state_machine) {
		this.state_machine = state_machine;
	}
	public int getSensor_power() {
		return sensor_power;
	}
	public void setSensor_power(int sensor_power) {
		this.sensor_power = sensor_power;
	}
}
