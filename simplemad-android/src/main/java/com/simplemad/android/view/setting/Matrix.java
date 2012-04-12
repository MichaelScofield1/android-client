package com.simplemad.android.view.setting;

public class Matrix {

	private int _rows;
	
	private int _columns;
	
	public Matrix(int rows, int columns) {
		_rows = rows;
		_columns = columns;
	}
	
	public int getRows() {
		return _rows;
	}
	
	public int getColumns() {
		return _columns;
	}
}
