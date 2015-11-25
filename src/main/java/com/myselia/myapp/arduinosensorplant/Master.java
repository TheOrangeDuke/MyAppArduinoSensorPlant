package com.myselia.myapp.arduinosensorplant;

import com.myselia.myapp.arduinosensorplant.routines.IncomingDataStrategy;
import com.myselia.myapp.arduinosensorplant.tools.BarChart;
import com.myselia.sandbox.templates.MyseliaMasterModule;

public class Master extends MyseliaMasterModule {
	//display
	public BarChart chart = new BarChart();
	
	//strategies
	public IncomingDataStrategy incomingData = new IncomingDataStrategy();
	
	//data structures
	public int average_one = 0;
	public int average_two = 0;
	public int average_three = 0;
	public int average_four = 0;
	
	public int count = 0;

	public Master() {
		super();
		strategyMap.put("average", incomingData);
		try {
			chart.update(0, average_one, average_two, average_three, average_four);
			chart.setFrame();
		} catch (Exception e) {
			System.out.println("Master : error setting up the chart and frame");
		}
	}
	
	public void updateLens(){
		System.out.println("updateLens()");
	}

}
