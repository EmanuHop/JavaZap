package trabalhoJavaSocket;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class JavaZap {

	private JFrame frmJavazap;
	private JFrame frame;
	private JTextField txtIp;
	private JTextField mensagem;
	private JTextArea chat;
	private JScrollPane scrollChat;
	private int id;
	private Socket client;
	private Socket clientServer;
	private ServerSocket server;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaZap window = new JavaZap();
					window.frmJavazap.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JavaZap() {
		initialize();
	}
	private void initialize() {
		/*
		 * Tela inicial
		 */
		frmJavazap = new JFrame();
		frmJavazap.setTitle("JavaZap");
		frmJavazap.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		frmJavazap.getContentPane().setBackground(Color.WHITE);
		frmJavazap.getContentPane().setLayout(null);
		JLabel Aviso = new JLabel("Selecione como deseja iniciar a sess\u00E3o");
		Aviso.setForeground(new Color(3, 117, 186));
		Aviso.setFont(new Font("Tahoma", Font.PLAIN, 24));
		Aviso.setBounds(107, 10, 416, 33);
		frmJavazap.getContentPane().add(Aviso);
		
		/*
		 * Painel servidor 
		 */
		JPanel panelServer = new JPanel();
		panelServer.setBackground(new Color(3, 117, 186));
		panelServer.setBorder(null);
		panelServer.setBounds(40, 68, 230, 110);
		panelServer.setLayout(null);
		frmJavazap.getContentPane().add(panelServer);
		
		JLabel laberServer = new JLabel("Server");
		laberServer.setForeground(new Color(255, 255, 255));
		laberServer.setBounds(85, 16, 55, 19);
		laberServer.setFont(new Font("Consolas", Font.BOLD, 15));
		laberServer.setAlignmentX(1.5f);
		panelServer.add(laberServer);
		//Botao Servidor
		JButton btnServer = new JButton("Abrir Servidor");
		btnServer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnServer.setBounds(49, 45, 120, 35);
		panelServer.add(btnServer);
		btnServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				id = 0;
				try {
					server = new ServerSocket(10000);
					clientServer = server.accept();
					
					Thread.sleep(1000);
					client = new Socket(clientServer.getInetAddress().getHostAddress(), 10001);
					
					Escuta e1 = new Escuta(clientServer, chat);
					Thread te = new Thread(e1);
					te.start();
					
					frame.setVisible(true);
					chat.setVisible(true);
					frmJavazap.setVisible(false);
				} catch (IOException er) {
					er.printStackTrace();
					JOptionPane.showMessageDialog(frmJavazap, "Servidor ja esta aberto", "Server", 0);
				} catch (InterruptedException er) {
					er.printStackTrace();
				}
			}
		});
		
		/*
		 * Painel Cliente
		 */
		JPanel panelClient = new JPanel();
		panelClient.setBackground(new Color(3, 117, 186));
		panelClient.setBounds(351, 68, 230, 110);
		frmJavazap.getContentPane().add(panelClient);
		panelClient.setLayout(null);
		JLabel labelClient = new JLabel("Client");
		labelClient.setForeground(new Color(255, 255, 255));
		labelClient.setHorizontalAlignment(SwingConstants.CENTER);
		labelClient.setFont(new Font("Consolas", Font.BOLD, 15));
		labelClient.setBounds(85, 10, 59, 20);
		panelClient.add(labelClient);
		//entrada IP
		txtIp = new JTextField();
		txtIp.setDropMode(DropMode.INSERT);
		txtIp.setToolTipText("IP");
		txtIp.setHorizontalAlignment(SwingConstants.CENTER);
		txtIp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtIp.setBounds(47, 32, 136, 20);
		panelClient.add(txtIp);
		txtIp.setColumns(10);
		//botao conectar
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				id = 1;
				String IP = "127.0.0.1";
				IP = txtIp.getText();
				try {
					client = new Socket(IP, 10000);
					
					server = new ServerSocket(10001);
					clientServer = server.accept();
					
					Escuta e1 = new Escuta(clientServer, chat);
					Thread te = new Thread(e1);
					te.start();
					
					frame.setVisible(true);
					chat.setVisible(true);
					frmJavazap.setVisible(false);
				}catch (ConnectException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frmJavazap, "Conexao recusada! Nenhum servidor encotrado", "Cliente", 0);
				}catch (BindException e2) {
					JOptionPane.showMessageDialog(frmJavazap, "Ja existe um cliente conectado ao servidor", "Cliente", 0);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		});
		btnConectar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnConectar.setBounds(55, 62, 120, 35);
		panelClient.add(btnConectar);
		
		frmJavazap.setBackground(new Color(128, 128, 64));
		frmJavazap.setBounds(100, 100, 620, 277);
		frmJavazap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		 * Janela Chat
		 */
		frame = new JFrame();
		frame.setTitle("Chat");
		frame.getContentPane().setBackground(new Color(46, 52, 54));
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(450, 310); 
		frame.getContentPane().setLayout(null);
		//Fechar Chat
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if(id==0) {
						server.close();
						clientServer.close();
						client.close();
					}
					else {
						client.close();
						server.close();
						clientServer.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					frmJavazap.setVisible(true);
					chat.setText("");
				}
			}
		});
		
		//Botao enviar mensagem
		JButton enviar = new JButton("->");
		enviar.setFont(new Font("Dialog", Font.BOLD, 12));
		enviar.setBackground(new Color(78, 154, 6));
		enviar.setBounds(337, 239, 54, 25);
		frame.getContentPane().add(enviar);
		enviar.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				String str = mensagem.getText().trim();
		    	if(!str.isEmpty()) {
		    		chat.append("You: "+str+"\n");
		    		mensagem.setText("");
		    		try {
		    			PrintStream out;
		    			out = new PrintStream(client.getOutputStream());
		    			out.println(str);
		    		} catch (IOException e1) {
		    			e1.printStackTrace();
		    		}
		    	}
			}
		});
		//caixa Enviar mensagem
		mensagem = new JTextField();
		mensagem.setBackground(new Color(238, 238, 236));
		mensagem.setBounds(39, 239, 286, 25);
		frame.getContentPane().add(mensagem);
		mensagem.setColumns(10);
		mensagem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode()==KeyEvent.VK_ENTER){
			    	String str = mensagem.getText().trim();
			    	if(!str.isEmpty()) {
			    		chat.append("You: "+str+"\n");
			    		mensagem.setText("");
			    		try {
			    			PrintStream out;
			    			out = new PrintStream(client.getOutputStream());
			    			out.println(str);
			    		} catch (IOException e1) {
			    			e1.printStackTrace();
			    		}
			    	}
			    }
			}
		});
		/*
		 * Area do chat
		 */
		chat = new JTextArea();
		chat.setEditable(false);
		scrollChat = new JScrollPane(chat);
		scrollChat.setForeground(new Color(128, 128, 64));
		scrollChat.setBackground(new Color(211, 215, 207));
		scrollChat.setBounds(39, 12, 352, 215);
		frame.getContentPane().add(scrollChat);
	}
}
