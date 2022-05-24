package com.yhy.chat.view;

import com.yhy.chat.model.User;
import com.yhy.chat.model.file.FileFolder;
import com.yhy.chat.model.file.MsgManager;
import com.yhy.chat.model.msg.ChatMsg;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.yhy.chat.model.msg.ChatMsg.getAllElements;

/**
 * @author: 杨海勇
 * 聊天记录窗口
 **/
public class ChatRecordFrame extends JFrame {

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    public static final int FRAME_WIDTH = 500;
    public static final int FRAME_HEIGHT = 480;

    private User user;
    private User targetUser;
    private ArrayList<ChatMsg> userMsg = new ArrayList<>();
    private ArrayList<ChatMsg> targetUserMsg = new ArrayList<>();
    private ArrayList<ChatMsg> allMsg = new ArrayList<>();
    private JButton btn_delete = new JButton("删除记录");
    private JTextPane txt_display = new JTextPane();

    public ChatRecordFrame(User user, User targetUser) throws Exception {
        super("聊天记录 " + "用户:" + user.getName() + "与" + "用户:" + targetUser.getName());
        this.user = user;
        this.targetUser = targetUser;
        showChat();
        this.setIconImage(new ImageIcon(ChatRecordFrame.class.getResource("/com/yhy/chat/view/assets/chatrecord.png")).getImage());
        this.setLocation(150, 100);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setResizable(false);
    }

    public void showChat() throws BadLocationException, IOException {

        SimpleAttributeSet sas3 = new SimpleAttributeSet();
        StyleConstants.setForeground(sas3, Color.GRAY);
        //获取聊天信息记录对象
        userMsg.addAll(MsgManager.readMsg(user, targetUser));
        targetUserMsg.addAll(MsgManager.readMsg(targetUser, user));
        allMsg.addAll(userMsg);
        allMsg.addAll(targetUserMsg);

        //界面
        this.setLayout(null);
        JScrollPane sp = new JScrollPane(txt_display,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        txt_display.setEditable(false);
        sp.setBounds(20, 40, 350, 320);
        this.add(sp);
        btn_delete.setBounds(380, 90, 100, 30);
        btn_delete.setFont(new Font("menlo", Font.BOLD, 12));
        btn_delete.addActionListener(new Moniter());
        this.add(btn_delete);

        //处理聊天消息


        if (allMsg.isEmpty() && targetUserMsg.isEmpty() && userMsg.isEmpty()) {
            this.getTxt_display().getStyledDocument().insertString(0, "聊天记录为空", sas3);
        } else {
            arrayListSort(allMsg);
            for (ChatMsg msg : allMsg) {
                process(msg);
            }

        }
//        Container container = this.getContentPane();
//        container.add(sp);
        this.setIconImage(new ImageIcon(ChatRecordFrame.class.getResource("/com/yhy/chat/view/assets/user.png")).getImage());
        this.setVisible(true);
        this.setSize(500, 480);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    }

    //解包消息
    public void process(ChatMsg msg) {
        Date date = msg.getDate();
        StyledDocument doc;
        doc = msg.getDoc();
        List<Element> list = getAllElements(doc.getRootElements());
        List<Icon> iconList = new ArrayList<Icon>();
        SimpleAttributeSet sas = new SimpleAttributeSet();
        SimpleAttributeSet sas2 = new SimpleAttributeSet();
        StyleConstants.setForeground(sas2, Color.GRAY);
        for (int i = 0; i < list.size(); i++) {
            Element e = list.get(i);
            if (e.getName().equals("icon")) {
                Icon icon = StyleConstants.getIcon(e.getAttributes());
                iconList.add(icon);
            } else if (e.getName().equals("content")) {
                StyleConstants.setFontSize(sas, StyleConstants.getFontSize(e
                        .getAttributes()));
                StyleConstants.setForeground(sas, StyleConstants
                        .getForeground(e.getAttributes()));
                StyleConstants.setBold(sas, StyleConstants.isBold(e
                        .getAttributes()));
                StyleConstants.setItalic(sas, StyleConstants.isItalic(e
                        .getAttributes()));
                StyleConstants.setUnderline(sas, StyleConstants.isUnderline(e
                        .getAttributes()));
            }
        }
        try {
            this.getTxt_display().getStyledDocument().insertString(
                    this.getTxt_display().getStyledDocument().getLength(),
                    msg.getUser().getName() + " 对 " + msg.getTargetUser().getName() + " " + FORMAT.format(date) + ":\n", sas2);
            for (int i = 0; i < doc.getText(0, doc.getLength()).length(); i++) {
                this.getTxt_display().setCaretPosition(
                        this.getTxt_display().getStyledDocument().getLength());
                if (((StyledDocument) doc).getCharacterElement(i).getName().equals("icon")) {
                    this.getTxt_display().insertIcon(iconList.remove(0));
                } else {
                    this.getTxt_display().getStyledDocument()
                            .insertString(
                                    this.getTxt_display().getStyledDocument()
                                            .getLength(), doc.getText(i, 1),
                                    sas);
                }
            }
            this.getTxt_display().getStyledDocument().insertString(
                    this.getTxt_display().getStyledDocument().getLength(), "\n",
                    sas);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    //按时间升序比较
    private static void arrayListSort(ArrayList<ChatMsg> list) {
        Iterator<ChatMsg> iterator = list.iterator();
        if (iterator.hasNext()) {
            ChatMsg next = iterator.next();
            if (next == null) {
                iterator.remove();
            }
        }
        Collections.sort(list, new Comparator<ChatMsg>() {
            @Override
            public int compare(ChatMsg o1, ChatMsg o2) {
                try {
                    if (o1.getDate() == null || o2.getDate() == null) {
                        return 1;
                    }
                    Date dt1 = o1.getDate();
                    Date dt2 = o2.getDate();

                    if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    //监控删除聊天记录按钮
    class Moniter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btn_delete) {
                File fileUser = new File(FileFolder.getDefaultDirectory() + "/" + user.getId() + "/" + targetUser.getId() + ".dat");
                File fileTargetUser = new File(FileFolder.getDefaultDirectory() + "/" + targetUser.getId() + "/" + user.getId() + ".dat");
                if ((fileUser.delete() && fileTargetUser.delete())
                || (!fileUser.delete()&& fileTargetUser.delete())||(fileUser.delete()&&!fileTargetUser.delete())
                ) {
                    JOptionPane.showMessageDialog(null, "删除成功", "成功",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "聊天记录已空", "错误",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    public JTextPane getTxt_display() {
        return txt_display;
    }

    public void setTxt_display(JTextPane txt_display) {
        this.txt_display = txt_display;
    }

}


