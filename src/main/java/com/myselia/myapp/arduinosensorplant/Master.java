package com.myselia.myapp.arduinosensorplant;

import com.mycelia.common.communication.units.TransmissionBuilder;
import com.mycelia.common.constants.opcode.ActionType;
import com.mycelia.common.constants.opcode.ComponentType;
import com.mycelia.common.constants.opcode.OpcodeAccessor;
import com.mycelia.sandbox.runtime.templates.MyceliaMasterModule;

public class Master extends MyceliaMasterModule {
	TransmissionBuilder tb = new TransmissionBuilder();

	int count = 0 ;
	
	public Master() {
		System.out.println("Master Module Initialisation");
	}
	
	@Override
	public void setup() {

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
