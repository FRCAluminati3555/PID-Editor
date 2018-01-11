package org.usfirst.frc.team3555.Robot;

import java.io.IOException;
import java.util.Scanner;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Robot.Monitors.MonitorManager;

public class Test {
	public static void main(String[] args) throws IOException {
		
		MonitorManager manager = new MonitorManager();
		Server server = new Server(manager);
		server.start();
		
		Scanner sc = new Scanner(System.in);
		String str = " ";
		
		while(!str.equals("f")) {
			str = sc.nextLine();
			
			server.send(Util.genPacket(Controller.CANTalon, Properties.Current, 15, 34));
		}
	}
}
