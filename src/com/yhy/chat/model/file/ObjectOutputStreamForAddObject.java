package com.yhy.chat.model.file;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @author: 杨海勇
 **/
public class ObjectOutputStreamForAddObject extends ObjectOutputStream {
    private static File f;

    public static ObjectOutputStreamForAddObject newInstance(File file, OutputStream out) throws IOException {

        f = file;

        return new ObjectOutputStreamForAddObject(out);

    }

    private ObjectOutputStreamForAddObject(OutputStream out) throws IOException {

        super(out);

    }

    @Override

    protected void writeStreamHeader() throws IOException {

        if (!f.exists() || (f.exists() && f.length() == 0)) {

            super.writeStreamHeader();

        } else {

            super.reset();

        }

    }
}

