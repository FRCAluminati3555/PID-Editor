package org.usfirst.frc.team3555.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Network.Packets.BooleanPacket;
import org.usfirst.frc.team3555.Network.Packets.Packet;
import org.usfirst.frc.team3555.robot.Monitors.MonitorManager;

import edu.wpi.first.wpilibj.DriverStation;

/**
 *	Robot Side will update the talon monitors in the robot 
 * @author Sam
 */
public class Server extends Thread {
//	public static String host = "localhost";
	public static String host = "172.22.11.2";
//	public static String host = "10.35.55.2";
	public static int port = 8080;
	
	private ServerSocket serverSocket;
	private ReadClientThread readThread;
	
	private volatile MonitorManager monitorManager;
	private volatile ArrayList<Packet> toSend;
   
	private volatile boolean running;
   
	public Server(MonitorManager monitorManager) throws IOException {
		this.monitorManager = monitorManager;
		toSend = new ArrayList<>();
	   
		running = true;
	   
		serverSocket = new ServerSocket(port);
	    serverSocket.setSoTimeout(0);
   }
   
   @Override
   public void run() {
	   try {
		   System.out.println("SERVER\n");
		   System.out.println("Waiting For Client On Port: " + serverSocket.getLocalPort() + ".....");
		   Socket server = serverSocket.accept();
		   
		   System.out.println("Connected To Client: " + server.getRemoteSocketAddress());
		   DataOutputStream outputStream = new DataOutputStream(server.getOutputStream());
		   outputStream.flush();
		   
		   DataInputStream inputStream = new DataInputStream(server.getInputStream());
		   
		   readThread = new ReadClientThread(monitorManager, inputStream);
		   readThread.start();
		   
		   send(new BooleanPacket(Controller.DriverStation, Properties.Enabled, DriverStation.getInstance().isEnabled(), 0));
		   
		   while(running) {
			   //Send
			   System.out.println(toSend.size());
			   while(toSend.size() > 0) {
				   Packet packet = toSend.remove(0);
//				   System.out.println("Server Sent: " + packet);
				   if(packet != null && outputStream != null)
					   packet.write(outputStream);
			   }
			   
//			   if(toSend.size() > 0) {
//				   Packet packet = toSend.remove(0);
//				   System.out.println("Server Sent: " + packet);
//				   packet.write(outputStream);
//				   toSend.clear();
//			   }
			   
			   try {
				   sleep(10);
			   } catch (InterruptedException e) {
				   e.printStackTrace();
			   }
		   }
		   
		   server.close();
	   } catch(IOException e) {
		   e.printStackTrace();
	   }
   }
   
   public void send(Packet packet) { toSend.add(packet); }
   public void shutDown() { running = false; readThread.shutDown(); }
}

class ReadClientThread extends Thread {
   private volatile MonitorManager monitorManager;
   private volatile DataInputStream inputStream;
   private volatile boolean running;
   
   public ReadClientThread(MonitorManager monitorManager, DataInputStream inputStream) {
	   this.monitorManager = monitorManager;
	   this.inputStream = inputStream;
	   
	   running = true;
   }
   
   @Override
   public void run() {
	   while(running) {
		   try {
			   if(inputStream.available() > 0) {
				   Packet packet = Util.genPacket(inputStream);
//				   System.out.println("Server Read: " + packet);
				   monitorManager.processPacket(packet);
			   }
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		   
		   try {
			   sleep(10);
		   } catch (InterruptedException e) {
			   e.printStackTrace();
		   }
	   }
   }
   
   public void shutDown() {
	   running = false;
   }
}