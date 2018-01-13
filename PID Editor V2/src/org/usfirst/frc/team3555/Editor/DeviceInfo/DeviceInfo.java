package org.usfirst.frc.team3555.Editor.DeviceInfo;

import java.util.HashMap;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Network.Client;
import org.usfirst.frc.team3555.Network.Packets.Packet;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class DeviceInfo {
	protected Client client; 
	
	protected Controller controller;
	protected int id;
	
	protected HashMap<Properties, Object> values;
	
	public DeviceInfo(Controller controller, Client client, int id) {
		this.controller = controller;
		this.client = client;
		this.id = id;
		
		values = new HashMap<>();
		initInfo();
	}
	
	public DeviceInfo(Client client, Packet packet) {
		this.client = client;
		this.controller = packet.getController();
		this.id = packet.getId();
		
		values = new HashMap<>();
		
		initInfo();
		processPacket(packet);
	}
	
	private void initInfo() {
		values.put(Properties.ControlMode, 0.0);
		values.put(Properties.Current, 0.0);
		values.put(Properties.D, 0.0);
		values.put(Properties.Enabled, false);
		values.put(Properties.F, 0.0);
		values.put(Properties.FeedBackSensor, -1);
		values.put(Properties.I, 0.0);
		values.put(Properties.Name, String.valueOf(id));
		values.put(Properties.P, 0.0);
		values.put(Properties.Position, 0.0);
		values.put(Properties.ResetSensorPosition, false);
		values.put(Properties.SensorUnitsPerRotation, 0.0);
		values.put(Properties.SetPoint, 0.0);
		values.put(Properties.Temperature, 0.0);
		values.put(Properties.Velocity, 0.0);
		values.put(Properties.Voltage, 0.0);
	}
	
	public void putInfo(Properties property, Object value) {
		values.put(property, value);
	}

	/**
	 * 
	 * Determine the value of the motor depending on the control mode. 
	 * Example: If this is a talon in position mode, it will return the position of the sensor, Velocity mode -> return velocity, etc...
	 * 
	 * @return the value of the motor depending on the control mode
	 * 
	 * TODO Determine value
	 */
	public double getValue() {
		return 0;
	}
	
	public void processPacket(Packet packet) {
		putInfo(packet.getProperty(), packet.getValue());
	}
	
	protected void sendPacket(Packet packet) { client.send(packet); }
	
	public Controller getController() { return controller; }
	public Object getInfo(Properties property) { return values.get(property); }
	public int getId() { return id; }
}
