package com.myselia.myapp.arduinosensorplant.routines;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.myapp.arduinosensorplant.Master;
import com.myselia.sandbox.runtime.routines.Strategy;
import com.myselia.sandbox.runtime.templates.MyseliaModule;

public class IncomingDataStrategy implements Strategy{
	Gson json = new Gson();

	@Override
	public void execute(Message message, MyseliaModule module) {
		Master master = (Master) module;
		
		if (master.slaves[0].getMyseliaUUID().toString().equals(message.getSource().toString())) {
			System.out.println("Modifying Slave a");
			Master.average_one = Integer.parseInt(json.fromJson(message.getContent(), String.class));
		} else if (master.slaves[1].getMyseliaUUID().toString().equals(message.getSource().toString())) {
			System.out.println("Modifying Slave b");
			Master.average_two = Integer.parseInt(json.fromJson(message.getContent(), String.class));
		} else if (master.slaves[2].getMyseliaUUID().toString().equals(message.getSource().toString())) {
			System.out.println("Modifying Slave c");
			Master.average_three = Integer.parseInt(json.fromJson(message.getContent(), String.class));
		} else if (master.slaves[3].getMyseliaUUID().toString().equals(message.getSource().toString())) {
			System.out.println("Modifying Slave c");
			Master.average_four = Integer.parseInt(json.fromJson(message.getContent(), String.class));
		} else {
			System.err.println("Unknown message source : ||" + message.getSource().toString() + "||");
		}
		master.count++;	
		master.updateLens();
		System.out.println("");
	}


}
