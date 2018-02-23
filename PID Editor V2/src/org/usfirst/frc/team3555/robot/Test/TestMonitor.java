package org.usfirst.frc.team3555.robot.Test;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfo;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Network.Packets.Packet;
import org.usfirst.frc.team3555.robot.Monitors.Monitor;

public class TestMonitor extends Monitor {
	private DeviceInfo info;
	private double x;
	
	public TestMonitor(Server server, int id) {
		super(server);
		
		info = new DeviceInfo(Controller.CANTalon, null, id);
	}
	
	@Override
	public void update() {
		info.putInfo(Properties.RotationalVelocity, Math.cos(x));
		info.putInfo(Properties.Current, Math.sin(x));           
		info.putInfo(Properties.Voltage, Math.cos(x / 2.0));           
		info.putInfo(Properties.Temperature, Math.sin(x / 2.0));
      	
		x += .1;
		
		sendPacket(Properties.RotationalVelocity);
		sendPacket(Properties.Current);
		sendPacket(Properties.Voltage);
		sendPacket(Properties.Temperature);
	}

	@Override
	public void processPacket(Packet packet) {
		System.out.println("Test Program: Setting " + packet.getProperty() + " to " + packet.getValue());
		info.processPacket(packet);
	}

	private void sendPacket(Properties property) { sendPacket(property, info.getInfo(property)); }
	public Controller getController() { return info.getController(); }
	public int getId() { return info.getId(); }
}