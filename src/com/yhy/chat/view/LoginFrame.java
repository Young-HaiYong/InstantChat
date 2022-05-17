package com.yhy.chat.view;

import com.yhy.chat.main.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author: 杨海勇
 **/
public class LoginFrame extends JFrame {

    public static final int FRAME_WIDTH = 350;
    public static final int FRAME_HEIGHT = 250;


    private JTextField txt_name = new JTextField(10);
    private JPasswordField txt_password = new JPasswordField(10);
    private JButton btn_login = new JButton("登录");

    public LoginFrame() {
        super("InstantChat");

        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();

        //this.setBounds((d.width - d.width / 3) / 2, (d.height - d.height / 3) / 2, 510, 380);
        this.setBackground(new Color(0.5f,0.0f,0.0f));
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon(LoginFrame.class.getResource("/com/yhy/chat/view/assets/icon.jpg")).getImage());
        this.setResizable(false);
        init();
        this.setVisible(true);
    }

    public void init() {
        this.setLayout(null);
//        JLabel lbl_title = new JLabel(new ImageIcon(LoginFrame.class.getResource("/com/yhy/chat/view/assets/logo.png")));
//        lbl_title.setBounds(100, 20, 200, 100);
//        this.add(lbl_title);

        JPanel p = new JPanel();
        p.setBackground(new Color(255, 255, 255));
        p.setBounds(0, 70, FRAME_WIDTH, 90);
        p.setLayout(null);
        JLabel lbl_name = new JLabel("用户名:");
        lbl_name.setBounds(80, 20, 50, 25);
        lbl_name.setFont(new Font("menlo", Font.BOLD, 12));
        p.add(lbl_name);
        txt_name.setBounds(140, 20, 120, 25);
        p.add(txt_name);
        JLabel lbl_password = new JLabel("密码:");
        lbl_password.setFont(new Font("menlo", Font.BOLD, 12));
        lbl_password.setBounds(80, 50, 50, 25);
        p.add(lbl_password);
        txt_password.setBounds(140, 50, 120, 25);
        p.add(txt_password);
        this.add(p);

        p = new JPanel();
        p.setBackground(new Color(255, 255, 255));
        p.add(btn_login);
        p.setBounds(0, 160, FRAME_WIDTH, 100);
        this.add(p);

        btn_login.addActionListener(new Monitor());
        txt_password.addActionListener(new Monitor());
        txt_name.addActionListener(new Monitor());
    }

    class Monitor implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btn_login) {
                ChatClient.getInstance().getNetClient().connect(
                        txt_name.getText(),
                        new String(txt_password.getPassword()));
            }
        }
    }
}
