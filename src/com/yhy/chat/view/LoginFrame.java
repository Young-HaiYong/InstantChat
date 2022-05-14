package com.yhy.chat.view;

import com.yhy.chat.main.ChatClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author: Ñîº£ÓÂ
 **/
public class LoginFrame extends JFrame {

	public static final int FRAME_WIDTH = 350;
	public static final int FRAME_HEIGHT = 250;

	private JTextField txt_name = new JTextField(10);
	private JPasswordField txt_password = new JPasswordField(10);
	private JButton btn_login = new JButton("µÇÂ¼");
	private JButton btn_reg = new JButton("×¢²á");

	public LoginFrame() {
		super("ÓÃ»§µÇÂ¼");
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		init();
		this.setVisible(true);
	}

	public void init() {
		this.setLayout(null);

		JLabel lbl_title = new JLabel(new ImageIcon("pictures\\title.jpg"));
		lbl_title.setBounds(0, 0, FRAME_WIDTH, 60);
		this.add(lbl_title);

		JPanel p = new JPanel();
		p.setBounds(0, 70, FRAME_WIDTH, 110);
		p.setLayout(null);
		JLabel lbl_name = new JLabel("ÓÃ»§Ãû:");
		lbl_name.setBounds(80, 20, 40, 25);
		p.add(lbl_name);
		txt_name.setBounds(140, 20, 120, 25);
		p.add(txt_name);
		JLabel lbl_password = new JLabel("ÃÜÂë:");
		lbl_password.setBounds(80, 50, 40, 25);
		p.add(lbl_password);
		txt_password.setBounds(140, 50, 120, 25);
		p.add(txt_password);
		this.add(p);

		p = new JPanel();
		p.add(btn_login);
		//p.add(btn_reg);
		p.setBounds(0, 160, FRAME_WIDTH, 100);
		this.add(p);

		btn_login.addActionListener(new Monitor());
		//btn_reg.addActionListener(new Monitor());
		txt_password.addActionListener(new Monitor());
		txt_name.addActionListener(new Monitor());
	}

	class Monitor implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btn_reg) {
				new RegFrame();
			} else {
				ChatClient.getInstance().getNetClient().connect(
						txt_name.getText(),
						new String(txt_password.getPassword()));
			}
		}
	}
}
