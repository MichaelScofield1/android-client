package com.simplemad.android.view.adList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.OvalShape;

public class DeleteShape extends OvalShape {

	@Override
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);
		float yIndex = rect().centerY();
		float xLeftIndex = rect().left;
		float xRightIndex = rect().right;
		Paint linePaint = new Paint();
		linePaint.setStrokeWidth(3);
		linePaint.setColor(Color.WHITE);
		canvas.drawLine(xLeftIndex, yIndex, xRightIndex, yIndex, linePaint);
	}
}
