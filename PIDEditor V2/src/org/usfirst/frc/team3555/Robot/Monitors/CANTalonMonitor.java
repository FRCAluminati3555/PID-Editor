package org.usfirst.frc.team3555.Robot.Monitors;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Network.Packets.Packet;
import org.usfirst.frc.team3555.Robot.CANTalon;

public class CANTalonMonitor extends Monitor {
	private CANTalon talon;
	
	public CANTalonMonitor(Server server, CANTalon talon) {
		super(server);
		this.talon = talon;
	}
	
	@Override
	public void update() {//Send data -> Speed, position, current
		sendPacket(Properties.Velocity, talon.getVelocityRPM());
		sendPacket(Properties.Current, talon.getOutputCurrent());
		sendPacket(Properties.Voltage, talon.getMotorOutputVoltage());
	}

	@Override
	public void processPacket(Packet packet) {//Read data and change the talon accordingly
		Properties property = packet.getProperty();

		if(property == Properties.Enabled)
			talon.setEnabled((boolean) packet.getValue());
		else if(property == Properties.SetPoint)
			talon.set((double) packet.getValue());
	}

	@Override
	public Controller getController() { return Controller.CANTalon; }
	@Override
	public int getId() { return talon.getDeviceID(); }
}
