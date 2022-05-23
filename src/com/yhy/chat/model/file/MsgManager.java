package com.yhy.chat.model.file;

import com.yhy.chat.model.User;
import com.yhy.chat.model.msg.ChatMsg;
import com.yhy.chat.model.msg.Msg;

import java.io.*;
import java.sql.Array;
import java.util.*;

/**
 * @author: 杨海勇
 **/
public class MsgManager {

    public static void saveMsg(User u1, User u2, ArrayList<ChatMsg> chatMsgs) throws IOException {
        File file = new File(FileFolder.getDefaultDirectory() + "/" + u1.getId() + "/" + u2.getId() + ".dat");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream outputStream = new ObjectOutputStream(fos);

        try {
            for (ChatMsg msg : chatMsgs) {
                outputStream.writeObject(msg);
            }
        } finally {
            outputStream.flush();
            fos.close();
            outputStream.close();
        }
    }


    public static ArrayList<ChatMsg> readMsg(User u1, User u2) throws IOException {
        File file = new File(FileFolder.getDefaultDirectory() + "/" + u1.getId() + "/" + u2.getId() + ".dat");
        ArrayList<ChatMsg> resList = new ArrayList<>();

        if (!file.exists() || file.isDirectory()) {
            return resList;
        }
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));

        try {
            while (true) {
                Object oj = inputStream.readObject();
                ChatMsg msg = (ChatMsg) oj;
                resList.add(msg);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("over");
        } catch (EOFException e) {
//            System.out.println("EOFERROR");
//            for(ChatMsg msg : resList) {
//                System.out.println(msg);
//            }
            //  inputStream.close();
//            return resList;
        } finally {
            inputStream.close();
            return resList;
        }

    }

}
