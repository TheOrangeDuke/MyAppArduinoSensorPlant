package com.myselia.myapp.arduinosensorplant.routines;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.myapp.arduinosensorplant.Master;
import com.myselia.sandbox.routines.Strategy;
import com.myselia.sandbox.templates.MyseliaModule;

public class IncomingDataStrategy implements Strategy{
	Gson json = new Gson();

	@Override
	public void execute(MyseliaModule module, Message message) {
		Master master = (Master) module;
		
		master.average_one = Integer.parseInt(json.fromJson(message.getContent(), String.class));
		
		master.count++;	
		
		action(master);
	}
	
	private void action(Master master){
		int value_one = (int) (((double) master.average_one / 1024) * 100);
		int value_two = (int) (((double) master.average_two / 1024) * 100);
		int value_three = (int) (((double) master.average_three / 1024) * 100);
		int value_four = (int) (((double) master.average_four / 1024) * 100);
		master.chart.update(master.count, value_one + 4, value_two + 4, value_three + 4, value_four + 4);
	}


}
