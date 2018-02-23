package org.usfirst.frc.team3555.robot.Test;

import java.io.IOException;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Network.Packets.BooleanPacket;
import org.usfirst.frc.team3555.robot.Monitors.MonitorManager;

public class TestRobot {
	public static void main(String[] args) throws IOException {
		MonitorManager monitorManager = new MonitorManager();

		Server server = new Server(monitorManager);
		
		monitorManager.add(new TestMonitor(server, 0));
        server.start();
        
        server.send(new BooleanPacket(Controller.DriverStation, Properties.Enabled, true, 0));
        while(server.isAlive()) 
        	monitorManager.update();
	}
}
