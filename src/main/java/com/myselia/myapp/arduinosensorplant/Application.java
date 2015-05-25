package com.myselia.myapp.arduinosensorplant;

import com.mycelia.sandbox.runtime.templates.MyceliaApplication;

public class Application {
	
	public static void main(String[] args){
		
		MyceliaApplication<Master, Slave> app = new MyceliaApplication<Master, Slave>(Master.class, Slave.class, args);	
		
		System.out.println("Myselia Application : Arduino Sensor Plant");
		
		app.start();
		
	}

}
