package com.myselia.myapp.arduinosensorplant;

import com.myselia.sandbox.runtime.settings.ArgumentsInterpreter;
import com.myselia.sandbox.runtime.settings.MyseliaApplicationSettings;
import com.myselia.sandbox.templates.MyseliaApplication;

public class Application {
	
	private static MyseliaApplication<Master, Slave> app;
	private static MyseliaApplicationSettings settings;
	
	public static void main(String[] args){
		
		settings = ArgumentsInterpreter.interpret(args);
		app = new MyseliaApplication<Master, Slave>(Master.class, Slave.class, settings);	
		
		System.out.println("Myselia Application : Arduino Sensor Plant");

		app.start();
	}

}