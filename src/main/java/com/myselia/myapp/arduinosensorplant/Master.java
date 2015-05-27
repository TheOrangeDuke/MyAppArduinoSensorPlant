package com.myselia.myapp.arduinosensorplant;

import com.myselia.javacommon.communication.units.TransmissionBuilder;
import com.myselia.sandbox.runtime.templates.MyseliaMasterModule;

public class Master extends MyseliaMasterModule {
	TransmissionBuilder tb = new TransmissionBuilder();

	int count = 0 ;
	
	public Master() {
		System.out.println("Master Module Initialisation");
	}
	
	@Override
	public void setup() {
		System.out.println("MASTER SETUP");
	}

	protected void tick() {
		System.out.println("master - tick");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
