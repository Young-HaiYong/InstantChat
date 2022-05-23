package com.yhy.chat.view;

import com.yhy.chat.main.ChatClient;
import com.yhy.chat.model.User;
import com.yhy.chat.model.msg.AddFriendMsg;
import com.yhy.chat.model.msg.Msg;
import com.yhy.chat.model.msg.RemoveFriendMsg;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author: ���
 * ���ѽ��水ť����ʵ��
 **/
public class UserListPanel extends JPanel {

	private List<User> userList = new ArrayList<User>();
	private List<UserInfoPanel> panelList = new ArrayList<UserInfoPanel>();
	private JLabel lbl_onlineFriend = new JLabel("<���ߺ���>");
	private JLabel lbl_offlineFriend = new JLabel("<���ߺ���>");
	private JLabel lbl_onlinePerson = new JLabel("<���ߵ���>");
	private boolean onlineFriendFlag = true, offlineFriendFlag = true,
			onlinePersonFlag = true;

	public UserListPanel() {
		this.setLayout(new GridLayout(15, 1));
		lbl_onlineFriend.setFont(new Font("menlo",Font.BOLD,18));
		lbl_offlineFriend.setFont(new Font("menlo",Font.BOLD,18));
		lbl_onlinePerson.setFont(new Font("menlo",Font.BOLD,18));
		lbl_onlineFriend.addMouseListener(new MouseMonitor());
		lbl_offlineFriend.addMouseListener(new MouseMonitor());
		lbl_onlinePerson.addMouseListener(new MouseMonitor());
		this.rebuild();
	}

	public void setUser(User user) {
		if (userList.contains(user)) {
			for (UserInfoPanel infoPanel : panelList) {
				if (infoPanel.getUser().equals(user)) {
					infoPanel.setUser(user);
				}
			}
			rebuild();
			return;
		}
		userList.add(user);
		this.setLayout(new GridLayout(Math.max(10, userList.size() + 3), 1));
		UserInfoPanel infoPanel = new UserInfoPanel(user);
		panelList.add(infoPanel);
		this.add(infoPanel);
		this.repaint();
		this.validate();
		rebuild();
	}

	public void rebuild() {
		for (UserInfoPanel panel : panelList) {
			this.remove(panel);
		}
		this.remove(lbl_onlineFriend);
		this.remove(lbl_offlineFriend);
		this.remove(lbl_onlinePerson);

		User user = ChatClient.getInstance().getUser();
		this.add(lbl_onlineFriend);
		if (onlineFriendFlag) {
			for (UserInfoPanel panel : panelList) {
				if (user.getFirends().contains(panel.getUser())
						&& panel.getUser().isLogin()) {
					this.add(panel);
				}
			}
		}
		this.add(lbl_onlinePerson);
		if (onlinePersonFlag) {
			for (UserInfoPanel panel : panelList) {
				if (!user.getFirends().contains(panel.getUser())
						&& panel.getUser().isLogin()) {
					this.add(panel);
				}
			}
		}
		this.add(lbl_offlineFriend);
		if (offlineFriendFlag) {
			for (UserInfoPanel panel : panelList) {
				if (user.getFirends().contains(panel.getUser())
						&& !panel.getUser().isLogin()) {
					this.add(panel);
				}
			}
		}
		this.repaint();
		this.validate();
	}

	public void removeUser(User user) {
		if (!userList.contains(user)) {
			return;
		}
		userList.remove(user);
		for (int i = 0; i < panelList.size(); i++) {
			if (panelList.get(i).getUser().equals(user)) {
				this.remove(panelList.get(i));
				panelList.remove(i);
				this.repaint();
				this.validate();
				break;
			}
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(this.getWidth() - 30, panelList.size() * 35);
	}

	class MouseMonitor extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			if (e.getSource() == lbl_onlineFriend) {
				onlineFriendFlag = !onlineFriendFlag;
			} else if (e.getSource() == lbl_offlineFriend) {
				offlineFriendFlag = !offlineFriendFlag;
			} else if (e.getSource() == lbl_onlinePerson) {
				onlinePersonFlag = !onlinePersonFlag;
			}
			rebuild();
		}

