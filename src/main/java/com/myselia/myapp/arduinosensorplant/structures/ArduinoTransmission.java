package com.myselia.myapp.arduinosensorplant.structures;

public class ArduinoTransmission {
	private int transmission = 0;
	private int state_machine = 0;
	private int led_intensity = 0;
	private int led_oscillation_direction = 0;
	private Sensor[] sensors = new Sensor[6];
	
	public int getTransmission() {
		return transmission;
	}
	public void setTransmission(int transmission) {
		this.transmission = transmission;
	}
	public int getState_machine() {
		return state_machine;
	}
	public void setState_machine(int state_machine) {
		this.state_machine = state_machine;
	}
	public int getLed_intensity() {
		return led_intensity;
	}
	public void setLed_intensity(int led_intensity) {
		this.led_intensity = led_intensity;
	}
	public int getLed_oscillation_direction() {
		return led_oscillation_direction;
	}
	public void setLed_oscillation_direction(int led_oscillation_direction) {
		this.led_oscillation_direction = led_oscillation_direction;
	}
	public Sensor[] getSensors() {
		return sensors;
	}
}
