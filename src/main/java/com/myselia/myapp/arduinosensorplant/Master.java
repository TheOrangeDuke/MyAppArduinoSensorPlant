package com.myselia.myapp.arduinosensorplant;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.mail.MailService;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.javacommon.communication.units.TransmissionBuilder;
import com.myselia.javacommon.constants.opcode.ActionType;
import com.myselia.javacommon.constants.opcode.ComponentType;
import com.myselia.javacommon.constants.opcode.OpcodeBroker;
import com.myselia.javacommon.constants.opcode.operations.LensOperation;
import com.myselia.javacommon.constants.opcode.operations.SandboxMasterOperation;
import com.myselia.myapp.arduinosensorplant.tools.BarChart;
import com.myselia.sandbox.runtime.templates.MyseliaMasterModule;

public class Master extends MyseliaMasterModule {
	TransmissionBuilder tb = new TransmissionBuilder();

	public static JFrame frame = new JFrame();
	public static BarChart chart = new BarChart();
	public static JLabel label_avg = new JLabel("Average : null");

	public static Gson gson = new Gson();

	int connection_status = 0;
	int average_one = 0;
	int average_two = 0;
	int average_three = 0;
	int count = 0;

	public Master() {
		String check = OpcodeBroker.makeMailCheckingOpcode(
				ActionType.DATA, SandboxMasterOperation.RESULTCONTAINER);
		System.out.println(check);
		MailService.register(check, this);
	}

	@Override
	public void setup() {
		chart.update(average_one+4, average_two+4, average_three+4, 4); 
		frame.getContentPane().add(chart);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	protected void tick() {
		send_message();
			
		int value_one = (int)(((double)average_one/1024)*100);
		int value_two = (int)(((double)average_two/1024)*100);
		int value_three = (int)(((double)average_three/1024)*100);
		chart.update(value_one+4, value_two+4, value_three + 4, 4); 

	}

	@Override
	protected void handleTask() {
		//Task newtask = taskbox.dequeueIn();
	}

	@Override
	protected void handleMessage() {
		Message newmessage = messagebox.dequeueIn();
		System.out.println(json.toJson(newmessage));
		if(newmessage.getTitle().contains("average")){
			if(newmessage.getTitle().contains("0")){
				average_one  = Integer.parseInt(json.fromJson(newmessage.getContent(), String.class));
			} else if(newmessage.getTitle().contains("1")){
				average_two  = Integer.parseInt(json.fromJson(newmessage.getContent(), String.class));
			} else if(newmessage.getTitle().contains("2")){
				average_three  = Integer.parseInt(json.fromJson(newmessage.getContent(), String.class));
			} else {
				System.err.println("Unknown message source + ||" + newmessage.getTitle() + "||");
			}
		} else if(newmessage.getTitle().equals("count")){
			count++;
			frame.setTitle("Sensor plant v0.1 | Transmission Count : " + count);
		}
		
	}
	
	private void send_message(){
		String to_opcode = OpcodeBroker.make(ComponentType.LENS, null, ActionType.DATA, LensOperation.TESTDATA);
		String from_opcode = OpcodeBroker.make(ComponentType.SANDBOXMASTER, null, ActionType.RUNTIME, SandboxMasterOperation.TRANSFER);
		tb.newTransmission(from_opcode, to_opcode);
		tb.addAtom("average_one", "Integer", Integer.toString(average_one));
		tb.addAtom("average_two", "Integer", Integer.toString(average_two));
		mailbox.enqueueOut(tb.getTransmission());
		MailService.notify(this);
	}

}