		public void mouseEntered(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			l.setForeground(Color.BLUE);
		}

		public void mouseExited(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			l.setForeground(Color.BLACK);
		}
	}

	class UserInfoPanel extends JPanel {

		public final Color[] COLOR_ARR = new Color[] { Color.RED, Color.YELLOW,
				Color.GREEN, Color.BLUE, Color.BLACK };

		private User user;
		private JLabel lbl_head = new JLabel();
		private JLabel lbl_name = new JLabel();
		private JLabel lbl_sign = new JLabel();
		private JPopupMenu menu = new JPopupMenu();
		private JMenuItem item_addFriend = new JMenuItem("��Ӻ���");
		private JMenuItem item_removeFriend = new JMenuItem("ɾ������");
		private JMenuItem item_info = new JMenuItem("�鿴��Ϣ");

		public UserInfoPanel(User user) {
			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			this.setUser(user);
			lbl_head.setSize(40, 40);
			this.add(lbl_head);
			this.add(lbl_name);
			this.add(lbl_sign);
			this.addMouseListener(new Monitor());

			item_removeFriend.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(null, "ȷ���� "
							+ UserInfoPanel.this.user.getName() + " ɾ�����ѹ�ϵ��",
							"ɾ������", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						Msg msg = new RemoveFriendMsg();
						msg.setUser(ChatClient.getInstance().getUser());
						msg.setTargetUser(UserInfoPanel.this.user);
						msg.send();
					}
				}
			});
			item_addFriend.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String str = JOptionPane.showInputDialog(null, "�����������֤��Ϣ",
							"���,���� "
									+ ChatClient.getInstance().getUser()
											.getName());
					if (str != null) {
						AddFriendMsg msg = new AddFriendMsg();
						msg.setUser(ChatClient.getInstance().getUser());
						msg.setTargetUser(UserInfoPanel.this.user);
						msg.setStr(str);
						msg.send();
					}
				}
			});
			item_info.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new UserInfoFrame(UserInfoPanel.this.user);
				}
			});
			this.add(menu);
		}

		public void setUser(User user) {
			ImageIcon img = new ImageIcon(user.getImgPath());
			if (!user.isLogin()) {
				BufferedImage bi = null;
				try {
					bi = ImageIO.read(new File(user.getImgPath()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				bi = new ColorConvertOp(ColorSpace
						.getInstance(ColorSpace.CS_GRAY), null)
						.filter(bi, null);
				img.setImage(bi);
			}
			img.setImage(img.getImage().getScaledInstance(30, 30,
					Image.SCALE_DEFAULT));
			lbl_head.setIcon(img);
			lbl_name.setText(user.getName());
			lbl_sign.setText(user.getSign().substring(
					0,
					Math.min(28 - user.getName().length(), user.getSign()
							.length())));
			lbl_sign.setForeground(Color.GRAY);
			this.repaint();
			this.validate();
			if ((this.user == null && user.isLogin())
					|| (this.user != null && !this.user.isLogin() && user
							.isLogin())) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						for (Color c : COLOR_ARR) {
							lbl_name.setForeground(c);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
			menu.removeAll();
			menu.add(item_info);
			if (ChatClient.getInstance().getUser().getFirends().contains(user)) {
				menu.add(item_removeFriend);
			} else {
				menu.add(item_addFriend);
			}
			this.user = user;
		}

		public User getUser() {
			return user;
		}

		class Monitor extends MouseAdapter {
			public void mouseClicked(MouseEvent e) {
				if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
					menu.show(UserInfoPanel.this, e.getX(), e.getY());
				} else if (e.getClickCount() == 2) {
					for (ChatFrame c : ChatClient.getInstance()
							.getChatFrameList()) {
						if (user.equals(c.getTargetUser())) {
							return;
						}
					}
					ChatFrame cf = new ChatFrame(ChatClient.getInstance()
							.getUser(), user);
					ChatClient.getInstance().getChatFrameList().add(cf);
				}
			}

			public void mouseEntered(MouseEvent e) {
				UserInfoPanel.this.setBackground(Color.PINK);
			}

			public void mouseExited(MouseEvent e) {
				UserInfoPanel.this.setBackground(null);
			}
		}
	}
}
