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
import org.usfirst.frc.team3555.Network.Packets.IntegerPacket;
import org.usfirst.frc.team3555.Network.Packets.Packet;
import org.usfirst.frc.team3555.Robot.Monitors.MonitorManager;

/**
 *	Robot Side will update the talon monitors in the robot 
 * @author Sam
 */
public class Server extends Thread {
	public static String host = "localhost";
	public static int port = 8546;
	
	private volatile ArrayList<Packet> toSend;
	
	private ServerSocket serverSocket;
	private ReadClientThread readThread;
	
	private MonitorManager monitorManager;
   
	private boolean running;
   
	public Server(MonitorManager monitorManager) throws IOException {
		this.monitorManager = monitorManager;
		toSend = new ArrayList<>();
	   
		running = true;
	   
		serverSocket = new ServerSocket(port);
	    serverSocket.setSoTimeout(10000);
   }
   
   @Override
   public void run() {
	   try {
		   System.out.println("Waiting For Client On Port: " + serverSocket.getLocalPort() + ".....");
		   Socket server = serverSocket.accept();
		   
		   System.out.println("Connected To Client: " + server.getRemoteSocketAddress());
		   DataOutputStream output = new DataOutputStream(server.getOutputStream());
		   output.flush();
		   
		   DataInputStream inputStream = new DataInputStream(server.getInputStream());
		   
		   readThread = new ReadClientThread(monitorManager, inputStream);
		   readThread.start();
		   
		   toSend.add(new IntegerPacket(Controller.CANTalon, Properties.Current, 842, 48));
		   
		   while(running) {
			   //Send
			   while(toSend.size() > 0) {
				   Packet packet = toSend.remove(0);
				   System.out.println(packet);
				   packet.write(output);
			   }
		   }
		   
		   server.close();
	   } catch(IOException e) {
		   e.printStackTrace();
	   }
   }
   
   public void send(Packet packet) { toSend.add(packet); System.out.println(toSend.size()); }
   public void shutDown() { running = false; }
}

class ReadClientThread extends Thread{
   private MonitorManager monitorManager;
   private DataInputStream inputStream;
   private boolean running;
   
   public ReadClientThread(MonitorManager monitorManager, DataInputStream inputStream) {
	   this.monitorManager = monitorManager;
	   this.inputStream = inputStream;
	   
	   running = true;
   }
   
   @Override
   public void run() {
	   while(running) {
		   try {
			   if(inputStream.available() > 0)
				   monitorManager.processPacket(Util.genPacket(inputStream));
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
	   }
   }
   
   public void shutDown() {
	   running = false;
   }
}