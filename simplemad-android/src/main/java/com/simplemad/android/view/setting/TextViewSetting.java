package com.simplemad.android.view.setting;

import android.content.res.ColorStateList;

public interface TextViewSetting extends Background {

	/**
	 * �r��j�p
	 * @return
	 */
	float fontSize();
	
	/**
	 * �r���C��
	 * @return
	 */
	ColorStateList fontColor();
	
	/**
	 * ����覡
	 * @return
	 */
	int gravity();
	
	/**
	 * textview���j�p
	 * @return
	 */
	Bound bound();
	
	/**
	 * ��R
	 * @return
	 */
	Padding padding();
	
	/**
	 * ���,�Ω�b�K�[TextView��layout����ɳ]�w
	 * @return
	 */
	Margin margin();
	
}
