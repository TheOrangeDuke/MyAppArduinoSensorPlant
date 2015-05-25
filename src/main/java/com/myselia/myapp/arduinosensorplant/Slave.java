package com.myselia.myapp.arduinosensorplant;

import java.util.Random;

import com.google.gson.Gson;
import com.mycelia.common.communication.units.TransmissionBuilder;
import com.mycelia.common.constants.opcode.ActionType;
import com.mycelia.common.constants.opcode.ComponentType;
import com.mycelia.common.constants.opcode.OpcodeAccessor;
import com.mycelia.sandbox.runtime.templates.MyceliaSlaveModule;
import com.myselia.myapp.arduinosensorplant.structures.ArduinoTransmission;
import com.myselia.myapp.arduinosensorplant.tools.ArduinoSensorDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;
import java.util.Scanner;

public class Slave extends MyceliaSlaveModule {
	TransmissionBuilder tb = new TransmissionBuilder();
	Gson jsonInterpreter = new Gson();
	ArduinoSensorDriver asd = new ArduinoSensorDriver();
	int transmission_count = 0;

	public Slave() {
		System.out.println("Slave Module Initialisation");
	}

	@Override
	public void setup() {
		System.out.println("SLAVE SETUP");
		asd.initialize();
		asd.roll();
	}

	@Override
	protected void tick() {
		
		boolean check = false;
		
		if(asd.getArduinoTransmission() != null){
			if(asd.getArduinoTransmission().getTransmission() > transmission_count){
				transmission_count = asd.getArduinoTransmission().getTransmission();
				check=true;
			}
		}
		
		
		if (check) {
			tb.newTransmission(OpcodeAccessor.make(ComponentType.SANDBOXSLAVE,
					ActionType.RUNTIME, "DATA"), OpcodeAccessor.make(
					ComponentType.LENS, ActionType.RUNTIME, "DATA"));
			
			tb.addAtom("ArduinoTransmission", "ArduinoTransmission",
					jsonInterpreter.toJson(asd.getArduinoTransmission()));

			mailbox.putInOutQueue(tb.getTransmission());
		}
	}
}