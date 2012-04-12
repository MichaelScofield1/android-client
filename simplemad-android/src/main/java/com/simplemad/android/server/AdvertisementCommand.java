package com.simplemad.android.server;

import android.content.Intent;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.client.AdEffectEntityUtil;
import com.simplemad.android.service.DownloadService;
import com.simplemad.android.service.DownloadServiceImpl;
import com.simplemad.android.service.UserAccountService;
import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.service.android.AdNotificationService;
import com.simplemad.android.util.AppUtil;
import com.simplemad.bean.AdEffectEntity;
import com.simplemad.bean.Advertisement;
import com.simplemad.bean.Message;
import com.simplemad.message.util.JacksonUtil;

public class AdvertisementCommand implements IMessageCommand {

	private Message msg;
	private DownloadService downloadService;
	private UserAccountService accountService;
	
	public AdvertisementCommand(Message msg) {
		this.msg = msg;
		accountService = UserAccountServiceImpl.instance();
	}
	
	@Override
	public void execute() {
		String content = msg.getContent();
		Advertisement advertisement = JacksonUtil.getObject(content, Advertisement.class);
		System.out.println("received advertisement from : " + msg.getSource().getHost() + " " + msg.getSource().getPort());
		if(accountService.getCurrentUserAccount() == null) {
			return;
		}
		if(advertisement != null) {
			if(advertisement.getMobile() != accountService.getCurrentUserAccount().getMobile()) {
				return;
			}
			downloadService = new DownloadServiceImpl();
			boolean isSaved = downloadService.downloadPreviewFile(advertisement);
			if(!isSaved) {
				return;
			}
			Intent intent = new Intent(SimpleMadApp.instance(), AdNotificationService.class);
			intent.putExtra(AppUtil.EXTRA_NAME_AD, advertisement);
			SimpleMadApp.instance().startService(intent);
			AdEffectEntityUtil.send(advertisement, AdEffectEntity.KEY_AD_RECEIVED);
		}
	}

}
