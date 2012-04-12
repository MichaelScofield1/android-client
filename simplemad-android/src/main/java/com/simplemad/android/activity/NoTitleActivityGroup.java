package com.simplemad.android.activity;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.Window;

public class NoTitleActivityGroup extends ActivityGroup {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
}
