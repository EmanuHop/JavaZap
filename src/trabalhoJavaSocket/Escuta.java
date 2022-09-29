package trabalhoJavaSocket;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JTextArea;

public class Escuta implements Runnable{
	private Socket client;
	private JTextArea chat;
	
	public Escuta(Socket client, JTextArea chat) {
		this.client = client;
		this.chat = chat;
	}
	
	@Override
	public void run() {
		Scanner s;
		try {
			s = new Scanner(this.client.getInputStream());
			while(s.hasNextLine()) {
				chat.append("JavaAmigo: "+s.nextLine()+"\n");
			}
			s.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
