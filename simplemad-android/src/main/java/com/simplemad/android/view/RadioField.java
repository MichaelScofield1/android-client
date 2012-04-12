package com.simplemad.android.view;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RadioField extends FieldView {
	
	private RadioGroup group;
	private int id = 1;

	public RadioField(Context context) {
		super(context);
	}
	
	public void setTarget(Object target) {
		group.setTag(target);
	}
	
	public Object getTarget() {
		return group.getTag();
	}
	
	public Object getValue() {
		return group.findViewById(group.getCheckedRadioButtonId()).getTag();
	}
	
	public void addRadio(Object target, String text, boolean isSelected) {
		RadioButton rb = new RadioButton(getContext());
		rb.setText(text);
		rb.setTag(target);
		rb.setId(id++);
		rb.setChecked(isSelected);
		group.addView(rb);
	}

	@Override
	protected View createEditView() {
		group = new RadioGroup(getContext());
		return group;
	}

}
