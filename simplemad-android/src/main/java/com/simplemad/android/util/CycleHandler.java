package com.simplemad.android.util;

import android.os.Handler;
import android.os.Message;

public abstract class CycleHandler extends Handler {

	private int PAUSE_FLAG = 1;
	private boolean _running;
	private boolean _changeRunning;
	private long _interval = 20000;
	private long _pauseInterval = 30000;
	private long _historyTime;
	private boolean _isImmediate;
	
	public CycleHandler(long interval, long pauseInterval, boolean isImmediate) {
		super();
		_interval = interval;
		_pauseInterval = pauseInterval;
		_isImmediate = isImmediate;
		if(_interval > _pauseInterval)
			_pauseInterval = _interval;
	}
	
	@Override
	public final void handleMessage(Message msg) {
		if(msg.what == PAUSE_FLAG) {
			if(_running) {
				Message newMsg = obtainMessage(PAUSE_FLAG);
				long intervalTiming = System.currentTimeMillis() - _historyTime;
				if(intervalTiming >= _interval) {
					updateTouchTime();
					run();
					sendMessageDelayed(newMsg, _interval);
				} else {
					sendMessageDelayed(newMsg, _pauseInterval - intervalTiming);
				}
			}
		}
	}
	
	public void updateTouchTime() {
		_historyTime = System.currentTimeMillis();
	}
	
	public void start() {
		updateTouchTime();
		_changeRunning = true;
		updateRunning();
	}
	
	public void startForLater() {
		_isImmediate = false;
		updateTouchTime();
		_changeRunning = true;
		updateRunning();
	}
	
	public void stop() {
		_changeRunning = false;
		updateRunning();
	}
	
	public boolean isRunning() {
		return _running;
	}
	
	private void updateRunning() {
		boolean running = _changeRunning;
		if(running != _running) {
			if (running) {
            	removeMessages(PAUSE_FLAG);
                Message msg = obtainMessage(PAUSE_FLAG);
                if(_isImmediate)
                	run();
                sendMessageDelayed(msg, _interval);
            } else {
                removeMessages(PAUSE_FLAG);
            }
            _running = running;
		}
	}
	
	protected abstract void run();
}
