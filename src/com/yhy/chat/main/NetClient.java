package com.yhy.chat.main;

import com.yhy.chat.model.User;
import com.yhy.chat.model.msg.Msg;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClient {

	public static final int LOGIN_MSG = 1;
	public static final int REG_MSG = 2;

	private int tcpPort;
	private String ip;
	private int udpPort;
	private Socket socket;
	private ChatClient client;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public NetClient(ChatClient client, String ip, int tcpPort) {
		this.client = client;
		this.ip = ip;
		this.tcpPort = tcpPort;
	}

	public boolean connect(String name, String password) {
		boolean success = false;
		try {
			socket = new Socket(ip, tcpPort);
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			ObjectInputStream ois = new ObjectInputStream(is);
			os.write(LOGIN_MSG);
			os.flush();
			dos.writeUTF(name);
			dos.writeUTF(password);
			dos.flush();
			Msg msg = (Msg) ois.readObject();
			if (msg.getUser() != null) {
				success = true;
			}
			msg.process(client);
			if (success) {
				this.oos = new ObjectOutputStream(os);
				this.ois = new ObjectInputStream(is);
				new Thread(new TCPThread()).start();
			}
		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(null, "δ���ӵ�����������������");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return success;
	}

	public boolean reg(User user) {
		try {
			socket = new Socket(ip, tcpPort);
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			os.write(REG_MSG);
			os.flush();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			ObjectInputStream ois = new ObjectInputStream(is);
			oos.writeObject(user);
			oos.flush();
			boolean success = ois.readBoolean();
			return success;
		} catch (ConnectException e) {
			JOptionPane.showMessageDialog(null, "δ���ӵ�����������������");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				socket = null;
			}
		}
		return false;
	}

	public void send(Msg msg) {
		try {
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class TCPThread implements Runnable {

		public void run() {
			try {
				while (true) {
					Msg msg = (Msg) ois.readObject();
					msg.process(client);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "�ѶϿ��������������");
				System.exit(0);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
}
