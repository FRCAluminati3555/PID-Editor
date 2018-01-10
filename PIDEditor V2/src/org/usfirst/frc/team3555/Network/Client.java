package org.usfirst.frc.team3555.Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *	Editor Side -> Updates the controller information  
 * @author Sam
 */
public class Client extends Thread {
	private ArrayList<Packet> toSend;
	private boolean running;
	
	public Client() {
		toSend = new ArrayList<>();
		running = true;
	} 
	
	@Override
	public void run() {
		try {
			System.out.println("Client Connecting To: " + Server.host + ", On Port: " + Server.port);
			Socket client = new Socket(Server.host, Server.port);
			
			System.out.println("Client Connected To: " + Server.host + ", On Port: " + Server.port);
			ObjectInputStream objIn = new ObjectInputStream(client.getInputStream());
			ObjectOutputStream objOut = new ObjectOutputStream(client.getOutputStream());
			
			while(running) {
				//Read 
				
				//Send
				for(int i = toSend.size() - 1; i >= 0; i--)
					objOut.writeObject(toSend.remove(i));
			}
			
			client.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(Packet packet) { toSend.add(packet); }
	public void shutDown() { running = false; }
}
