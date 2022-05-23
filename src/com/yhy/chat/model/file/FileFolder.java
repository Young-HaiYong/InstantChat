package com.yhy.chat.model.file;

import java.io.File;

/**
 * @author: 杨海勇
 **/
public class FileFolder {

    public static String getDefaultDirectory() {
        //聊天记录文件路径
        return "./src/com/yhy/chat/model";
    }

    public static void init() {
        File file = new File(getDefaultDirectory());
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        file.mkdirs();
    }
}
