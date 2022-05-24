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
        boolean toDo = true;
        if (u2 == null){
            toDo = false;
        }

        if (toDo) {
            File file = new File(FileFolder.getDefaultDirectory() + "/" + u1.getId() + "/" + u2.getId() + ".dat");
            boolean isexist = false;

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
//        ObjectOutputStreamForAddObject addObject = ObjectOutputStreamForAddObject.newInstance(file,fos);
//        ObjectOutputStream outputStream = new ObjectOutputStream(fos);
//        long pos = 0;
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
                //            fos.close();
                //            if (isexist) {
                //                pos = fos.getChannel().position() - 4;
                //                fos.getChannel().truncate(pos);
                //            }
                //            for (ChatMsg msg : chatMsgs) {
                //                outputStream.writeObject(msg);
                //            }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fos.close();
            }

//            outputStream.close();
        }

    }


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



    static class MyObjectOutputStream extends ObjectOutputStream {

        public MyObjectOutputStream(OutputStream os) throws IOException, SecurityException {
            super(os);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            super.reset();
        }
    }


    static class MyObjectInputStream extends ObjectInputStream {

        public MyObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected void readStreamHeader() throws IOException, StreamCorruptedException {
        }
    }
}
