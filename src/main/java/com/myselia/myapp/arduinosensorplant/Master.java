package com.myselia.myapp.arduinosensorplant;

import com.myselia.myapp.arduinosensorplant.routines.IncomingDataStrategy;
import com.myselia.myapp.arduinosensorplant.routines.LensUpdateStrategy;
import com.myselia.myapp.arduinosensorplant.tools.BarChart;
import com.myselia.sandbox.runtime.templates.MyseliaMasterModule;

public class Master extends MyseliaMasterModule {
	//display
	public static BarChart chart = new BarChart();
	
	//strategies
	public static LensUpdateStrategy lensUpdate = new LensUpdateStrategy();
	public static IncomingDataStrategy incomingData = new IncomingDataStrategy();
	
	//data structures
	public static int average_one = 0;
	public static int average_two = 0;
	public static int average_three = 0;
	public static int average_four = 0;
	public int count = 0;

	public Master() {
		super();
		char[] uid = { 'a', 'b', 'c', 'd' };
		allocateVirtualSlaves(4, uid);
		strategyMap.put("average", incomingData);
	}

	@Override
	public void setup() {
		try {
			chart.update(average_one, average_two, average_three, average_four);
			chart.setFrame();
		} catch (Exception e) {
			System.out.println("You're shit out of luck.");
			e.printStackTrace();
		}
	}

	@Override
	protected void tick() {
		int value_one = (int) (((double) average_one / 1024) * 100);
		int value_two = (int) (((double) average_two / 1024) * 100);
		int value_three = (int) (((double) average_three / 1024) * 100);
		int value_four = (int) (((double) average_four / 1024) * 100);
		chart.update(value_one + 4, value_two + 4, value_three + 4, value_four + 4);
	}
	
	public void updateLens(){
		lensUpdate.execute(null, this);
	}
}
