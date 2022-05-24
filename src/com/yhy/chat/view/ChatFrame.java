package com.yhy.chat.view;


import com.yhy.chat.main.ChatClient;
import com.yhy.chat.model.User;
import com.yhy.chat.model.file.MsgManager;
import com.yhy.chat.model.msg.*;
import com.yhy.chat.utils.MyFont;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: 杨海勇
 * 聊天窗口
 **/
public class ChatFrame extends JFrame {

    public static final int FRAME_WIDTH = 500;
    public static final int FRAME_HEIGHT = 480;
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    private File file;
    private FileOutputStream fos;
    private long length;
    private User user;
    private User targetUser;
    private JTextPane txt_input = new JTextPane() {
        @SuppressWarnings("unchecked")
        public void paste() {
            if (isEditable() && isEnabled()) {
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable contents = cb.getContents(null);
                if (contents != null
                        && contents
                        .isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    super.paste();
                } else if (contents != null
                        && contents
                        .isDataFlavorSupported(DataFlavor.imageFlavor)) {
                    try {
                        Image img = (Image) contents
                                .getTransferData(DataFlavor.imageFlavor);
                        Icon icon = new ImageIcon(img);
                        insertIcon(icon);
                    } catch (UnsupportedFlavorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (contents != null
                        && contents
                        .isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        if (!targetUser.isLogin()) {
                            JOptionPane.showMessageDialog(null, "离线用户无法发送文件",
                                    "错误", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (file != null) {
                            JOptionPane.showMessageDialog(null,
                                    "正在发送文件，不允许同时发送", "错误",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        List<File> file = (List<File>) contents
                                .getTransferData(DataFlavor.javaFileListFlavor);
                        SendFileMsg msg = new SendFileMsg();
                        msg.setUser(user);
                        msg.setTargetUser(targetUser);
                        msg.setFile(file.get(0));
                        msg.send();
                    } catch (UnsupportedFlavorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    private JTextPane txt_display = new JTextPane();
    private JComboBox com_size = new JComboBox(new String[]{"10", "11", "12",
            "14", "16", "20", "24", "32", "36"});
    private JButton btn_sendFile = new JButton("发送文件");
    private JButton btn_acceptFile = new JButton("接收文件");
    private JButton btn_refuseFile = new JButton("拒绝文件");
    private JButton btn_chattingRecords = new JButton("聊天记录");
    private JLabel lbl_head = new JLabel();
    private JLabel lbl_name = new JLabel();
    private JLabel lbl_file = new JLabel() {
        public void setText(String text) {
            int length = 13;
            for (int i = length - 6; i < text.length(); i += length) {
                text = text.substring(0, i + 1) + "<br/>"
                        + text.substring(i + 1);
            }
            text = "<html>" + text + "</html>";
            super.setText(text);
        }
    };
    private JProgressBar fileBar = new JProgressBar();
    private ArrayList<ChatMsg> msgs = new ArrayList<>();

    private Color color = Color.BLACK;

    public ChatFrame(User user, User targetUser) {
        super(user.getName()+ "  正在与  " + (targetUser == null ? "大家" : targetUser.getName())
                + " 聊天");
        this.user = user;
        this.targetUser = targetUser;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //保存文件逻辑实现
                try {
                    MsgManager.saveMsg(user, targetUser, msgs);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                for (int i = 0; i < ChatClient.getInstance().getChatFrameList()
                        .size(); i++) {
                    if (ChatClient.getInstance().getChatFrameList().get(i) == ChatFrame.this) {
                        ChatClient.getInstance().getChatFrameList().remove(i);
                        break;
                    }
                }
                ChatFrame.this.dispose();
            }
        });

        this.setIconImage(new ImageIcon(ChatFrame.class.getResource("/com/yhy/chat/view/assets/chat.png")).getImage());
        this.setLocation(150, 100);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setResizable(false);
        init();
        this.setVisible(true);
    }

    //聊天界面初始化
    public void init() {

        this.setLayout(null);
        JScrollPane sp = new JScrollPane(txt_input);
        sp.setBounds(20, 330, 350, 100);
        this.add(sp);

        //获取好友信息
        if (targetUser != null) {
            this.setTargetUser(targetUser);
            lbl_head.setBounds(20, 10, 40, 40);
            this.add(lbl_head);

            lbl_name.setText(targetUser.getName());
            lbl_name.setBounds(70, 10, 100, 30);
            lbl_name.setFont(new Font("menlo", Font.BOLD, 18));
            this.add(lbl_name);
        }

        sp = new JScrollPane(txt_display,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        txt_display.setEditable(false);
        sp.setBounds(20, 50, 350, 250);
        this.add(sp);


        //按钮功能-字号大小
        Monitor m = new Monitor();

        com_size.setBounds(20, 305, 50, 20);
        com_size.setFont(new Font("menlo", Font.BOLD, 13));
        com_size.setSelectedIndex(5);
        com_size.addActionListener(m);
        this.add(com_size);

        //聊天记录
        btn_chattingRecords.setBounds(380, 90, 90, 30);
        btn_chattingRecords.setFont(new Font("menlo", Font.BOLD, 12));
        btn_chattingRecords.addActionListener(m);
        this.add(btn_chattingRecords);
        //发送文件功能
        btn_sendFile.setBounds(380, 130, 90, 30);
        btn_sendFile.setFont(new Font("menlo", Font.BOLD, 12));
        btn_sendFile.addActionListener(m);
        if (targetUser != null) {
            this.add(btn_sendFile);
        }
        //接受文件
        btn_acceptFile.setBounds(380, 170, 90, 30);
        btn_acceptFile.setFont(new Font("menlo", Font.BOLD, 12));
        btn_acceptFile.addActionListener(m);
        btn_acceptFile.setVisible(false);
        this.add(btn_acceptFile);
        //拒绝接收文件
        btn_refuseFile.setBounds(380, 210, 90, 30);
        btn_refuseFile.setFont(new Font("menlo", Font.BOLD, 12));
        btn_refuseFile.addActionListener(m);
        btn_refuseFile.setVisible(false);
        this.add(btn_refuseFile);

        lbl_file.setBounds(390, 200, 600, 190);
        this.add(lbl_file);

        fileBar.setVisible(false);
        fileBar.setBounds(390, 400, 100, 20);
        this.add(fileBar);

        txt_input.addKeyListener(new KeyMonitor());
    }

    //监控发送类型按钮
    class Monitor implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SimpleAttributeSet sas = new SimpleAttributeSet();
            txt_input.setCaretPosition(txt_input.getStyledDocument()
                    .getLength());
            if (e.getSource() == com_size) {
                StyleConstants.setFontSize(sas, Integer.parseInt(com_size
                        .getSelectedItem().toString()));
            } else if (e.getSource() == btn_sendFile) {
                if (!targetUser.isLogin()) {
                    JOptionPane.showMessageDialog(null, "不允许向离线用户发送文件", "错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (file != null) {
                    JOptionPane.showMessageDialog(null, "正在发送文件，不允许同时发送", "错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    if (!new File(chooser.getSelectedFile().getAbsolutePath())
                            .exists()) {
                        JOptionPane.showMessageDialog(null, "文件不存在", "错误",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    SendFileMsg msg = new SendFileMsg();
                    msg.setUser(user);
                    msg.setTargetUser(targetUser);
                    msg.setFile(chooser.getSelectedFile());
                    msg.send();
                }
                return;
            } else if (e.getSource() == btn_acceptFile) {
                if (file != null) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setSelectedFile(new File(file.getName()));
                    if (chooser.showDialog(null, "保存") == JFileChooser.APPROVE_OPTION) {
                        if (chooser.getSelectedFile().exists()) {
                            chooser.getSelectedFile().delete();
                        }
                        try {
                            chooser.getSelectedFile().createNewFile();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        AcceptFileMsg msg = new AcceptFileMsg();
                        msg.setSrcFile(file);
                        msg.setDestFile(chooser.getSelectedFile());
                        msg.setUser(user);
                        msg.setTargetUser(targetUser);
                        msg.send();
                    }
                }
            } else if (e.getSource() == btn_refuseFile) {
                if (file != null) {
                    RefuseFileMsg msg = new RefuseFileMsg();
                    msg.setFile(file);
                    msg.setUser(user);
                    msg.setTargetUser(targetUser);
                    msg.send();
                }
            } else if (e.getSource() == btn_chattingRecords) {
                //阅读聊天记录实现
                try {
                    ChatRecordFrame chatRecordFrame = new ChatRecordFrame(user,targetUser);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            txt_input.getStyledDocument().setCharacterAttributes(0,
                    txt_input.getStyledDocument().getLength() + 1, sas, false);
            txt_input.setCharacterAttributes(sas, false);
        }
    }

    //监控文本消息输入
    class KeyMonitor extends KeyAdapter {
        int num = 0;

        public void keyPressed(KeyEvent e) {
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                try {
                    txt_input.getStyledDocument().insertString(
                            txt_input.getCaretPosition(), "\n", null);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (txt_input.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "不允许发送空消息", "错误",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Date date = new Date();
                    ChatMsg msg = new ChatMsg();
                    msg.setDoc(txt_input.getStyledDocument());
                    msg.setUser(user);
                    msg.setTargetUser(targetUser);
                    msg.setDate(date);
                    msg.send();
                    msgs.add(msg);
                }
            } else if (e.isControlDown() && e.isAltDown()
                    && e.getKeyCode() == KeyEvent.VK_A) {
                JFrame.setDefaultLookAndFeelDecorated(false);
                new ScreenWindow();
                JFrame.setDefaultLookAndFeelDecorated(true);
            }
        }

        public void keyReleased(KeyEvent e) {
            if (!e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                txt_input.setText("");
            }
        }
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
        if (targetUser != null) {
            ImageIcon img = new ImageIcon(targetUser.getImgPath());
            if (!targetUser.isLogin()) {
                BufferedImage bi = null;
                try {
                    bi = ImageIO.read(new File(targetUser.getImgPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bi = new ColorConvertOp(ColorSpace
                        .getInstance(ColorSpace.CS_GRAY), null)
                        .filter(bi, null);
                img.setImage(bi);
            }
            img.setImage(img.getImage().getScaledInstance(40, 40,
                    Image.SCALE_DEFAULT));
            lbl_head.setIcon(img);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JTextPane getTxt_display() {
        return txt_display;
    }

    public void setTxt_display(JTextPane txtDisplay) {
        txt_display = txtDisplay;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ChatFrame)) {
            return false;
        }
        ChatFrame cf = (ChatFrame) o;
        if (cf.targetUser == null && targetUser == null) {
            return cf.user.equals(user);
        } else if (cf.targetUser == null || targetUser == null) {
            return false;
        }
        return cf.user.equals(user) && cf.targetUser.equals(targetUser);
    }

    public JButton getBtn_chattingRecords() {
        return btn_chattingRecords;
    }

    public void setBtn_chattingRecords(JButton btn_chattingRecords) {
        this.btn_chattingRecords = btn_chattingRecords;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public JButton getBtn_acceptFile() {
        return btn_acceptFile;
    }

    public void setBtn_acceptFile(JButton btnAcceptFile) {
        btn_acceptFile = btnAcceptFile;
    }

    public JButton getBtn_refuseFile() {
        return btn_refuseFile;
    }

    public void setBtn_refuseFile(JButton btnRefuseFile) {
        btn_refuseFile = btnRefuseFile;
    }

    public JLabel getLbl_file() {
        return lbl_file;
    }

    public void setLbl_file(JLabel lblFile) {
        lbl_file = lblFile;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public FileOutputStream getFos() {
        return fos;
    }

    public void setFos(FileOutputStream fos) {
        this.fos = fos;
    }

    public JProgressBar getFileBar() {
        return fileBar;
    }

    public void setFileBar(JProgressBar fileBar) {
        this.fileBar = fileBar;
    }
}
