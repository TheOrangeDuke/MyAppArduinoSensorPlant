package com.myselia.myapp.arduinosensorplant;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.mail.MailService;
import com.myselia.javacommon.communication.units.Transmission;
import com.myselia.javacommon.communication.units.TransmissionBuilder;
import com.myselia.javacommon.constants.opcode.ActionType;
import com.myselia.javacommon.constants.opcode.OpcodeBroker;
import com.myselia.javacommon.constants.opcode.operations.SandboxMasterOperation;
import com.myselia.sandbox.runtime.templates.MyseliaMasterModule;

public class Master extends MyseliaMasterModule {
	TransmissionBuilder tb = new TransmissionBuilder();

	public static JFrame frame = new JFrame("Arduino Sensor Farm");
	public static JLabel label_one = new JLabel("Connection Status : null");
	public static JLabel label_two = new JLabel("Average : null");

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
		frame.getContentPane().add(label_one, BorderLayout.NORTH);
		frame.getContentPane().add(label_two, BorderLayout.SOUTH);
		frame.pack();
		frame.setSize(400, 70);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	protected void tick() {

		int value = 0;

		if (mailbox.getInSize() > 0) {
			Transmission trans = mailbox.dequeueIn();
			System.out.println(gson.toJson(trans));
			value = Integer.parseInt(trans.get_atoms().get(0).get_value());

			label_one.setText("Connection Status : " + connection_status++);
			label_two.setText("Average : " + value);
		}

		try {
			Thread.sleep(100);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
