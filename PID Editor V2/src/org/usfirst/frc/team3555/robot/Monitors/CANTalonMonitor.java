package org.usfirst.frc.team3555.robot.Monitors;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Network.Packets.Packet;
import org.usfirst.frc.team3555.robot.CANTalon;

public class CANTalonMonitor extends Monitor {
	private CANTalon talon;
	
	public CANTalonMonitor(Server server, CANTalon talon) {
		super(server);
		this.talon = talon;
		
//		talon.configForwardSoftLimitEnable(true, 10);
//		talon.configPeakOutputForward(.8, 10);
	}
	
	@Override
	public void update() {//Send data -> Speed, position, current
		if(talon.getSensorUnitsPerRotation() > 0) {
			sendPacket(Properties.Velocity, talon.getVelocityRPM());
			sendPacket(Properties.Position, talon.getPositionRotations());
		}
		
		sendPacket(Properties.Current, talon.getOutputCurrent());
		sendPacket(Properties.Voltage, talon.getMotorOutputVoltage());
		sendPacket(Properties.Temperature, talon.getTemperature());
		
		talon.set(talon.getSetPoint());
	}

	@Override
	public void processPacket(Packet packet) {//Read data and change the talon accordingly
		Properties property = packet.getProperty();

		if(property == Properties.Enabled) {
			talon.setEnabled((boolean) packet.getValue());
			System.out.println("\n\nSet Enabled " + (boolean) packet.getValue() + "\n");
		} else if(property == Properties.SetPoint) {
			talon.set((double) packet.getValue());
			System.out.println("\n\nSetPoint " + (double) packet.getValue() + "\n");
		} else if(property == Properties.ControlMode) {
			talon.setControlMode((int) packet.getValue());
			System.out.println("\n\nMode " + (int) packet.getValue() + "\n");
		} else if(property == Properties.P) {
			talon.setP((double) packet.getValue());
			System.out.println("\n\nP " + (double) packet.getValue() + "\n");
		} else if(property == Properties.I) {
			talon.setI((double) packet.getValue());
			System.out.println("\n\nI " + (double) packet.getValue() + "\n");
		} else if(property == Properties.D) {
			talon.setD((double) packet.getValue());
			System.out.println("\n\nD " + (double) packet.getValue() + "\n");
		} else if(property == Properties.F) {
			talon.setF((double) packet.getValue());
			System.out.println("\n\nF " + (double) packet.getValue() + "\n");
		} else if(property == Properties.FeedBackSensor) {
			talon.setFeedbackDevice((int) packet.getValue());
			System.out.println("\n\nFeedBackDevice " + (int) packet.getValue() + "\n");
		} else if(property == Properties.SensorUnitsPerRotation) {
			talon.setSensorUnitsPerRotation((int) packet.getValue());
			System.out.println("\n\nSensor Units " + (int) packet.getValue() + "\n");
		}
	}

	@Override
	public Controller getController() { return Controller.CANTalon; }
	@Override
	public int getId() { return talon.getDeviceID(); }
}
