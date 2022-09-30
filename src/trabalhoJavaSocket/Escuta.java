package trabalhoJavaSocket;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JScrollPane;
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
				String str = s.nextLine();
				chat.append("JavaAmigo: "+str+"\n");
				//comando para descer automaticamente a tela ao receber mensagem
				chat.setCaretPosition(chat.getText().length());
			}
			s.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
