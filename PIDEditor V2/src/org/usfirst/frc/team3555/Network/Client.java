package org.usfirst.frc.team3555.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfoManager;
import org.usfirst.frc.team3555.Network.Packets.Packet;

/**
 *	Editor Side -> Updates the controller information  
 * @author Sam
 */
public class Client extends Thread {
	private ReadServerThread readServer;
	
	private DeviceInfoManager deviceManager;
	private ArrayList<Packet> toSend;
	private boolean running;
	
	public Client(DeviceInfoManager deviceManager) {
		this.deviceManager = deviceManager;
		
		toSend = new ArrayList<>();
		running = true;
	} 
	
	@Override
	public void run() {
		try {
			System.out.println("Client Connecting To: " + Server.host + ", On Port: " + Server.port);
			Socket client = new Socket(Server.host, Server.port);
			
			System.out.println("Client Connected To: " + Server.host + ", On Port: " + Server.port);
			
			DataInputStream inputStream = new DataInputStream(client.getInputStream());
			DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
			
			readServer = new ReadServerThread(deviceManager, inputStream);
			readServer.start();
			
			while(running) {
				//Send
				if(!toSend.isEmpty())
					for(int i = toSend.size() - 1; i >= 0; i--)
						toSend.remove(i).write(outputStream);
			}
			
			client.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(Packet packet) { toSend.add(packet); }
	public void shutDown() { running = false; }
}

class ReadServerThread extends Thread {
   private DeviceInfoManager deviceManager;
   private DataInputStream inputStream;
   private boolean running;
   
   public ReadServerThread(DeviceInfoManager deviceManager, DataInputStream inputStream) {
	   this.deviceManager = deviceManager;
	   this.inputStream = inputStream;
	   
	   running = true;
   }
   
   @Override
   public void run() {
	   while(running) {
		   try {
			   System.out.println("Read");
			   Packet packet = Util.genPacket(inputStream);
			   System.out.println(packet);
			   deviceManager.processPacket(packet);
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
	   }
   }
   
   public void shutDown() {
	   running = false;
   }
}
