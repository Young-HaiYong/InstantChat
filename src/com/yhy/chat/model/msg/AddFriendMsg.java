package com.yhy.chat.model.msg;

import com.yhy.chat.main.ChatClient;
import com.yhy.chat.main.Server;

import javax.swing.*;

public class AddFriendMsg extends SoundMsg {

	private String str;

	@Override
	public void process(ChatClient client) {
		if (client.getUser().equals(user)) {
			JOptionPane.showMessageDialog(null, "��������ɹ�");
		} else {
			this.playSound(client.getProperties().get("MSG_SOUND_PATH"));
			client.getUserListFrame().addUnreadMsg(this);
		}
	}

	@Override
	public void process(Server server) {

	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	@Override
	public void processWithNoSound(ChatClient client) {
		if (JOptionPane.showConfirmDialog(null, "�û�  " + user.getName()
				+ " �������Ϊ���ѣ���֤��Ϣ��" + str + "������������", "",
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			AcceptFriendMsg msg = new AcceptFriendMsg();
			msg.setUser(user);
			msg.setTargetUser(targetUser);
			msg.send();
		}
	}

}
