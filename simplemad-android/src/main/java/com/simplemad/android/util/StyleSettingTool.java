package com.simplemad.android.util;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Divider;
import com.simplemad.android.view.setting.ImageSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.SettingConstants;
import com.simplemad.android.view.setting.TextViewSetting;
import com.simplemad.android.view.setting.ViewAnimatorSetting;

public class StyleSettingTool {

	public static void setBackground(View view, Background background) {
		if(view == null)
			return;
		if(background == null)
			return;
		if(background.background() != null)
			view.setBackgroundDrawable(background.background());
	}
	
	public static void setBackgroundEmpty(View view) {
		if(view == null) {
			return;
		}
		view.setBackgroundDrawable(null);
	}
	
	public static void setBackground(Window window, Background background) {
		if(window == null)
			return;
		if(background == null)
			return;
		if(background.background() != null)
			window.setBackgroundDrawable(background.background());
	}
	
	public static void setBackground(View view, Drawable drawable) {
		if(view == null)
			return;
		if(drawable == null)
			return;
		view.setBackgroundDrawable(drawable);
	}
	
	public static void setTextStyle(TextView textView, TextViewSetting textViewSetting) {
		if(textView == null || textViewSetting == null)
			return;
		Drawable bg = textViewSetting.background();
		ColorStateList csl = textViewSetting.fontColor();
		float fontSize = textViewSetting.fontSize();
		if(bg != null)
			textView.setBackgroundDrawable(bg);
		setBound(textView, textViewSetting.bound());
		if(csl != null) {
			textView.setLinkTextColor(csl);
			textView.setTextColor(csl);
		}
		if(fontSize != SettingConstants.NO_FONT_SIZE)
			textView.setTextSize(fontSize);
		if(textViewSetting.gravity() != SettingConstants.NO_GRAVITY)
			textView.setGravity(textViewSetting.gravity());
		setPadding(textView, textViewSetting.padding());
	}
	
	public static void setPadding(View view, Padding padding) {
		if(view == null || padding == null)
			return;
		view.setPadding(padding.getLeft(), padding.getTop(), padding.getRight(), padding.getBottom());
	}
	
	public static void setBound(TextView textView, Bound bound) {
		if(bound != null) {
			if(bound.getLines() != SettingConstants.NO_LINES)
				textView.setLines(bound.getLines());
			if(bound.getMinHeight() != SettingConstants.NO_HEIGHT) {
				textView.setMinHeight(bound.getMinHeight());
				textView.setMinimumHeight(bound.getMinHeight());
			}
			if(bound.getMinWidth() != SettingConstants.NO_WIDTH) {
				textView.setMinWidth(bound.getMinWidth());
				textView.setMinimumWidth(bound.getMinWidth());
			}
			if(bound.getWidth() != SettingConstants.NO_WIDTH)
				textView.setWidth(bound.getWidth());
			if(bound.getHeight() != SettingConstants.NO_HEIGHT)
				textView.setHeight(bound.getHeight());
			textView.setSingleLine(bound.isSingleLine());
		}
	}
	
	public static void setMargin(Margin margin, MarginLayoutParams params) {
		if(margin != null)
			params.setMargins(margin.left(), margin.top(), margin.right(), margin.bottom());
	}
	
	public static void setAnimation(ViewAnimator animator, ViewAnimatorSetting setting, boolean isOut, int direction) {
		Animation animation = null;
		if(animator == null)
			return;
		if(setting == null)
			return;
		
		switch (direction) {
			case ViewAnimatorSetting.DIRECTION_LEFT: {
				if(isOut)
					animation = setting.leftOutAnimation();
				else
					animation = setting.leftInAnimation();
				break;
			}
			case ViewAnimatorSetting.DIRECTION_TOP: {
				if(isOut)
					animation = setting.topOutAnimation();
				else
					animation = setting.topInAnimation();
				break;
			}
			case ViewAnimatorSetting.DIRECTION_RIGHT: {
				if(isOut)
					animation = setting.rightOutAnimation();
				else
					animation = setting.rightInAnimation();
			}
			case ViewAnimatorSetting.DIRECTION_BOTTOM: {
				if(isOut)
					animation = setting.bottomOutAnimation();
				else
					animation = setting.bottomInAnimation();
				break;
			}
			default:
				break;
		}
		
		if(animation == null)
			return;
		if(isOut)
			animator.setOutAnimation(animation);
		else
			animator.setInAnimation(animation);
	}
	
	public static void setImageViewStyle(ImageSetting setting, ImageView image) {
		image.setImageDrawable(setting.image());
		setPadding(image, setting.padding());
	}
	
	public static void setListViewStyle(ListView listView, Divider divider) {
		if(listView == null || divider == null) {
			return;
		}
		listView.setDivider(divider.background());
		listView.setDividerHeight(divider.getHeight());
	}
}
