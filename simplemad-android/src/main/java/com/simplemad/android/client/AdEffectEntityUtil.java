package com.simplemad.android.client;

import com.simplemad.bean.AdEffectEntity;
import com.simplemad.bean.Advertisement;
import com.simplemad.bean.MessageType;

public class AdEffectEntityUtil {

	public static void send(final Advertisement advertisement, final int key) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				AdEffectEntity entity = new AdEffectEntity();
				entity.setAdId(advertisement.getId());
				entity.setKey(key);
				entity.setMobile(advertisement.getMobile());
				entity.setValue(true);
				ClientUtil.doSend(MessageType.AD_EFFECT, entity);
			}
		}).start();
		
	}
	
	public static void send(final Advertisement advertisement, final int key, final Object obj) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				AdEffectEntity entity = new AdEffectEntity();
				entity.setAdId(advertisement.getId());
				entity.setKey(key);
				entity.setMobile(advertisement.getMobile());
				entity.setValue(obj);
				ClientUtil.doSend(MessageType.AD_EFFECT, entity);
			}
		}).start();
		
	}
}
