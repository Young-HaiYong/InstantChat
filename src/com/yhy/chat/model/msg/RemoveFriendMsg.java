package com.yhy.chat.model.msg;

import com.yhy.chat.main.ChatClient;
import com.yhy.chat.main.Server;

import javax.swing.*;

public class RemoveFriendMsg extends Msg {

	@Override
	public void process(ChatClient client) {
		if (client.getUser().equals(user)) {
			client.getUser().getFirends().remove(targetUser);
			client.getUserListFrame().getUserListPanel().setUser(targetUser);
			JOptionPane.showMessageDialog(null, "�û�  " + targetUser.getName()
					+ " ������������ѹ�ϵ");
		} else {
			client.getUser().getFirends().remove(user);
			client.getUserListFrame().getUserListPanel().setUser(user);
			JOptionPane.showMessageDialog(null, "�û�  " + user.getName()
					+ " �Ѳ��������ĺ���");
		}
		client.getUserListFrame().getUserListPanel().rebuild();
	}

	@Override
	public void process(Server server) {
		server.getUserManager().removeFriend(user, targetUser);
	}
}
