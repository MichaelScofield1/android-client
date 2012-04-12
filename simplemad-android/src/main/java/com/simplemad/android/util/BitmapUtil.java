package com.simplemad.android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapUtil {
	
	/**
	 * @param filePath 图片的本地路径(图片必须存在于android文件系统中)
	 * @param minSideLength 原始图片将要放至的view的长与宽之间的最小值,即Math.min(width, height)
	 * @param maxNumOfPixels 原始图片将要放至的view的最大像素值,即width*heigh;
	 * @return 压缩或放大后的图片
	 */
	public static Bitmap createPromptBitmap(String filePath, int minSideLength, int maxNumOfPixels) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		
		opts.inSampleSize = computeSampleSize(opts, minSideLength, maxNumOfPixels);
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, opts);
	}

	public static int computeSampleSize(Options opts, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(opts, minSideLength, maxNumOfPixels);
		int roundedSize;
		if(initialSize <= 8) {
			roundedSize = 1;
			while(roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}
	
	protected static int computeInitialSampleSize(Options opts, int minSideLength, int maxNumOfPixels) {
		double w = opts.outWidth;
		double h = opts.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if(upperBound < lowerBound)
			return lowerBound;
		if(maxNumOfPixels == -1 && minSideLength == -1)
			return 1;
		else if(minSideLength == -1)
			return lowerBound;
		else
			return upperBound;
	}
}
