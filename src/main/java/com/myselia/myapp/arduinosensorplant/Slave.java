package com.myselia.myapp.arduinosensorplant;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.ComponentCommunicator;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.javacommon.constants.opcode.ComponentType;
import com.myselia.javacommon.topology.ComponentCertificate;
import com.myselia.javacommon.topology.MyseliaUUID;
import com.myselia.myapp.arduinosensorplant.structures.ArduinoTransmission;
import com.myselia.myapp.arduinosensorplant.tools.ArduinoSensorDriver;
import com.myselia.sandbox.constants.MyseliaModuleType;
import com.myselia.sandbox.runtime.ArgumentsInterpreter;
import com.myselia.sandbox.runtime.templates.MyseliaSlaveModule;

public class Slave extends MyseliaSlaveModule {

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

	public void eventAction() {

		if (!masterSetup) {
			ComponentCertificate cc = ComponentCommunicator.componentCertificate;
			if (cc != null) {
				System.out.println(">>>>>>>>>>>>>>>>>>CC IS NOT NULL: sending slavesetup to master");
				Message setup_mess = new Message(String.valueOf(ArgumentsInterpreter.uid), "master", "slavesetup",
						String.valueOf(ArgumentsInterpreter.uid));
				sendMessage(ComponentType.SANDBOXMASTER, null, "slavesetup", json.toJson(setup_mess));
				masterSetup = true;
			}
		}

		ArduinoTransmission at = asd.getArduinoTransmission();
		System.out.println("GOT ARDUINO TRANSMISSION IN SLAVE MODULE:");
		System.out.println("||" + json.toJson(at) + "||");
		String avg = Integer.toString(getAverageSensorValue(at.getSensors()));
		System.out.println("Averages out to : " + avg);
		Message runtime_mess = new Message(String.valueOf(ArgumentsInterpreter.uid), "master", "average", json.toJson(avg));
		sendMessage(ComponentType.SANDBOXMASTER, null, "average", json.toJson(runtime_mess));
	}

	public int getAverageSensorValue(int[] s) {
		int avg = 0;
		for (int i = 0; i < s.length; i++) {
			avg += s[i];
		}
		avg = avg / s.length;
		return avg;
	}

	@Override
	protected void handleMessage(MyseliaUUID muuid) {
		Message newmessage = messagebox.dequeueIn();
		System.out.println(json.toJson(newmessage));
	}

}