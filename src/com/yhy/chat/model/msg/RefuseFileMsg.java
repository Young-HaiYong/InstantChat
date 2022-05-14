package com.yhy.chat.model.msg;

import com.yhy.chat.main.ChatClient;
import com.yhy.chat.main.Server;
import com.yhy.chat.view.ChatFrame;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.File;

public class RefuseFileMsg extends Msg {

	private File file;

	@Override
	public void process(ChatClient client) {
		ChatFrame cf = null;
		if (client.getUser().equals(user)) {
			for (ChatFrame c : client.getChatFrameList()) {
				if (c.getTargetUser().equals(targetUser)) {
					cf = c;
					break;
				}
			}
		} else if (client.getUser().equals(targetUser)) {
			for (ChatFrame c : client.getChatFrameList()) {
				if (c.getTargetUser().equals(user)) {
					cf = c;
					break;
				}
			}
		}
		if (cf.getFile().equals(file)) {
			SimpleAttributeSet sas = new SimpleAttributeSet();
			StyleConstants.setForeground(sas, Color.BLUE);
			try {
				cf.getTxt_display().getStyledDocument().insertString(
						cf.getTxt_display().getStyledDocument().getLength(),
						"�ܾ��ļ� " + file.getName() + " ��С:" + file.length()
								/ 1024 + "k\n", sas);
				cf.getTxt_display().setCaretPosition(
						cf.getTxt_display().getStyledDocument()
								.getLength());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			cf.getBtn_acceptFile().setVisible(false);
			cf.getBtn_refuseFile().setVisible(false);
			cf.setFile(null);
		}
	}

	@Override
	public void process(Server server) {

	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
