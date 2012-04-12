package com.simplemad.android.adapter;

import java.util.List;

import com.simplemad.android.setting.AverageTextViewSetting;
import com.simplemad.android.util.CollectionUtil;
import com.simplemad.android.view.AverageTextView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AverageTextViewAdapter extends BaseAdapter {
 
	private AverageTextViewSetting setting;
	private List<List<String>> data;
	private Context context;
	
	public AverageTextViewAdapter(Context context, AverageTextViewSetting setting, List<List<String>> data) {
		this.setting = setting;
		this.data = data;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		if(CollectionUtil.isEmpty(data)) {
			return 0;
		}
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return new AverageTextView(context, setting, data.get(position));
	}

}
