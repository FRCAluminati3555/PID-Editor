/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3555.Robot;

import java.io.IOException;

import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Robot.Monitors.CANTalonMonitor;
import org.usfirst.frc.team3555.Robot.Monitors.MonitorManager;

import edu.wpi.first.wpilibj.IterativeRobot;

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
		
		monitorManager.add(new CANTalonMonitor(server, new CANTalon(0)));
        server.start();
	}

	@Override
	public void teleopPeriodic() {
		monitorManager.update();
	}
}
