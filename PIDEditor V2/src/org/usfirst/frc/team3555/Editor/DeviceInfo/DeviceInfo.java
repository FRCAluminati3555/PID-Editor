package org.usfirst.frc.team3555.Editor.DeviceInfo;

import java.util.HashMap;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Network.Client;
import org.usfirst.frc.team3555.Network.Packets.Packet;

public abstract class DeviceInfo {
	protected Client client; 
	
	protected Controller controller;
	protected int id;
	
	protected HashMap<Properties, Object> values;
	
	public DeviceInfo(Controller controller, Client client, int id) {
		this.controller = controller;
		this.client = client;
		this.id = id;
		
		values = new HashMap<>();
	}
	
	public void putInfo(Properties property, Object value) {
		if(values.keySet().contains(property))
			values.put(property, value);
	}
	
	public void processPacket(Packet packet) {
		putInfo(packet.getProperty(), packet.getValue());
	}

	protected void sendPacket(Packet packet) { client.send(packet); }
	public Object getInfo(Properties property) { return values.get(property); }
	public int getId() { return id; }
}
