package com.yhy.chat.view;

import com.yhy.chat.main.ChatClient;
import com.yhy.chat.model.User;
import com.yhy.chat.utils.DateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author: ���
 **/
public class RegFrame extends JFrame {

	public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static final int FRAME_WIDTH = 400;
	public static final int FRAME_HEIGHT = 400;

	public static final String HEAD1_PATH = "pictures\\h1.png";
	public static final String HEAD2_PATH = "pictures\\h2.png";
	public static final String HEAD3_PATH = "pictures\\h3.png";
	public static final String HEAD4_PATH = "pictures\\h4.png";

	private JTextField txt_name = new JTextField();
	private JPasswordField txt_password1 = new JPasswordField();
	private JPasswordField txt_password2 = new JPasswordField();
	private JTextField txt_age = new JTextField();
	private DateChooser txt_birthday;
	private JComboBox com_sex = new JComboBox(new String[] { "��", "Ů" });
	private JRadioButton btn_head1 = new JRadioButton();
	private JRadioButton btn_head2 = new JRadioButton();
	private JRadioButton btn_head3 = new JRadioButton();
	private JRadioButton btn_head4 = new JRadioButton();
	private JRadioButton[] btn_head = new JRadioButton[] { btn_head1,
			btn_head2, btn_head3, btn_head4 };
	private JButton btn_submit = new JButton("ȷ��");
	private JButton btn_cancel = new JButton("ȡ��");
	
	
//ע�����
	public RegFrame() {
		super("�û�ע��");
		this.setIconImage(new ImageIcon(RegFrame.class.getResource("/com/yhy/chat/view/assets/user.png")).getImage());
		this.setLayout(null);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		init();
		this.repaint();
		this.validate();
	}

	public void init() {
		ButtonGroup bg = new ButtonGroup();
		for (JRadioButton b : btn_head) {
			bg.add(b);
		}

		JLabel l = new JLabel("ͷ��");
		l.setBounds(10, 10, 50, 30);
		this.add(l);

		this.setRadioBtn(btn_head1, HEAD1_PATH);
		btn_head1.setBounds(10, 40, 50, 50);
		btn_head1.setSelected(true);
		this.add(btn_head1);

		this.setRadioBtn(btn_head2, HEAD2_PATH);
		btn_head2.setBounds(60, 40, 50, 50);
		this.add(btn_head2);

		this.setRadioBtn(btn_head3, HEAD3_PATH);
		btn_head3.setBounds(120, 40, 50, 50);
		this.add(btn_head3);

		this.setRadioBtn(btn_head4, HEAD4_PATH);
		btn_head4.setBounds(180, 40, 50, 50);
		this.add(btn_head4);

		l = new JLabel("�û���");
		l.setBounds(10, 100, 50, 30);
		this.add(l);

		txt_name.setBounds(60, 100, 120, 30);
		this.add(txt_name);

		l = new JLabel("����");
		l.setBounds(10, 140, 50, 30);
		this.add(l);

		txt_password1.setBounds(60, 140, 120, 30);
		this.add(txt_password1);

		l = new JLabel("ȷ������");
		l.setBounds(190, 140, 50, 30);
		this.add(l);

		txt_password2.setBounds(250, 140, 120, 30);
		this.add(txt_password2);

		l = new JLabel("�Ա�");
		l.setBounds(10, 180, 50, 30);
		this.add(l);

		com_sex.setBounds(60, 180, 50, 30);
		com_sex.setSelectedIndex(0);
		this.add(com_sex);

		l = new JLabel("����");
		l.setBounds(150, 180, 60, 30);
		this.add(l);

		txt_age.setBounds(200, 180, 50, 30);
		this.add(txt_age);

		l = new JLabel("��������");
		l.setBounds(10, 240, 50, 30);
		this.add(l);

		txt_birthday = new DateChooser(new Date());
		txt_birthday.setBounds(60, 240, 150, 30);
		this.add(txt_birthday);

		
		
		btn_submit.setBounds(100, 290, 60, 30);
		btn_submit.addActionListener(new Monitor());
		this.add(btn_submit);

		btn_cancel.setBounds(180, 290, 60, 30);
		btn_cancel.addActionListener(new Monitor());
		this.add(btn_cancel);

	}

	class Monitor implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btn_submit) {
				if (new String(txt_password1.getPassword()).equals("")
						|| new String(txt_password2.getPassword()).equals("")) {
					JOptionPane.showMessageDialog(null, "���벻��Ϊ��", "����",
							JOptionPane.ERROR_MESSAGE);
					txt_password1.setText("");
					txt_password2.setText("");
					return;
				}
				if (!new String(txt_password1.getPassword()).equals(new String(
						txt_password2.getPassword()))) {
					JOptionPane.showMessageDialog(null, "�����벻һ�£�����������");
					txt_password1.setText("");
					txt_password2.setText("");
					return;
				}
				User user = new User();
				user.setName(txt_name.getText());
				user.setPassword(new String(txt_password1.getPassword()));
				try {
					user.setAge(Integer.parseInt(txt_age.getText()));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "��������ȷ����", "����",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				user.setSex(com_sex.getSelectedIndex());
				user.setBirthday(FORMAT.format(txt_birthday.getDate()));
				if (btn_head1.isSelected()) {
					user.setImgPath(HEAD1_PATH);
				} else if (btn_head2.isSelected()) {
					user.setImgPath(HEAD2_PATH);
				} else if (btn_head3.isSelected()) {
					user.setImgPath(HEAD3_PATH);
				} else if (btn_head4.isSelected()) {
					user.setImgPath(HEAD4_PATH);
				}
				if (ChatClient.getInstance().getNetClient().reg(user)) {
					JOptionPane.showMessageDialog(null, "ע��ɹ�");
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "ע��ʧ�ܣ��û����ظ�");
				}
			} else if (e.getSource() == btn_cancel) {
				dispose();
			}
		}
	}

	public void setRadioBtn(JRadioButton btn, String path) {
		ImageIcon img = new ImageIcon(path);
		img.setImage(img.getImage().getScaledInstance(40, 40,
				Image.SCALE_DEFAULT));
		btn.setIcon(img);
		img = new ImageIcon(path);
		img.setImage(img.getImage().getScaledInstance(40, 40,
				Image.SCALE_DEFAULT));
		Image img2 = this.createImage(40, 40);
		img2.getGraphics().drawImage(img.getImage(), 0, 0, 40, 40, null);
		img2.getGraphics().setColor(Color.YELLOW);
		for (int i = 0; i < 2; i++) {
			img2.getGraphics().drawRect(i, i, 39 - 2 * i, 39 - 2 * i);
		}
		btn.setSelectedIcon(new ImageIcon(img2));
		btn.setPressedIcon(new ImageIcon(img2));
		btn.setRolloverIcon(new ImageIcon(img2));
		btn.setFocusPainted(false);
	}
}
