package com.yhy.chat.model.msg;

import com.yhy.chat.main.ChatClient;
import com.yhy.chat.main.Server;

import javax.swing.*;
import java.awt.*;

public class UpdateUserMsg extends Msg {

	@Override
	public void process(ChatClient client) {
		if (client.getUser().equals(user)) {
			client.getUser().setUser(user);
			client.getUserListFrame().getLbl_sign().setText(user.getSign());
			client.getUserListFrame().getLbl_name().setText(user.getName());
			ImageIcon img = new ImageIcon(user.getImgPath());
			img.setImage(img.getImage().getScaledInstance(40, 40,
					Image.SCALE_DEFAULT));
			client.getUserListFrame().getLbl_head().setIcon(img);
		} else {
			client.getUserListFrame().getUserListPanel().setUser(user);
		}
	}

	@Override
	public void process(Server server) {
		server.getUserManager().updateUser(user);
	}
}
