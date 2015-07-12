package com.myselia.myapp.arduinosensorplant;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.mail.MailService;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.javacommon.communication.units.Task;
import com.myselia.javacommon.communication.units.Transmission;
import com.myselia.javacommon.communication.units.TransmissionBuilder;
import com.myselia.javacommon.constants.opcode.ActionType;
import com.myselia.javacommon.constants.opcode.ComponentType;
import com.myselia.javacommon.constants.opcode.OpcodeBroker;
import com.myselia.javacommon.constants.opcode.operations.LensOperation;
import com.myselia.javacommon.constants.opcode.operations.SandboxMasterOperation;
import com.myselia.javacommon.constants.opcode.operations.SandboxSlaveOperation;
import com.myselia.sandbox.runtime.templates.MyseliaMasterModule;

public class Master extends MyseliaMasterModule {
	TransmissionBuilder tb = new TransmissionBuilder();

	public static JFrame frame = new JFrame("Arduino Sensor Farm");
	public static JLabel label_avg = new JLabel("Average : null");
	public static JLabel label_cnt = new JLabel("Count : null");

	public static Gson gson = new Gson();

	int connection_status = 0;
	int average = 0;
	int count = 0;

	public Master() {
		String check = OpcodeBroker.makeMailCheckingOpcode(
				ActionType.DATA, SandboxMasterOperation.RESULTCONTAINER);
		System.out.println(check);
		MailService.register(check, this);
	}

	@Override
	public void setup() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(label_avg, BorderLayout.WEST);
		frame.getContentPane().add(label_cnt, BorderLayout.EAST);
		frame.pack();
		frame.setSize(400, 100);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	protected void tick() {
		try {
			Thread.sleep(500);
			//send_message();

		} catch (InterruptedException e) {
			e.printStackTrace(); 
		}
	}

	@Override
	protected void handleTask() {
		//Task newtask = taskbox.dequeueIn();
	}

	@Override
	protected void handleMessage() {
		Message newmessage = messagebox.dequeueIn();
		System.out.println(json.toJson(newmessage));
		if(newmessage.getTitle().equals("average")){
			average  = Integer.parseInt(json.fromJson(newmessage.getContent(), String.class));
			label_avg.setText("Average : " + average);
		} else if(newmessage.getTitle().equals("count")){
			count = Integer.parseInt(json.fromJson(newmessage.getContent(), String.class));
			label_cnt.setText("Count : " + count);
		}
		
	}
	
	private void send_message(){
		String to_opcode = OpcodeBroker.make(ComponentType.LENS, null, ActionType.DATA, LensOperation.TESTDATA);
		String from_opcode = OpcodeBroker.make(ComponentType.SANDBOXMASTER, null, ActionType.RUNTIME, SandboxMasterOperation.TRANSFER);
		tb.newTransmission(from_opcode, to_opcode);
		tb.addAtom("average", "Integer", Integer.toString(average));
		tb.addAtom("count", "Integer", Integer.toString(connection_status));
		mailbox.enqueueOut(tb.getTransmission());
		MailService.notify(this);
	}

}
