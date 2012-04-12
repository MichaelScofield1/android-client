package com.simplemad.android.util;

import java.util.ArrayList;
import java.util.List;

public class MatrixMaker<T> {

	private List<DasGridItem<T>> _items=new ArrayList<DasGridItem<T>>();
	private int colCount;
	private boolean isInifine;
	private int currentRowIndex=0;
	private int currentColIndex=0;
	
	public MatrixMaker(int colCount){
		this.colCount=colCount;
		this.isInifine = this.colCount == 0 ? true : false;
	}
	
	public void addItem(T value){
		if(isInifine) {
			colCount++;
		} 
		if (currentColIndex >= colCount){
			currentColIndex = 0;
			currentRowIndex += 1;
		}
		_items.add(new DasGridItem<T>(currentRowIndex,currentColIndex,value));
		currentColIndex++;
	}
	
	public void addItems(List<T> valueList) {
		if(CollectionUtil.isEmpty(valueList))
			return;
		for(T value : valueList) {
			addItem(value);
		}
	}

	public int getRowCount(){
		int result=0;
		for (DasGridItem<T> item:_items){
			result=Math.max(result, item.getRow()+1);
		}
		return result;
	}

	public int getColCount(){
		return colCount == 0 ? currentColIndex : colCount;
	}
	
	public T getValue(int rowIndex,int colIndex){
		for (DasGridItem<T> item:_items){
			if (item.getRow()==rowIndex && item.getCol()==colIndex){
				return item.getValue();
			}
		}
		return null;
	}
	
}

class DasGridItem<T> {
	private int rowIndex;
	private int colIndex;
	private T obj;

	public DasGridItem(int rowIndex, int colIndex, T obj) {
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
		this.obj = obj;
	}

	public int getRow() {
		return rowIndex;
	}
	public int getCol(){
		return colIndex;
	}
	public T getValue(){
		return obj;
	}
	
}
