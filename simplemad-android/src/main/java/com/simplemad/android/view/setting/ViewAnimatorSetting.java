package com.simplemad.android.view.setting;

import android.view.animation.Animation;

public interface ViewAnimatorSetting {

	static final int DIRECTION_LEFT = 0;
	
	static final int DIRECTION_TOP = 1;
	
	static final int DIRECTION_RIGHT = 2;
	
	static final int DIRECTION_BOTTOM = 3;
	
	Animation leftInAnimation();
	
	Animation leftOutAnimation();
	
	Animation rightInAnimation();
	
	Animation rightOutAnimation();
	
	Animation topInAnimation();
	
	Animation topOutAnimation();
	
	Animation bottomInAnimation();
	
	Animation bottomOutAnimation();
}
