package org.usfirst.frc.team3555.Robot.Monitors;

import org.usfirst.frc.team3555.Network.Packet;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;

public abstract class Monitor {
	protected Server server;
	
	public Monitor(Server server) {
		this.server = server;
	}
	
	public abstract void update();
	public abstract void processPacket(Packet packet);
	
	public Packet generatePacket(Properties property, Object value) { return new Packet(getController(), property, value, getId()); }
	public void sendPacket(Properties property, Object value) { server.send(generatePacket(property, value)); }
	
	public abstract Controller getController();
	public abstract int getId();
}
