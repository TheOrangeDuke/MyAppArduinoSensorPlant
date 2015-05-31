package com.myselia.myapp.arduinosensorplant;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.mail.MailService;
import com.myselia.javacommon.communication.units.Transmission;
import com.myselia.javacommon.communication.units.TransmissionBuilder;
import com.myselia.javacommon.constants.opcode.ActionType;
import com.myselia.javacommon.constants.opcode.ComponentType;
import com.myselia.javacommon.constants.opcode.OpcodeAccessor;
import com.myselia.myapp.arduinosensorplant.tools.ArduinoSensorDriver;
import com.myselia.sandbox.runtime.templates.MyseliaSlaveModule;

public class Slave extends MyseliaSlaveModule {
	TransmissionBuilder tb = new TransmissionBuilder();
	Gson jsonInterpreter = new Gson();
	ArduinoSensorDriver asd = new ArduinoSensorDriver();
	int transmission_count = 0;

	public Slave() {
		System.out.println("Slave Module Initialisation");
	}

	@Override
	public void setup() {
		System.out.println("SLAVE SETUP");
		asd.initialize();
		asd.roll();
	}

	@Override
	protected void tick() {
		
		boolean check = false;
		
		if(asd.getArduinoTransmission() != null){
			if(asd.getArduinoTransmission().getTransmission() > transmission_count){
				transmission_count = asd.getArduinoTransmission().getTransmission();
				check=true;
			}
		}
		
		
		if (check) {
			tb.newTransmission(OpcodeAccessor.make(ComponentType.SANDBOXSLAVE,
					ActionType.RUNTIME, "DATA"), OpcodeAccessor.make(
					ComponentType.LENS, ActionType.RUNTIME, "DATA"));
			
			tb.addAtom("ArduinoTransmission", "ArduinoTransmission",
					jsonInterpreter.toJson(asd.getArduinoTransmission()));

			mailbox.enqueueOut(tb.getTransmission());
			MailService.notify(this);
		}
	}

}