package com.yhy.chat.model.msg;

import com.yhy.chat.main.ChatClient;
import sun.audio.AudioPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class SoundMsg extends Msg {
	public abstract void processWithNoSound(ChatClient client);

	public void playSound(String path) {
		try {
			AudioPlayer.player.start(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
