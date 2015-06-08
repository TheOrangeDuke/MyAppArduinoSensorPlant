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
import com.myselia.javacommon.constants.opcode.OpcodeBroker;
import com.myselia.javacommon.constants.opcode.operations.SandboxMasterOperation;
import com.myselia.sandbox.runtime.templates.MyseliaMasterModule;

public class Master extends MyseliaMasterModule {
	TransmissionBuilder tb = new TransmissionBuilder();

	public static JFrame frame = new JFrame("Arduino Sensor Farm");
	public static JLabel avg = new JLabel("Average : null");

	public static Gson gson = new Gson();

	int connection_status = 0;
	int average = 0;

	public Master() {
		MailService.register(OpcodeBroker.makeMailCheckingOpcode(
				ActionType.RUNTIME, SandboxMasterOperation.RESULTCONTAINER),
				this);
	}

	@Override
	public void setup() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(avg, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(400, 70);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	protected void tick() {
		try {
			Thread.sleep(1000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void handleTask() {
		Task newtask = taskbox.dequeueIn();
	}

	@Override
	protected void handleMessage() {
		Message newmessage = messagebox.dequeueIn();
		System.out.println(json.toJson(newmessage));
		if(newmessage.getTitle().equals("average")){
			//System.out.println("MASTER HANDLE MESSAGE");
			int value = Integer.parseInt(json.fromJson(newmessage.getContent(), String.class));
			avg.setText("Average : " + value);
		}
	}

}
