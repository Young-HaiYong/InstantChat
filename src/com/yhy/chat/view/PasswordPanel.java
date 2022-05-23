package com.yhy.chat.view;

import com.yhy.chat.main.ChatClient;
import com.yhy.chat.model.User;
import com.yhy.chat.model.msg.UpdateUserMsg;
import com.yhy.chat.utils.MyFont;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author: ���
 **/
public class PasswordPanel extends JPanel {

	private User user;
	private UserInfoFrame frame;

	private JPasswordField txt_old = new JPasswordField();
	private JPasswordField txt_new1 = new JPasswordField();
	private JPasswordField txt_new2 = new JPasswordField();
	private JButton btn_submit = new JButton("ȷ��");
	private JButton btn_cancel = new JButton("ȡ��");

	public PasswordPanel(UserInfoFrame frame) {
		this.frame = frame;
		this.user = frame.getUser();
		this.setLayout(null);
		init();
	}
	//�޸����봰��
	public void init() {
		JLabel l = new JLabel("ԭ���룺");
		l.setFont(MyFont.getSonFont());
		l.setBounds(10, 10, 60, 25);
		this.add(l);

		txt_old.setBounds(70, 10, 120, 25);
		this.add(txt_old);

		l = new JLabel("�����룺");
		l.setFont(MyFont.getSonFont());
		l.setBounds(10, 45, 60, 25);
		this.add(l);

		txt_new1.setBounds(70, 45, 120, 25);
		this.add(txt_new1);

		l = new JLabel("ȷ�����룺");
		l.setFont(MyFont.getSonFont());
		l.setBounds(10, 80, 70, 25);
		this.add(l);

		txt_new2.setBounds(70, 80, 120, 25);
		this.add(txt_new2);

		btn_submit.setBounds(100, 290, 60, 30);
		btn_submit.setFont(MyFont.getSonFont());
		btn_submit.addActionListener(new Monitor());
		this.add(btn_submit);

		btn_cancel.setBounds(180, 290, 60, 30);
		btn_cancel.setFont(MyFont.getSonFont());
		btn_cancel.addActionListener(new Monitor());
		this.add(btn_cancel);

	}

	class Monitor implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btn_submit) {
				if (!new String(txt_old.getPassword()).equals(user
						.getPassword())) {
					JOptionPane.showMessageDialog(null, "�����������", "����",
							JOptionPane.ERROR_MESSAGE);
					txt_old.setText("");
					txt_new1.setText("");
					txt_new2.setText("");
					return;
				}
				if (new String(txt_new1.getPassword()).equals("")
						|| new String(txt_new2.getPassword()).equals("")) {
					JOptionPane.showMessageDialog(null, "���벻��Ϊ��", "����",
							JOptionPane.ERROR_MESSAGE);
					txt_old.setText("");
					txt_new1.setText("");
					txt_new2.setText("");
					return;
				}
				if (!new String(txt_new1.getPassword()).equals(new String(
						txt_new2.getPassword()))) {
					JOptionPane.showMessageDialog(null, "���������벻һ��", "����",
							JOptionPane.ERROR_MESSAGE);
					txt_old.setText("");
					txt_new1.setText("");
					txt_new2.setText("");
					return;
				}
				UpdateUserMsg msg = new UpdateUserMsg();
				User u = new User(ChatClient.getInstance().getUser());
				u.setPassword(new String(txt_new1.getPassword()));
				msg.setUser(u);
				msg.send();
				JOptionPane.showMessageDialog(null, "�����޸ĳɹ�");
				frame.dispose();
			} else if (e.getSource() == btn_cancel) {
				frame.dispose();
			}
		}
	}
}
