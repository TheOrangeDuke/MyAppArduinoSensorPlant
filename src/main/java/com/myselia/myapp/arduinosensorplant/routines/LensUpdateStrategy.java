package com.myselia.myapp.arduinosensorplant.routines;

import com.myselia.javacommon.communication.mail.MailService;
import com.myselia.javacommon.communication.units.Message;
import com.myselia.javacommon.communication.units.Task;
import com.myselia.javacommon.constants.opcode.ActionType;
import com.myselia.javacommon.constants.opcode.ComponentType;
import com.myselia.javacommon.constants.opcode.OpcodeBroker;
import com.myselia.javacommon.constants.opcode.operations.LensOperation;
import com.myselia.javacommon.constants.opcode.operations.SandboxMasterOperation;
import com.myselia.javacommon.topology.MyseliaUUID;
import com.myselia.myapp.arduinosensorplant.Master;
import com.myselia.sandbox.runtime.routines.Strategy;
import com.myselia.sandbox.runtime.templates.MyseliaModule;

public class LensUpdateStrategy implements Strategy {

	@Override
	public void execute(Task t, MyseliaModule mm) {
		
		Master master = (Master) mm;
		String to_opcode = OpcodeBroker.make(ComponentType.LENS, null, ActionType.DATA, LensOperation.TESTDATA);
		String from_opcode = OpcodeBroker.make(ComponentType.SANDBOXMASTER, null, ActionType.RUNTIME,
				SandboxMasterOperation.TRANSFER);
		
		master.tb.newTransmission(from_opcode, to_opcode);
		master.tb.addAtom("average_one", "Integer", Integer.toString(Master.average_one));
		master.tb.addAtom("average_two", "Integer", Integer.toString(Master.average_two));
		master.tb.addAtom("average_three", "Integer", Integer.toString(Master.average_three));
		master.tb.addAtom("average_four", "Integer", Integer.toString(Master.average_four));
		master.mailbox.enqueueOut(master.tb.getTransmission());
		MailService.notify(master);
		
	}

	@Override
	public void execute(Task t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Message m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Message m, MyseliaModule mm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Message m, MyseliaModule mm, MyseliaUUID source) {
		// TODO Auto-generated method stub
		
	}

}
