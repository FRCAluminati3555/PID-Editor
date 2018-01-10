package org.usfirst.frc.team3555.Editor.DeviceInfo;

import java.util.ArrayList;
import java.util.HashMap;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Network.Packet;

public class DeviceInfoManager {
	private HashMap<Controller, ArrayList<DeviceInfo>> devices;
	
	public DeviceInfoManager() {
		devices = new HashMap<>();
		
		for(Controller controller : Controller.values())
			devices.put(controller, new ArrayList<>());
	}
	
	public void processPacket(Packet packet) {
		for(DeviceInfo monitor : devices.get(packet.getController())) {
			if(monitor.getId() == packet.getId()) {
				monitor.processPacket(packet);
				return;
			}
		}
	}
}
