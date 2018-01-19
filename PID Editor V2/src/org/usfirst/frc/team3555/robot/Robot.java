/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3555.robot;

import java.io.IOException;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Network.Packets.BooleanPacket;
import org.usfirst.frc.team3555.robot.Monitors.CANTalonMonitor;
import org.usfirst.frc.team3555.robot.Monitors.MonitorManager;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
	private Server server;
	private MonitorManager monitorManager;
	
	@Override
	public void robotInit() {
		monitorManager = new MonitorManager();
		
		try {
			server = new Server(monitorManager);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		monitorManager.add(new CANTalonMonitor(server, new CANTalon(1)));
		
        server.start();
	}

	@Override 
	public void teleopInit() {
		server.send(new BooleanPacket(Controller.DriverStation, Properties.Enabled, true, 0));
		server.send(new BooleanPacket(Controller.CANTalon, Properties.Enabled, false, 1));
	}
	
	@Override
	public void teleopPeriodic() {
		monitorManager.update();
		Timer.delay(.01);
	}
	
	@Override
	public void disabledInit() {
		server.send(new BooleanPacket(Controller.DriverStation, Properties.Enabled, false, 0));
	}
}
