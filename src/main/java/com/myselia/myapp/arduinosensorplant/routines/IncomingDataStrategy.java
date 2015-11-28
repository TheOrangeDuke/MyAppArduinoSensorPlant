package com.myselia.myapp.arduinosensorplant.routines;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.myapp.arduinosensorplant.Master;
import com.myselia.sandbox.routines.Strategy;
import com.myselia.sandbox.templates.MyseliaModule;
import com.myselia.sandbox.templates.proxy.LensProxy;

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
		int[] tosend = new int[4];
		tosend[0] = value_one;
		tosend[1] = value_two;
		tosend[2] = value_three;
		tosend[3] = value_four;
		
		sendToLens(master, tosend);
	}
	
	private void sendToLens(Master master, int[] tosend){
		for(int i = 0; i < tosend.length; i++){
			Integer buffer = new Integer(tosend[i]);
			if(buffer != 0 && buffer != null){
				Message mess = new Message("value", json.toJson(buffer));
				LensProxy.getInstance().sendMessage(master, mess);
			}
		}
	}


}
