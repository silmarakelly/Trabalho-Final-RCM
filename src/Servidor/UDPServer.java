package Servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;

/*public class UDPServer {
	public static void main(String args[]) {
		DatagramSocket aSocket = null;
		Scanner entrada = new Scanner(System.in);
		String[] array = new String[2];

		try {

			aSocket = new DatagramSocket(33000);
			byte[] buffer = new byte[1024];
			byte[] buffer2 = new byte[1024];
			
			while (true) {
				DatagramPacket recebimento = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(recebimento);
				String receive = new String(recebimento.getData());
				array = receive.split(":");
				String enderecoMAC = array[0];
				String nome = array[1];
				System.out.println("Conectado: " + nome);
				System.out.println("Deseja conectar-se com " + nome + "?(sim/não)");
				String desejo = entrada.nextLine();
				
				
				if (desejo.equalsIgnoreCase("sim")) {
					while (true) {
						System.out.println("Você: ");
						String msg = entrada.nextLine();

						byte[] m = msg.getBytes();

						DatagramPacket envio = new DatagramPacket(m, m.length, recebimento.getAddress(),
								recebimento.getPort());
						aSocket.send(envio);
						System.out.println("Mensagem enviada");
						System.out.println("-------------------------------------------");
						DatagramPacket reply = new DatagramPacket(buffer2, buffer2.length);
						aSocket.receive(reply);
						System.out.println("Mensagem de: " + array[1]);
						System.out.println(new String(reply.getData()));
						entrada.nextLine();
					}

				}

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

*/

public class UDPServer {
	public static void main(String args[]) throws Exception {
		byte[] buffer = new byte[1024];
		byte[] bufferchat = new byte[1024];
		String[] op = new String[1024];
		DatagramSocket aSocket = null;
		DatagramPacket recebimento = null;
		boolean fez = false;
		String info = "";
		
		try {
			aSocket = new DatagramSocket(33000);

			while (true) {
				if (!fez) {
					recebimento = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(recebimento);
					String receive = new String(recebimento.getData());
					op = receive.split("/");
					info = op[0];
					if (info.equals("b")) {
						String msgrecebe = op[1];
						byte[] msfinal = msgrecebe.getBytes();
						DatagramPacket msgBroad = new DatagramPacket(msfinal, msfinal.length,recebimento.getAddress(),recebimento.getPort());
						fez = true;
						new Thread(new ThreadBroadcast(aSocket, msgBroad)).start();
					
					} 
					
				}
					recebimento = new DatagramPacket(bufferchat, bufferchat.length);
					aSocket.receive(recebimento);
					String receive = new String(recebimento.getData());
					op = receive.split("/");
					info = op[0];
					if(info.equals("c")) {
						System.out.println("Mensagem de: " + recebimento.getAddress());
						String msgrecebe = op[1];
						System.out.println(msgrecebe);
						byte[] msfinal = msgrecebe.getBytes();
						DatagramPacket msgchat = new DatagramPacket(msfinal, msfinal.length, recebimento.getAddress(), recebimento.getPort());
						System.out.println("-------------------------------------------");

						new Thread(new ThreadChat(aSocket, msgchat)).start();

				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class ThreadBroadcast implements Runnable {

	DatagramSocket socket = null;
	DatagramPacket packet = null;
	String[] conectados = null;
	Scanner entrada = new Scanner(System.in);
	byte[] buffer2 = new byte[1024];
	int cont = 0;
	public ThreadBroadcast(DatagramSocket socket, DatagramPacket packet) {
		this.socket = socket;
		this.packet = packet;
	}

	public void run() {
		
		String dados = new String(packet.getData());
		String [] info = dados.split(":");
		String mac = info[0];
		String nome = info[1];
		System.out.println("conectados: "+nome);
		//conectados[0] = conectados[cont]+dados+"/n";
		//cont = cont++;
		//for (int i=0; i<cont; i++) {
			//System.out.println(conectados[i]);
			
		//}
		System.out.println("Deseja conectar-se com " + nome + "?(sim/não)");
		String desejo = entrada.nextLine();
		
		
		
		try {
			if (desejo.equalsIgnoreCase("sim")) {
				System.out.println("Você: ");

				String msgrecebe = entrada.nextLine();
				String msg = "c/" + msgrecebe;
				byte[] m = msg.getBytes();

				DatagramPacket envio = new DatagramPacket(m, m.length, packet.getAddress(), packet.getPort());

				socket.send(envio);

				System.out.println("Mensagem enviada");
				System.out.println("-------------------------------------------");
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}

class ThreadChat implements Runnable {
	
	DatagramPacket packet = null;
	DatagramSocket socket = null;
	Scanner entrada = new Scanner(System.in);


	public ThreadChat(DatagramSocket socket, DatagramPacket packet) {
		this.socket = socket;
		this.packet = packet;
	

	}

	public void run() {
		try {
			System.out.println("Você: ");

			String msgrecebe = entrada.nextLine();
			String msg = "c/" + msgrecebe;
			byte[] m = msg.getBytes();

			DatagramPacket envio = new DatagramPacket(m, m.length, packet.getAddress(), packet.getPort());

			socket.send(envio);

			System.out.println("Mensagem enviada");
			System.out.println("-------------------------------------------");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
