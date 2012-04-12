package com.simplemad.android.util;

import android.content.Context;
import android.os.Vibrator;

public class VibratorUtil {

	public static void vibrator(Context context, long timeMillions) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(timeMillions);
	}
}
