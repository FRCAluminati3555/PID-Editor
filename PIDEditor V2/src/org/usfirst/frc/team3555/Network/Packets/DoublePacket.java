package org.usfirst.frc.team3555.Network.Packets;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;

public class DoublePacket extends Packet {

	public DoublePacket(Controller controller, Properties property, double value, int id) {
		super(controller, property, value, id);
		
	}
}
