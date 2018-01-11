package org.usfirst.frc.team3555.Editor.DeviceInfo;

import java.util.ArrayList;
import java.util.HashMap;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Network.Client;
import org.usfirst.frc.team3555.Network.Packets.Packet;

public class DeviceInfoManager {
	private Client client;
	private HashMap<Controller, ArrayList<DeviceInfo>> devices;
	
	public DeviceInfoManager() {
		devices = new HashMap<>();
		
		for(Controller controller : Controller.values())
			devices.put(controller, new ArrayList<>());
	}
	
	/**
	 * Put In data without sending over network
	 * Allows for data that doesn't need to be sent, not send
	 */
	public void putData(Controller controller, Properties property, Object value, int id) {
		getDeviceInfo(controller, id).putInfo(property, value);
	}
	
	/**
	 * Sends the information over the network as a packet and stores the value in the device info object
	 */
	public void sendData(Controller controller, Properties property, Object value, int id) {
		DeviceInfo device = getDeviceInfo(controller, id);
		device.putInfo(property, value);
		device.sendPacket(Util.genPacket(controller, property, value, id));
	}
	
	public Object getInfo(Controller controller, Properties property, int id) {
		DeviceInfo device = getDeviceInfo(controller, id);
		if(device != null)
			return device.getInfo(property);
		return null;
	}
	
	public DeviceInfo getDeviceInfo(Controller controller, int id) {
		for(DeviceInfo device : devices.get(controller))
			if(device.getId() == id) return device;
		return null;
	}
	
	/**
	 * Returns whether or not the operation was successful 
	 */
	public boolean processPacket(Packet packet) {
		for(DeviceInfo monitor : devices.get(packet.getController())) {
			if(monitor.getId() == packet.getId()) {
				System.out.println(packet);
				monitor.processPacket(packet);
				return true;
			}
		}
		return false;
	}
	
	public void addDevice(Controller controller, int id) {
		if(controller == Controller.CANTalon)
			devices.get(controller).add(new CANTalonDeviceInfo(client, id));
	}
	
	public void setClient(Client client) { this.client = client; }
}
