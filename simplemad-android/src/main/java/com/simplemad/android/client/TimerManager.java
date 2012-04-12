package com.simplemad.android.client;

import com.simplemad.bean.Timer;

public class TimerManager {

	private static Timer timer;
	
	private static final int MINUTES = 60 * 1000;
	
	public static Timer getTimer() {
		if(timer == null) {
			timer = new Timer();
			timer.setStart(9 * 60 * MINUTES);
			timer.setEnd(21 * 60 * MINUTES);
			timer.setInterval(1 * 60 * MINUTES);
		}
		return timer;
	}
	
	public static void setTimer(Timer newTimer) {
		getTimer();
		if(newTimer != null) {
			timer.setStart(newTimer.getStart());
			timer.setEnd(newTimer.getEnd());
			timer.setInterval(newTimer.getInterval());
		}
	}
}
