package org.usfirst.frc.team3555.robot;

import java.util.Scanner;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfoManager;
import org.usfirst.frc.team3555.Network.Client;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;

public class TestClient {
	
	/**
	 *  This is to test the networking on the rio with the client from the computer
	 */
	public static void main(String[] args) {
		DeviceInfoManager deviceManager = new DeviceInfoManager();
		Client client = new Client(deviceManager);
		deviceManager.setClient(client);
		client.start();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		client.send(Util.genPacket(Controller.CANTalon, Properties.Enabled, true, 0));
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		client.send(Util.genPacket(Controller.CANTalon, Properties.SetPoint, .5, 0));
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		client.shutDown();
	}
}
