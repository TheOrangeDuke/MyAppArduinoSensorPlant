package com.myselia.myapp.arduinosensorplant;

import com.myselia.sandbox.runtime.templates.MyseliaApplication;

public class Application {
	
	public static void main(String[] args){
		
		MyseliaApplication<Master, Slave> app = new MyseliaApplication<Master, Slave>(Master.class, Slave.class, args);	
		
		System.out.println("Myselia Application : Arduino Sensor Plant");
		
		app.applicationName("myapp001");
		app.start();
		
	}

}
