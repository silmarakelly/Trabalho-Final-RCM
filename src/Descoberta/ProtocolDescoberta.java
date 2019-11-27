package Descoberta;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ProtocolDescoberta {

	public void broadcast(String broadcastMessage, InetAddress address, DatagramSocket socket) throws IOException {

		socket.setBroadcast(true);
		byte[] buffer = new byte[30];

		String msgrecebe = "b/"+broadcastMessage;
		buffer = msgrecebe.getBytes();
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 33000);
		socket.send(packet);
		System.out.println("Feito broadcast");

	}

}
