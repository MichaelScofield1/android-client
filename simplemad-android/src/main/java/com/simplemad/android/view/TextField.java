package com.simplemad.android.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

public class TextField extends FieldView {

	private EditText editView;
	
	public TextField(Context context) {
		super(context);
	}
	
	public void setValue(Object target, String value) {
		editView.setText(value);
		editView.setTag(target);
	}
	
	public Object getTarget() {
		return editView.getTag();
	}
	
	public String getValue() {
		return editView.getEditableText().toString();
	}

	@Override
	protected View createEditView() {
		editView = new EditText(getContext());
		return editView;
	}

}
