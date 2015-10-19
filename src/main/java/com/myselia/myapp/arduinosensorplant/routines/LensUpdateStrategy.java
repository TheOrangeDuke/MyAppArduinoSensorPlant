package com.myselia.myapp.arduinosensorplant.routines;

import com.google.gson.Gson;
import com.myselia.javacommon.communication.mail.MailService;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.javacommon.constants.opcode.ActionType;
import com.myselia.javacommon.constants.opcode.ComponentType;
import com.myselia.javacommon.constants.opcode.OpcodeBroker;
import com.myselia.javacommon.constants.opcode.operations.LensOperation;
import com.myselia.javacommon.constants.opcode.operations.SandboxMasterOperation;
import com.myselia.myapp.arduinosensorplant.Master;
import com.myselia.sandbox.runtime.routines.Strategy;
import com.myselia.sandbox.runtime.templates.MyseliaModule;
import com.myselia.sandbox.runtime.templates.lens.UIDataPacket;
import com.myselia.sandbox.runtime.templates.lens.data.DiscreteBarChartData;

public class LensUpdateStrategy implements Strategy {

	private Gson json = new Gson();
	
	@Override
	public void execute(Message message, MyseliaModule module) {
		Master master = (Master) module;
		
		String to_opcode = OpcodeBroker.make(ComponentType.LENS, null, ActionType.RUNTIME, LensOperation.DSUPDATE);
		String from_opcode = OpcodeBroker.make(ComponentType.SANDBOXMASTER, null, ActionType.RUNTIME,
				SandboxMasterOperation.DSDIGEST);
		master.tb.newTransmission(from_opcode, to_opcode);
		
		UIDataPacket packet = new UIDataPacket();
		packet.insertData(new DiscreteBarChartData("ONE", (double) Master.average_one/100));
		packet.insertData(new DiscreteBarChartData("TWO", (double) Master.average_two/100));	
		packet.insertData(new DiscreteBarChartData("THREE", (double) Master.average_three/100));	
		packet.insertData(new DiscreteBarChartData("FOUR", (double) Master.average_four/100));	
		//SHA1 of 'godzillafucksyourmum'
		master.tb.addAtom("12a7869435cca185b53597e93f277849c066fc55", "DataSeries", json.toJson(packet));
		
		master.mailbox.enqueueOut(master.tb.getTransmission());
		MailService.notify(master);
	}

}
