package org.usfirst.frc.team3555.robot.Monitors;

import java.util.ArrayList;
import java.util.HashMap;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Network.Packets.Packet;

public class MonitorManager {
	private HashMap<Controller, ArrayList<Monitor>> monitors;
	
	public MonitorManager() {
		this.monitors = new HashMap<>();
		
		for(Controller controller : Controller.values())
			monitors.put(controller, new ArrayList<>());
	}
	
	public void update() {
		for(Controller controller : monitors.keySet()) 
			for(Monitor monitor : monitors.get(controller))
				monitor.update();
	}
	
	public void add(Monitor monitor) {
		monitors.get(monitor.getController()).add(monitor);
	}
	
	public void processPacket(Packet packet) {
		for(Monitor monitor : monitors.get(packet.getController())) {
			if(monitor.getId() == packet.getId()) {
				monitor.processPacket(packet);
				return;
			}
		}
	}
}
