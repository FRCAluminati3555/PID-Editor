package org.usfirst.frc.team3555.Network.Packets;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;

public class IntegerPacket extends Packet {

	public IntegerPacket(Controller controller, Properties property, int value, int id) {
		super(controller, property, value, id);
		
	}
}
