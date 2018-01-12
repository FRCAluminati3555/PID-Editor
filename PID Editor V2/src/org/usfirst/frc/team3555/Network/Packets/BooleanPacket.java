package org.usfirst.frc.team3555.Network.Packets;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;

public class BooleanPacket extends Packet {

	public BooleanPacket(Controller controller, Properties property, boolean value, int id) {
		super(controller, property, value, id);
		
	}
}
