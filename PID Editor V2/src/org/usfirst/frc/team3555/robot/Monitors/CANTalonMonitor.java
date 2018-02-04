package org.usfirst.frc.team3555.robot.Monitors;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Network.Packets.Packet;
import org.usfirst.frc.team3555.robot.CANTalon;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;

public class CANTalonMonitor extends Monitor {
	private CANTalon talon;
	
	private long lastRotTime, lastLinearTime;
	private double lastRotVel, lastLinearVel;
	
	/**
	 * Lin Position is in distance
	 * Lin Velocity is in distance per second
	 * 
	 * Rot Acceleration is in rpm per second
	 * Lin Acceleration is in distance per second^2
	 */
	public CANTalonMonitor(Server server, CANTalon talon) {
		super(server);
		this.talon = talon;
		talon.enable();
		
//		talon.configForwardSoftLimitEnable(true, 10);
//		talon.configPeakOutputForward(.8, 10);
	}
	
	@Override
	public void update() {//Send data -> Speed, position, current
		if(talon.getSensorUnitsPerRotation() > 0) {
			sendPacket(Properties.RotationalVelocity, talon.getVelocityRPM());
			sendPacket(Properties.RotationalPosition, talon.getPositionRotations());
			
			if(lastRotTime == 0) {
				lastRotTime = System.currentTimeMillis();
			} else {
				double rotAcc = (talon.getVelocityRPM() - lastRotVel) / ((System.currentTimeMillis() - lastRotTime) / 1000.0);
				sendPacket(Properties.RotationalAcceleration, rotAcc);
				lastRotVel = talon.getVelocityRPM();
				lastRotTime = System.currentTimeMillis();
			}
			
			sendPacket(Properties.AnalogInPosition, talon.getAnalogInRotationPosition());
			sendPacket(Properties.AnalogInVelocity, talon.getAnalogInRPMVelocity());
		}
		
		if(talon.getDistancePerRotation() > 0) {
			sendPacket(Properties.LinearVelocity, talon.getLinearVelocity());
			sendPacket(Properties.LinearPosition, talon.getPositionLinearDistance());
			
			if(lastLinearTime == 0) {
				lastLinearTime = System.currentTimeMillis();
			} else {
				double linAcc = (talon.getLinearVelocity() - lastLinearVel) / ((System.currentTimeMillis() - lastLinearTime) / 1000.0);
				sendPacket(Properties.LinearAcceleration, linAcc);
				lastLinearVel = talon.getLinearVelocity();
				lastLinearTime = System.currentTimeMillis();
			}
		}
		
		sendPacket(Properties.Current, talon.getOutputCurrent());
		sendPacket(Properties.Voltage, talon.getMotorOutputVoltage());
		sendPacket(Properties.Temperature, talon.getTemperature());
		
		talon.update();
	}

	@Override
	public void processPacket(Packet packet) {//Read data and change the talon accordingly
		Properties property = packet.getProperty();

		if(property == Properties.Enabled) {
			talon.setEnabled((boolean) packet.getValue());
			System.out.println("\n\nSet Enabled " + (boolean) packet.getValue() + "\n");
		} else if(property == Properties.SetPoint) {
			if(talon.getControlMode() == ControlMode.Velocity)
				talon.setVelocityRPM((double) packet.getValue());
			else
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
		} else if(property == Properties.DistancePerRotation) {
			talon.setDistancePerRotation((double) packet.getValue());
			System.out.println("\n\nDistance Units " + (double) packet.getValue() + "\n");
			
		} else if(property == Properties.EnableLimitSwitch) {
			talon.enableLimitSwitch(true);
			System.out.println("\n\nLimit Switch Eanble" + (boolean) packet.getValue() + "\n");
			
		} else if(property == Properties.ForwardLimitSwtichSource) {
			talon.setForwardLimitSwitchSource((int) packet.getValue());
			System.out.println("\n\nForward Limit Switch Source" + (int) packet.getValue() + "\n");
		} else if(property == Properties.ForwardLimitSwitchNormal) {
			talon.setForwardLimitSwitchNormal((int) packet.getValue());
			System.out.println("\n\nForward Limit Switch Normal " + (int) packet.getValue() + "\n");
		}
		
		else if(property == Properties.ReverseLimitSwtichSource) {
			talon.setReverseLimitSwitchSource((int) packet.getValue());
			System.out.println("\n\nReverse Limit Switch Source " + (int) packet.getValue() + "\n");
		} else if(property == Properties.ReverseLimitSwitchNormal) {
			talon.setReverseLimitSwitchNormal((int) packet.getValue());
			System.out.println("\n\nReverse Limit Switch Normal " + (int) packet.getValue() + "\n");
		}
		
		else if(property == Properties.EnableSoftLimit) {
			talon.enableSoftLimit(true);
			System.out.println("\n\nSoft Limit Enable " + (boolean) packet.getValue() + "\n");
		} else if(property == Properties.SoftLimitForward) {
			talon.setForwardSoftLimitRotations((double) packet.getValue());
			System.out.println("\n\nSoft Limit Forward " + (double) packet.getValue() + "\n");
		} else if(property == Properties.SoftLimitReverse) {
			talon.setReverseSoftLimitRotations((double) packet.getValue());
			System.out.println("\n\nSoft Limit Reverse " + (double) packet.getValue() + "\n");
		}
	}

	@Override
	public Controller getController() { return Controller.CANTalon; }
	@Override
	public int getId() { return talon.getDeviceID(); }
}
