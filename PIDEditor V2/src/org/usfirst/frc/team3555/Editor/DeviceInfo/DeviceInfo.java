package org.usfirst.frc.team3555.Editor.DeviceInfo;

import org.usfirst.frc.team3555.Network.Packet;

public abstract class DeviceInfo {

	public DeviceInfo() {
		
	}
	
	public abstract void processPacket(Packet packet);
	public abstract int getId();
}
