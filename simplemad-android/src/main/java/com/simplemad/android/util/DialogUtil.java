package com.simplemad.android.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Debug.MemoryInfo;
import android.os.Process;
import android.widget.Toast;

public class DialogUtil {
	//private static boolean enable 
	public static void testSay(Context _context,Object data){
		Dialog showDialog=new AlertDialog.Builder(_context)
//		.setView(DialogUtil.createTitleView(_context, data.toString()))
		.setTitle(data.toString())
		.setPositiveButton("确认", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create();
		showDialog.show();
		
		
		
	}
	
	public static void testSay(Context _context,Object data
			,DialogInterface.OnClickListener listener){
		Dialog showDialog=new AlertDialog.Builder(_context)
//		.setCustomTitle(DialogUtil.createTitleView(_context, data.toString()))
		.setTitle(data.toString())
		.setPositiveButton("确认", listener).create();
		showDialog.show();		
	}
	
	public static void showMemory(Activity activity,String title ){
		
		int pid=Process.myPid();
		final ActivityManager activityManager = (ActivityManager)activity.getSystemService(Activity.ACTIVITY_SERVICE);
		MemoryInfo outInfo=activityManager .getProcessMemoryInfo(new int[]{pid})[0]; 
	
        StringBuffer buffer=new StringBuffer();
        buffer.append(title);
        buffer.append("\nPid:---->").append(pid);
        buffer.append("\ndalvikPss:---->").append(outInfo.dalvikPss )  
        .append("Kb");  
        buffer.append("\ndalvikPrivateDirty:---->").append(outInfo.dalvikPrivateDirty )  
        .append("Kb");  
        buffer.append("\ndalvikSharedDirty:---->").append(outInfo.dalvikSharedDirty )  
        .append("Kb");  
        buffer.append("\nnativePss:---->").append(outInfo.nativePss )  
        .append("Kb");
        buffer.append("\nnativePrivateDirty:---->").append(outInfo.nativePrivateDirty )  
        .append("Kb");  
        buffer.append("\nnativeSharedDirty:---->").append(outInfo.nativeSharedDirty )  
        .append("Kb");  
        //buffer.append("\nTotalSharedDirty:----->").append(outInfo.getTotalSharedDirty());
//        buffer.append("\nUsing Memory:---->").append(outInfo.threshold >> 20)  
//                .append("M");  
//        buffer.append("\nRemaining Memory:---->").append(outInfo.availMem >> 20)  
//                .append("M");  
//        buffer.append("\nIs Lower Memroy status:----->").append(outInfo.lowMemory);
        testSay(activity,buffer.toString());
	}
	public static void testShow(Context context,Object data){
	
	  Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show();
		
	}
	
	public static void sayError(Context context, Object object) {
		Dialog showDialog=new AlertDialog.Builder(context)
//		.setView(DialogUtil.createTitleView(context, object.toString()))
		.setTitle(object.toString())
		.setPositiveButton("确认", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).create();
		showDialog.show();
	}
	
	public static void sayInfo(Context context, Object object) {
		Dialog showDialog=new AlertDialog.Builder(context)
		.setTitle(object.toString())
//		.setView(DialogUtil.createTitleView(context, object.toString()))
		.setPositiveButton("确认", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).create();
		showDialog.show();
	}
}
