package org.usfirst.frc.team3555.Robot.Monitors;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Network.Packets.Packet;

public abstract class Monitor {
	protected Server server;
	
	public Monitor(Server server) {
		this.server = server;
	}
	
	public abstract void update();
	public abstract void processPacket(Packet packet);
	
	public void sendPacket(Properties property, Object value) { server.send(Util.genPacket(getController(), property, value, getId())); }
	
	public abstract Controller getController();
	public abstract int getId();
}
