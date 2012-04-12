package com.simplemad.android.view.setting;

public class Bound {

	private int width;
	
	private int height;
	
	private int lines;
	
	private int minWidth;
	
	private int minHeight;
	
	private boolean singleLine;
	
	public Bound(int width, int height, int lines, int minWidth, int minHeight, boolean singleLine) {
		this.width = width;
		this.height = height;
		this.lines = lines;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.singleLine = singleLine;
	}
	
	/**
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * @return
	 */
	public int getLines() {
		return lines;
	}
	
	/**
	 * @return
	 */
	public int getMinWidth() {
		return minWidth;
	}
	
	/**
	 * @return
	 */
	public int getMinHeight() {
		return minHeight;
	}
	
	public boolean isSingleLine() {
		return singleLine;
	}
}
