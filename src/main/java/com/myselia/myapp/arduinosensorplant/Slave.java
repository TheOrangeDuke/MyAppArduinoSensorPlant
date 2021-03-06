package com.myselia.myapp.arduinosensorplant;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.ComponentCommunicator;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.javacommon.constants.opcode.ComponentType;
import com.myselia.javacommon.topology.ComponentCertificate;
import com.myselia.myapp.arduinosensorplant.structures.ArduinoTransmission;
import com.myselia.myapp.arduinosensorplant.tools.ArduinoSensorDriver;
import com.myselia.sandbox.runtime.settings.ArgumentsInterpreter;
import com.myselia.sandbox.templates.MyseliaSlaveModule;
import com.myselia.sandbox.templates.proxy.MasterModuleProxy;

public class Slave extends MyseliaSlaveModule {

	Gson jsonInterpreter = new Gson();
	ArduinoSensorDriver asd = new ArduinoSensorDriver(this);
	int transmission_count = 0;

	public Slave() {
		super();
		asd.initialize();
		asd.roll();
	}

	public void eventAction() {
		//System.out.println("Slave : eventAction()");
		ArduinoTransmission at = asd.getArduinoTransmission();
		String avg = Integer.toString(getAverageSensorValue(at.getSensors()));
		Message runtime_mess = new Message("average", json.toJson(avg));
		MasterModuleProxy.getInstance().sendMessage(this, runtime_mess);
	}

	public int getAverageSensorValue(int[] s) {
		int avg = 0;
		for (int i = 0; i < s.length; i++) {
			avg += s[i];
		}
		avg = avg / s.length;
		return avg;
	}

}