package org.usfirst.frc.team3555.Editor.DeviceInfo;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Network.Client;

public class CANTalonDeviceInfo extends DeviceInfo {

	public CANTalonDeviceInfo(Client client, int id) {
		super(Controller.CANTalon, client, id);
		
	}
}
