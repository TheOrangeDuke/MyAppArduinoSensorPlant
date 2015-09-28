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
import com.myselia.sandbox.runtime.templates.MyseliaMasterModule;

public class Master extends MyseliaMasterModule {
	TransmissionBuilder tb = new TransmissionBuilder();
	
	static final int SAMPLE_WINDOW_SIZE = 1024;	
	static final String FILE = "samples/jazz.mp3";
	
	public static Gson gson = new Gson();
	public static JFrame frame = new JFrame();
	public static JLabel label_avg = new JLabel("Average : null");
	
	float[] fft = new float[SAMPLE_WINDOW_SIZE];
	float[][] freqDom;

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
		
		char[] uid = {'a', 'b', 'c', 'd', 'e'};
		slaves = allocateVirtualSlaves(5, uid);
		System.out.println("All virtual slaves are allocated. (" + slaves.length +")");
	}

	@Override
	public void setup() {
		
		try {

		}catch (Exception e) {
			System.out.println("You're shit out of luck.");
			e.printStackTrace();
		}
	}

	public void forwardTracksToSlaves(float[] frequencies){
		System.out.println(json.toJson(frequencies));
	}
	
	public void forwardDataToLens(float[][] domain){
		String domain_str = json.toJson(domain);
		
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
			} else if (uid == 'd'){
				slaves[3].assignMyseliaUUID(muuid);
			} else if (uid == 'e'){
				slaves[4].assignMyseliaUUID(muuid);
			} else{
				System.err.println("Unknown setup source : ||"+uid+"||");
			}
		} 
		
		send_message();	
	}
	
	private void send_message(){
		String to_opcode = OpcodeBroker.make(ComponentType.LENS, null, ActionType.DATA, LensOperation.TESTDATA);
		String from_opcode = OpcodeBroker.make(ComponentType.SANDBOXMASTER, null, ActionType.RUNTIME, SandboxMasterOperation.TRANSFER);
		tb.newTransmission(from_opcode, to_opcode);
		tb.addAtom("average_one", "Integer", Integer.toString(average_one));
		tb.addAtom("average_two", "Integer", Integer.toString(average_two));
		tb.addAtom("average_three", "Integer", Integer.toString(average_three));
		mailbox.enqueueOut(tb.getTransmission());
		MailService.notify(this);
	}
	
	private void check_slaves(){
		for(int i = 0; i < slaves.length; i++){
			MyseliaUUID muuid = slaves[i].getMyseliaUUID();
			if(!muuid.equals("")){
				System.out.println("Slave Exists as " + i + " is : " + muuid.toString());
			}
		}
	}

	@Override
	protected void tick() {
		// TODO Auto-generated method stub
		
	}

}
