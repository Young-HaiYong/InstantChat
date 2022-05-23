package com.yhy.chat.view;

import com.yhy.chat.main.ChatClient;

import javax.swing.*;
import java.awt.*;
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
        super("InstantChat");
        this.setBackground(new Color(0.5f, 0.0f, 0.0f));
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon(LoginFrame.class.getResource("/com/yhy/chat/view/assets/user.png")).getImage());
        this.setResizable(false);
        init();
        this.setVisible(true);
    }

    public void init() {
        this.setLayout(null);
        this.setBackground(new Color(255, 255, 255));
//      ImageIcon icon = new ImageIcon(LoginFrame.class.getResource("/com/yhy/chat/view/assets/loginfram.png"));
//      icon.setImage(obj.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        ImageIcon image = new ImageIcon(LoginFrame.class.getResource("/com/yhy/chat/view/assets/login.png"));
        image.setImage(image.getImage().getScaledInstance(140,80,Image.SCALE_DEFAULT));
        JLabel lbl_title = new JLabel(image);
        lbl_title.setBounds(72, -10, 200, 100);
        this.add(lbl_title);

        JPanel p = new JPanel();
        p.setBackground(new Color(255, 255, 255));
        p.setBounds(0, 70, FRAME_WIDTH, 90);
        p.setLayout(null);
        JLabel lbl_name = new JLabel("ÓÃ»§Ãû:");
        lbl_name.setBounds(80, 20, 50, 25);
        lbl_name.setFont(new Font("menlo", Font.BOLD, 12));
        p.add(lbl_name);
        txt_name.setBounds(130, 20, 120, 25);
        p.add(txt_name);
        JLabel lbl_password = new JLabel("ÃÜÂë:");
        lbl_password.setFont(new Font("menlo", Font.BOLD, 12));
        lbl_password.setBounds(80, 50, 50, 25);
        p.add(lbl_password);
        txt_password.setBounds(130, 50, 120, 25);
        p.add(txt_password);
        this.add(p);

        p = new JPanel();
        p.setBackground(new Color(255, 255, 255));
        p.add(btn_login);
        p.add(btn_reg);
        p.setBounds(0, 160, FRAME_WIDTH, 100);
        this.add(p);

        btn_login.addActionListener(new Monitor());
        btn_reg.addActionListener(new Monitor());
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
