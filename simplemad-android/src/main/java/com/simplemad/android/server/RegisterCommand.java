package com.simplemad.android.server;

import android.content.Intent;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.util.AppUtil;
import com.simplemad.bean.Message;
import com.simplemad.message.util.JacksonUtil;

public class RegisterCommand implements IMessageCommand {

	private Message msg;
	
	public RegisterCommand(Message msg) {
		this.msg = msg;
	}
	
	@Override
	public void execute() {
		String content = msg.getContent();
		Boolean result = JacksonUtil.getObject(content, Boolean.class);
		System.out.println("RegisterCommand result : " + result);
		Intent intent = new Intent(AppUtil.RECEIVER_USER_REGISTER_RESULT_MSG);
		intent.putExtra(AppUtil.EXTRA_NAME_REGISTER_RESULT, result);
		SimpleMadApp.instance().sendBroadcast(intent);
	}

}
