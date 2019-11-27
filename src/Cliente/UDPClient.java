package Cliente;

import java.net.*;
import java.util.Scanner;

import Descoberta.ProtocolDescoberta;

import java.io.*;

public class UDPClient {
	public static void main(String args[]) {
		DatagramSocket aSocket = null;
		Scanner entrada = new Scanner(System.in);
		ProtocolDescoberta PD = new ProtocolDescoberta();
		while (true) {
			try {
				aSocket = new DatagramSocket();
				byte[] buffer = new byte[1024];

				String ip = "localhost";
				//InetAddress aHost = InetAddress.getByName(ip);
				//int serverPort = 33000;

				System.out.println("Digite seu nome:");
				String NOME = entrada.nextLine();
				InetAddress address = InetAddress.getLocalHost();
				NetworkInterface ni = NetworkInterface.getByInetAddress(address);
				byte[] mac = ni.getHardwareAddress();
				String macAddress = "";
				for (int i = 0; i < mac.length; i++) {
					macAddress += (String.format("%02X-", mac[i]));
				}

				String MAC = macAddress.substring(0, macAddress.length() - 1);
				String resposta = MAC + ":" + NOME;
				System.out.println("Deseja fazer broadcast?");
				String entradacli = entrada.nextLine();
				if (entradacli.equals("sim")) {
					PD.broadcast(resposta, InetAddress.getByName("255.255.255.255"), aSocket);
				}

				while (true) {
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(reply);
					
					String receive = new String(reply.getData()); //pega os dados enviados
					String[] op = receive.split("/"); // separa por /
					String msgrecebida = op[1]; // dados sem a operacao
					System.out.println("Mensagem de: " + reply.getAddress());
					System.out.println(msgrecebida); //mostra os dados
					System.out.println("-------------------------------------------");
					
					System.out.println("Você: ");
					String msgrecebe = entrada.nextLine();
					String msg = "c/" + msgrecebe;

					byte[] m = msg.getBytes();

					DatagramPacket request = new DatagramPacket(m, m.length, reply.getAddress(), reply.getPort());
					aSocket.send(request);
					System.out.println("Mensagem enviada");

				}

			} catch (SocketException e) {
				System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("IO: " + e.getMessage());
			} finally {
				if (aSocket != null) {
					aSocket.close();
					entrada.close();
				}
			}
		}
	}
}