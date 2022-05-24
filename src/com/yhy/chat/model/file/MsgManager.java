package com.yhy.chat.model.file;

import com.yhy.chat.model.User;
import com.yhy.chat.model.msg.ChatMsg;
import com.yhy.chat.model.msg.Msg;

import java.io.*;
import java.sql.Array;
import java.util.*;

/**
 * @author: 杨海勇
 * 实现对聊天记录的存储和加载
 **/
public class MsgManager {

    //存储聊天记录
    public static void saveMsg(User u1, User u2, ArrayList<ChatMsg> chatMsgs) throws IOException {
        boolean toDo = true;
        if (u2 == null){
            toDo = false;
        }

        if (toDo) {
            File file = new File(FileFolder.getDefaultDirectory() + "/" + u1.getId() + "/" + u2.getId() + ".dat");

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            try {
                if (file.exists()) {
                    MyObjectOutputStream out = new MyObjectOutputStream(fos);
                    for (ChatMsg msg : chatMsgs) {
                        out.writeObject(msg);
                    }
                    out.flush();
                    out.close();
                } else {
                    ObjectOutputStream out = new ObjectOutputStream(fos);
                    for (ChatMsg msg : chatMsgs) {
                        out.writeObject(msg);
                    }
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fos.close();
            }
        }

    }

    //读取聊天记录
    public static ArrayList<ChatMsg> readMsg(User u1, User u2) throws IOException {
        File file = new File(FileFolder.getDefaultDirectory() + "/" + u1.getId() + "/" + u2.getId() + ".dat");
        ArrayList<ChatMsg> resList = new ArrayList<>();

        if (!file.exists() || file.isDirectory()) {
            return resList;
        }
        ObjectInputStream inputStream = new MyObjectInputStream(new FileInputStream(file));

        try {
            while (true) {
                Object oj = inputStream.readObject();
                ChatMsg msg = (ChatMsg) oj;
                resList.add(msg);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("over");
        } catch (EOFException e) {

        } finally {
            inputStream.close();
            return resList;
        }

    }

    //重写ObjectOutputStream 实现对象追加的存储
    static class MyObjectOutputStream extends ObjectOutputStream {

        public MyObjectOutputStream(OutputStream os) throws IOException, SecurityException {
            super(os);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            super.reset();
        }
    }

    //重写ObjectInputStream 实现对对象追加的存储文件的读取
    static class MyObjectInputStream extends ObjectInputStream {

        public MyObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected void readStreamHeader() throws IOException, StreamCorruptedException {
        }
    }
}
