package com.myselia.myapp.arduinosensorplant;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.mail.MailService;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.javacommon.communication.units.Transmission;
import com.myselia.javacommon.communication.units.TransmissionBuilder;
import com.myselia.javacommon.constants.opcode.ActionType;
import com.myselia.javacommon.constants.opcode.ComponentType;
import com.myselia.javacommon.constants.opcode.OpcodeBroker;
import com.myselia.javacommon.constants.opcode.operations.SandboxMasterOperation;
import com.myselia.javacommon.constants.opcode.operations.SandboxSlaveOperation;
import com.myselia.myapp.arduinosensorplant.structures.ArduinoTransmission;
import com.myselia.myapp.arduinosensorplant.structures.Sensor;
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
			ArduinoTransmission at = asd.getArduinoTransmission();
			String avg = Integer.toString(getAverageSensorValue(at.getSensors()));
			
			String from_opcode = OpcodeBroker.make(ComponentType.SANDBOXSLAVE, null, ActionType.DATA, SandboxSlaveOperation.RESULT);
			String to_opcode = OpcodeBroker.make(ComponentType.SANDBOXMASTER, null, ActionType.DATA, SandboxMasterOperation.RESULTCONTAINER);
			
			tb.newTransmission(from_opcode, to_opcode);
			Message mess = new Message("master", "average", json.toJson(avg));
			tb.addAtom("average", "Message", json.toJson(mess));
			
			Transmission trans_out = tb.getTransmission();
			System.out.println(jsonInterpreter.toJson(trans_out));

			mailbox.enqueueOut(trans_out);
			MailService.notify(this);
		}
	}
	
	public int getAverageSensorValue(Sensor[] s){
		int avg = 0;
		for(int i = 0; i < s.length; i++){
			avg += s[i].getValue();
		}
		avg = avg/s.length;
		return avg;
	}

	@Override
	protected void handleTask() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleMessage() {
		// TODO Auto-generated method stub
		
	}

}