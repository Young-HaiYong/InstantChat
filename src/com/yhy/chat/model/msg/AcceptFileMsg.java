package com.yhy.chat.model.msg;

import com.yhy.chat.main.ChatClient;
import com.yhy.chat.main.Server;
import com.yhy.chat.view.ChatFrame;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class AcceptFileMsg extends Msg {

	private File srcFile, destFile;
	private ChatFrame cf = null;
	private long finished = 0;

	@Override
	public void process(ChatClient client) {
		SimpleAttributeSet sas = new SimpleAttributeSet();
		StyleConstants.setForeground(sas, Color.BLUE);
		if (client.getUser().equals(user)) {
			for (ChatFrame c : client.getChatFrameList()) {
				if (c.getTargetUser().equals(targetUser)) {
					cf = c;
					break;
				}
			}
			try {
				if (cf.getFile().equals(srcFile)) {
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(destFile);
						cf.setFos(fos);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					cf.getTxt_display().getStyledDocument()
							.insertString(
									cf.getTxt_display().getStyledDocument()
											.getLength(),
									"��ʼ�����ļ� " + srcFile.getName() + " ��С:"
											+ srcFile.length() / 1024L + "k\n",
									sas);
					cf.setLength(0);
					new Thread(new Runnable() {
						@Override
						public void run() {
							int second = 0;
							cf.getFileBar().setVisible(true);
							cf.getFileBar().setMaximum((int) srcFile.length());
							cf.getFileBar().setValue(0);
							while (cf.getLength() >= 0) {
								cf.getLbl_file().setText(
										srcFile.getName()
												+ "���ѽ��գ�"
												+ cf.getLength()
												/ 1024L
												+ "k������"
												+ srcFile.length()
												/ 1024L
												+ "k\n���ٶȣ�"
												+ (second > 0 ? cf.getLength()
														/ 1024L / second : "0")
												+ "k/s");
								cf.getFileBar().setValue((int) cf.getLength());
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								second++;
							}
							SimpleAttributeSet sas = new SimpleAttributeSet();
							StyleConstants.setForeground(sas, Color.BLUE);
							try {
								cf.getTxt_display().getStyledDocument()
										.insertString(
												cf.getTxt_display()
														.getStyledDocument()
														.getLength(), "���ճɹ�\n",
												sas);
								JButton btn_open = new JButton("���ļ�");
								btn_open
										.addActionListener(new ActionListener() {
											@Override
											public void actionPerformed(
													ActionEvent e) {
												String command = "cmd /c start "
														+ destFile
																.getAbsolutePath();
												try {
													Runtime.getRuntime().exec(
															command);
												} catch (IOException e1) {
													e1.printStackTrace();
												}
											}
										});
								cf.getTxt_display().setCaretPosition(
										cf.getTxt_display().getStyledDocument()
												.getLength());
								cf.getTxt_display().insertComponent(btn_open);
								cf.getTxt_display().getStyledDocument()
										.insertString(
												cf.getTxt_display()
														.getStyledDocument()
														.getLength(), "\n\n",
												sas);
								cf.getTxt_display().setCaretPosition(
										cf.getTxt_display().getStyledDocument()
												.getLength());
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
							try {
								cf.getFos().flush();
								cf.getFos().close();
								cf.setFos(null);
							} catch (IOException e) {
								e.printStackTrace();
							}
							cf.getLbl_file().setText("");
							cf.setFile(null);
							cf.getFileBar().setVisible(false);
						}
					}).start();
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		} else if (client.getUser().equals(targetUser)) {
			for (ChatFrame c : client.getChatFrameList()) {
				if (c.getTargetUser().equals(user)) {
					cf = c;
					break;
				}
			}
			try {
				if (cf.getFile().equals(srcFile)) {
					cf.getTxt_display().getStyledDocument()
							.insertString(
									cf.getTxt_display().getStyledDocument()
											.getLength(),
									"��ʼ�����ļ� " + srcFile.getName() + " ��С:"
											+ srcFile.length() / 1024 + "k\n",
									sas);
					cf.getLbl_file().setText(
							srcFile.getName() + "���ѷ���0k����" + srcFile.length()
									/ 1024 + "k\n");
					new Thread(new Runnable() {
						@Override
						public void run() {
							sendFile();
						}
					}).start();
					new Thread(new Runnable() {
						@Override
						public void run() {
							int second = 0;
							cf.getFileBar().setMaximum((int) srcFile.length());
							cf.getFileBar().setVisible(true);
							cf.getFileBar().setValue(0);
							while (finished >= 0) {
								cf.getLbl_file().setText(
										srcFile.getName()
												+ "���ѷ��ͣ�"
												+ finished
												/ 1024L
												+ "k������"
												+ srcFile.length()
												/ 1024L
												+ "k\n���ٶȣ�"
												+ (second > 0 ? finished
														/ 1024L / second : "0")
												+ "k/s");
								cf.getFileBar().setValue((int) finished);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								second++;
							}
							SimpleAttributeSet sas = new SimpleAttributeSet();
							StyleConstants.setForeground(sas, Color.BLUE);
							try {
								cf.getTxt_display().getStyledDocument()
										.insertString(
												cf.getTxt_display()
														.getStyledDocument()
														.getLength(), "���ͳɹ�\n",
												sas);
								cf.getTxt_display().setCaretPosition(
										cf.getTxt_display().getStyledDocument()
												.getLength());
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
							cf.getLbl_file().setText("");
							cf.setFile(null);
							cf.getFileBar().setVisible(false);
						}
					}).start();
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		cf.getBtn_acceptFile().setVisible(false);
		cf.getBtn_refuseFile().setVisible(false);
	}

	public void sendFile() {
		FileInputStream bis = null;
		byte[] buf = new byte[1024];
		try {
			bis = new FileInputStream(srcFile);
			int length;
			while ((length = bis.read(buf)) != -1) {
				FileMsg msg = new FileMsg();
				msg.setUser(cf.getUser());
				msg.setTargetUser(cf.getTargetUser());
				msg.setSrcFile(srcFile);
				msg.setDestFile(destFile);
				msg.setBuffer(buf);
				msg.setLength(length);
				msg.send();
				finished += length;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			FileMsg msg = new FileMsg();
			msg.setUser(cf.getUser());
			msg.setTargetUser(cf.getTargetUser());
			msg.setSrcFile(srcFile);
			msg.setDestFile(destFile);
			msg.setLength(-1);
			msg.send();
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			finished = -1;
		}
	}

	@Override
	public void process(Server server) {

	}

	public File getSrcFile() {
		return srcFile;
	}

	public void setSrcFile(File srcFile) {
		this.srcFile = srcFile;
	}

	public File getDestFile() {
		return destFile;
	}

	public void setDestFile(File destFile) {
		this.destFile = destFile;
	}

}
