package com.simplemad.android.view.setting;

public final class Padding {

	private int left;
	
	private int top;
	
	private int right;
	
	private int bottom;
	
	public Padding(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public int getRight() {
		return right;
	}

	public int getBottom() {
		return bottom;
	}
}
