package com.myselia.myapp.arduinosensorplant.routines;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.javacommon.communication.units.Task;
import com.myselia.javacommon.topology.MyseliaUUID;
import com.myselia.myapp.arduinosensorplant.Master;
import com.myselia.sandbox.runtime.routines.Strategy;
import com.myselia.sandbox.runtime.templates.MyseliaModule;

public class IncomingDataStrategy implements Strategy{
	Gson json = new Gson();

	@Override
	public void execute(Message m) {}

	@Override
	public void execute(Message m, MyseliaModule mm) {}

	@Override
	public void execute(Task t) {}

	@Override
	public void execute(Task t, MyseliaModule m) {}

	@Override
	public void execute(Message m, MyseliaModule mm, MyseliaUUID source) {
		Master master = (Master) mm;
		
		if (master.slaves[0].getMyseliaUUID().toString().equals(source.toString())) {
			System.out.println("Modifying Slave a");
			Master.average_one = Integer.parseInt(json.fromJson(m.getContent(), String.class));
		} else if (master.slaves[1].getMyseliaUUID().toString().equals(source.toString())) {
			System.out.println("Modifying Slave b");
			Master.average_two = Integer.parseInt(json.fromJson(m.getContent(), String.class));
		} else if (master.slaves[2].getMyseliaUUID().toString().equals(source.toString())) {
			System.out.println("Modifying Slave c");
			Master.average_three = Integer.parseInt(json.fromJson(m.getContent(), String.class));
		} else if (master.slaves[3].getMyseliaUUID().toString().equals(source.toString())) {
			System.out.println("Modifying Slave c");
			Master.average_four = Integer.parseInt(json.fromJson(m.getContent(), String.class));
		} else {
			System.err.println("Unknown message source : ||" + source + "||");
		}
		master.count++;		
	}


}
