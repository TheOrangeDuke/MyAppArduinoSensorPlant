package com.myselia.myapp.arduinosensorplant;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.ComponentCommunicator;
import com.myselia.javacommon.communication.mail.MailService;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.javacommon.communication.units.Transmission;
import com.myselia.javacommon.communication.units.TransmissionBuilder;
import com.myselia.javacommon.constants.opcode.ActionType;
import com.myselia.javacommon.constants.opcode.ComponentType;
import com.myselia.javacommon.constants.opcode.OpcodeBroker;
import com.myselia.javacommon.constants.opcode.operations.SandboxMasterOperation;
import com.myselia.javacommon.constants.opcode.operations.SandboxSlaveOperation;
import com.myselia.javacommon.topology.ComponentCertificate;
import com.myselia.javacommon.topology.MyseliaUUID;
import com.myselia.myapp.arduinosensorplant.structures.ArduinoTransmission;
import com.myselia.myapp.arduinosensorplant.structures.Sensor;
import com.myselia.myapp.arduinosensorplant.tools.ArduinoSensorDriver;
import com.myselia.sandbox.runtime.ArgumentsInterpreter;
import com.myselia.sandbox.runtime.templates.MyseliaSlaveModule;

public class Slave extends MyseliaSlaveModule {
	TransmissionBuilder tb = new TransmissionBuilder();
	Gson jsonInterpreter = new Gson();
	ArduinoSensorDriver asd = new ArduinoSensorDriver(this);
	int transmission_count = 0;
	
	boolean masterSetup = false;

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
		
		if(!masterSetup){
			ComponentCertificate cc = ComponentCommunicator.componentCertificate;
			if(cc != null){
				System.out.println("not null fuckers!");
				//setup with master: o.k., if you've reached this point in the code
				//please take two minutes to cry and punch a wall *before* reading it
				String from_opcode = OpcodeBroker.make(ComponentType.SANDBOXSLAVE, cc.getUUID(), ActionType.DATA, SandboxSlaveOperation.RESULT);
				String to_opcode = OpcodeBroker.make(ComponentType.SANDBOXMASTER, null, ActionType.DATA, SandboxMasterOperation.RESULTCONTAINER);
				tb.newTransmission(from_opcode, to_opcode);
				Message setup_mess = new Message("master", "slavesetup", String.valueOf(ArgumentsInterpreter.uid));
				tb.addAtom("average", "Message", json.toJson(setup_mess));
				Transmission trans_out = tb.getTransmission();
				mailbox.enqueueOut(trans_out);
				MailService.notify(this);
				masterSetup = true;
			}
		}
		
		ArduinoTransmission at = asd.getArduinoTransmission();
		
		ComponentCertificate cc = null;
		MyseliaUUID muuid = null;
		
		try{
			cc = ComponentCommunicator.componentCertificate;
			muuid = cc.getUUID();
		}catch (Exception e){
			return;
		}
		
		String from_opcode = OpcodeBroker.make(ComponentType.SANDBOXSLAVE, muuid, ActionType.DATA, SandboxSlaveOperation.RESULT);
		String to_opcode = OpcodeBroker.make(ComponentType.SANDBOXMASTER, null, ActionType.DATA, SandboxMasterOperation.RESULTCONTAINER);
		
		tb.newTransmission(from_opcode, to_opcode);
		
		//SENDING THE AVERAGE
		String avg = Integer.toString(getAverageSensorValue(at.getSensors()));
		Message mess_one = new Message("master", "average", json.toJson(avg));
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
	protected void handleTask(MyseliaUUID muuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleMessage(MyseliaUUID muuid) {
		Message newmessage = messagebox.dequeueIn();
		System.out.println(json.toJson(newmessage));	
	}

}