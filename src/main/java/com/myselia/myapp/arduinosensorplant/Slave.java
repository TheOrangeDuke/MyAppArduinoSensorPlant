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
	ArduinoSensorDriver asd = new ArduinoSensorDriver(this);
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

	}
	
	public void eventAction(){
		ArduinoTransmission at = asd.getArduinoTransmission();
		
		String from_opcode = OpcodeBroker.make(ComponentType.SANDBOXSLAVE, null, ActionType.DATA, SandboxSlaveOperation.RESULT);
		String to_opcode = OpcodeBroker.make(ComponentType.SANDBOXMASTER, null, ActionType.DATA, SandboxMasterOperation.RESULTCONTAINER);
		
		tb.newTransmission(from_opcode, to_opcode);
		
		//SENDING THE AVERAGE
		String avg = Integer.toString(getAverageSensorValue(at.getSensors()));
		Message mess_one = new Message("master", "average_" + slaveID, json.toJson(avg));
		tb.addAtom("average", "Message", json.toJson(mess_one));
		
		//SENDING THE COUNT
		String cnt = Integer.toString(at.getTransmission());
		Message mess_two = new Message("master", "count", json.toJson(cnt));
		tb.addAtom("count", "Message", json.toJson(mess_two));
		
		Transmission trans_out = tb.getTransmission();

		mailbox.enqueueOut(trans_out);
		MailService.notify(this);
	}
	
	public int getAverageSensorValue(int[] s){
		int avg = 0;
		for(int i = 0; i < s.length; i++){
			avg += s[i];
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
		Message newmessage = messagebox.dequeueIn();
		System.out.println(json.toJson(newmessage));	
	}

}