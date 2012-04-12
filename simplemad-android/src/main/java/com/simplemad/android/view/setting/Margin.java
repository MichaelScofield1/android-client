package com.simplemad.android.view.setting;

public final class Margin {

	private int _left;
	
	private int _top;
	
	private int _right;
	
	private int _bottom;
	
	public Margin(int left, int top, int right, int bottom) {
		this._left = left;
		this._top = top;
		this._right = right;
		this._bottom = bottom;
	}
	
	public int left() {
		return this._left;
	}

	public int top() {
		return this._top;
	}

	public int right() {
		return this._right;
	}

	public int bottom() {
		return this._bottom;
	}
}
