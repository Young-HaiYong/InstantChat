package com.yhy.chat.view;

import com.yhy.chat.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * @author: 杨海勇
 **/
public class PopupFrame {
	protected static Timer Tims0;
	static JFrame frame;
	static float value1 = 1.0f;
	boolean isClicked = false;

	public PopupFrame(User user) {
		frame = new JFrame();
		JPanel jp = new JPanel();
		frame.add(jp);
		frame.setSize(250, 180);
		frame.setAlwaysOnTop(true);
		frame.setLocation(
				Toolkit.getDefaultToolkit().getScreenSize().width - 250,
				Toolkit.getDefaultToolkit().getScreenSize().height - 220);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ImageIcon img = new ImageIcon(user.getImgPath());
		img.setImage(img.getImage().getScaledInstance(40, 40,
				Image.SCALE_DEFAULT));
		JLabel lbl_head = new JLabel(img);
		lbl_head.setBounds(20, 20, 40, 40);
		frame.add(lbl_head);

		JLabel lbl_name = new JLabel(user.getName() + " 上线了");
		lbl_name.setBounds(70, 20, 100, 30);
		frame.add(lbl_name);

		frame.setVisible(true); // 添加按钮的监听事件
		Tims0 = new Timer(100, new Tim1());
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				close();
			}
		}).start();
	}

	public void close() {
		Tims0.start();
		isClicked = true; // 当鼠标移入窗口时，中止线程,当点击了“淡出”按钮后添加事件监听代码
		if (isClicked == true) {
			frame.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					Tims0.stop();
					value1 = 1.0f;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							com.sun.awt.AWTUtilities.setWindowOpacity(frame,
									value1);
						}
					});
				}

				public void mouseExited(MouseEvent e) {
					Tims0.start();
				}
			});
		}
	}

	public static class Tim1 implements ActionListener {// 控制窗口关闭时透明度的渐变
		public void actionPerformed(ActionEvent e) {
			value1 -= 0.02f;
			if (value1 >= 0.02f) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						com.sun.awt.AWTUtilities.setWindowOpacity(frame, value1);
					}
				});
			} else {
				frame.dispose();
			}
		}
	}
}
