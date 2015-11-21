package com.myselia.myapp.arduinosensorplant;

import com.myselia.myapp.arduinosensorplant.routines.IncomingDataStrategy;
import com.myselia.myapp.arduinosensorplant.tools.BarChart;
import com.myselia.sandbox.templates.MyseliaMasterModule;

public class Master extends MyseliaMasterModule {
	//display
	public static BarChart chart = new BarChart();
	
	//strategies
	public static IncomingDataStrategy incomingData = new IncomingDataStrategy();
	
	//data structures
	public static int average_one = 0;
	public static int average_two = 0;
	public static int average_three = 0;
	public static int average_four = 0;
	public int count = 0;

	public Master() {
		super();
		strategyMap.put("average", incomingData);
		try {
			chart.update(average_one, average_two, average_three, average_four);
			chart.setFrame();
		} catch (Exception e) {
			System.out.println("You're shit out of luck.");
			e.printStackTrace();
		}
	}
	
	public void updateLens(){
		System.out.println("updateLens()");
	}

}
