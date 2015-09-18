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
import com.myselia.javacommon.topology.MyseliaUUID;
import com.myselia.myapp.arduinosensorplant.tools.BarChart;
import com.myselia.sandbox.runtime.network.VirtualSlave;
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
		
		char[] uid = {'a', 'b', 'c'};
		slaves = allocateVirtualSlaves(3, uid);
		System.out.println("All virtual slaves are allocated;");
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
		int value_one = (int)(((double)average_one/1024)*100);
		int value_two = (int)(((double)average_two/1024)*100);
		int value_three = (int)(((double)average_three/1024)*100);
		chart.update(value_one+4, value_two+4, value_three + 4, 4); 
	}

	@Override
	protected void handleTask(MyseliaUUID muuid) {
		//Task newtask = taskbox.dequeueIn();
	}

	@Override
	protected void handleMessage(MyseliaUUID muuid) {
		Message newmessage = messagebox.dequeueIn();
		System.out.println(json.toJson(newmessage));
		check_slaves();
		if(newmessage.getTitle().contains("slavesetup")){
			char uid = json.fromJson(newmessage.getContent(), String.class).charAt(0);
			if(uid == 'a'){
				slaves[0].assignMyseliaUUID(muuid);
			} else if (uid == 'b'){
				slaves[1].assignMyseliaUUID(muuid);
			} else if (uid == 'c'){
				slaves[2].assignMyseliaUUID(muuid);
			} else {
				System.err.println("Unknown setup source : ||"+uid+"||");
			}
		} else if(newmessage.getTitle().contains("average")){
			//if you ever end up here again, it's because of a null pointer exception, eh?
			//that null pointer exception happens when the Remote Slave 'forgets' to send
			//its setup message to the master. this causes a null pointer on your virtual slave
			//quick fix : Start Stem, Start Master, Start Daemon. Wait. Wait a while. Start Slave.
			if(slaves[0].getMyseliaUUID().toString().equals(muuid.toString())){
				System.out.println("Modifying Slave 0");
				average_one  = Integer.parseInt(json.fromJson(newmessage.getContent(), String.class));
			} else if(slaves[1].getMyseliaUUID().toString().equals(muuid.toString())){
				System.out.println("Modifying Slave 1");
				average_two  = Integer.parseInt(json.fromJson(newmessage.getContent(), String.class));
			} else if(slaves[2].getMyseliaUUID().toString().equals(muuid.toString())){
				System.out.println("Modifying Slave 2");
				average_three  = Integer.parseInt(json.fromJson(newmessage.getContent(), String.class));
			} else {
				System.err.println("Unknown message source : ||" + muuid + "||");
			}
			count++;
			frame.setTitle("Sensor plant v0.1 | Transmission Count : " + count);
			send_message();
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
	
	private void check_slaves(){
		for(int i = 0; i < slaves.length; i++){
			MyseliaUUID muuid = slaves[i].getMyseliaUUID();
			if(muuid != null){
				System.out.println("Slave Exists as " + i + " is : " + muuid.toString());
			}
		}
	}

}
