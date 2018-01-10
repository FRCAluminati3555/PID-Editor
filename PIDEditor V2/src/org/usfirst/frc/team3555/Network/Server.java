package org.usfirst.frc.team3555.Network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.usfirst.frc.team3555.Robot.Monitors.MonitorManager;

/**
 *	Robot Side will update the talon monitors in the robot 
 * @author Sam
 */
public class Server extends Thread {
	public static String host = "";
	public static int port = 8080;
	
	private ServerSocket serverSocket;
   
	private MonitorManager monitorManager;
	private ArrayList<Packet> toSend;
   
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
		   ObjectInputStream objIn = new ObjectInputStream(server.getInputStream());
		   ObjectOutputStream objOut = new ObjectOutputStream(server.getOutputStream());
		   
		   while(running) {
			   //Read
			   try {
				   monitorManager.processPacket((Packet) objIn.readObject());
			   } catch(ClassNotFoundException e) {
				   e.printStackTrace();
			   } catch(EOFException e) {
				   
			   }
			   
			   //Send
			   for(int i = toSend.size() - 1; i >= 0; i--)
				   objOut.writeObject(toSend.remove(i));
		   }
		   
		   server.close();
	   } catch(IOException e) {
		   e.printStackTrace();
	   }
   }
   
   public void send(Packet packet) { toSend.add(packet); }
   public void shutDown() { running = false; }
}