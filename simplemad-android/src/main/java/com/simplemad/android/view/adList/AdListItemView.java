package com.simplemad.android.view.adList;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.simplemad.bean.Advertisement;

public class AdListItemView extends View {
	
	private List<String> textList = new ArrayList<String>();
	
	private Advertisement advertisement;
	
	private int width;
	 
	private int height;
	
	private TextPaint titleTp;

	public AdListItemView(Context context) {
		super(context);
		init();
	}
	
	public AdListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public AdListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	private void init() {
		titleTp = new TextPaint(TextPaint.DEV_KERN_TEXT_FLAG);
		titleTp.setColor(Color.WHITE);
		titleTp.setTextSize(36.0f);
	}
	
	public void setDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setAdvertisement(Advertisement advertisement) {
		this.advertisement = advertisement;
		addText(this.advertisement.getName());
	}
	
	public void addText(String text) {
		textList.add(text);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(width == 0 ? 480 : width, height == 0 ? 100 : height);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint p = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
		p.setColor(Color.RED);
		RectF rectf = new RectF(10, 10, 100, 100);
		canvas.drawRoundRect(rectf, 90, 90, p);
		
		
		int y = 0;
		for(int index = 0; index < textList.size(); index++) {
			Rect rect = new Rect();
			titleTp.getTextBounds(textList.get(index), 0, 1, rect);
			y += rect.height();
			canvas.drawText(textList.get(index), 110, y, titleTp);
		}
	}

}
