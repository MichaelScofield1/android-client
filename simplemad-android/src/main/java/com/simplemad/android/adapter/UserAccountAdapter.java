package com.simplemad.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.simplemad.android.util.CollectionUtil;
import com.simplemad.bean.UserAccount;

public class UserAccountAdapter extends BaseAdapter implements Filterable {

	private Context context;
	private List<UserAccount> userAccountList;
	private List<UserAccount> resultList;
	private final Object lock = new Object();
	private Filter filter;
	private boolean isFirst = true;
	
	public UserAccountAdapter(Context context, List<UserAccount> userAccountList) {
		this.context = context;
		this.userAccountList = userAccountList;
		if(!CollectionUtil.isEmpty(userAccountList))
			this.resultList = new ArrayList<UserAccount>(userAccountList);
	}
	
	@Override
	public int getCount() {
		if(CollectionUtil.isEmpty(resultList)) {
			return 0;
		} else {
			return resultList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return resultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text = new TextView(context);
		UserAccount userAccount = resultList.get(position);
		text.setText(String.valueOf(userAccount.getMobile()));
		text.setBackgroundColor(Color.WHITE);
		text.setTextColor(Color.BLACK);
		return text;
	}

	@Override
	public Filter getFilter() {
		if(filter == null)
			filter = new UserAccountFilter();
		return filter;
	}
	
	private class UserAccountFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if(isFirst) {
            	isFirst = false;
            	return results;
            }
            
            if (userAccountList == null) {
                synchronized (lock) {
                	userAccountList = new ArrayList<UserAccount>();
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<UserAccount> list;
                synchronized (lock) {
                    list = new ArrayList<UserAccount>(userAccountList);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<UserAccount> values;
                synchronized (lock) {
                    values = new ArrayList<UserAccount>(userAccountList);
                }

                final int count = values.size();
                final ArrayList<UserAccount> newValues = new ArrayList<UserAccount>();

                for (int i = 0; i < count; i++) {
                    final UserAccount value = values.get(i);
                    final String valueText = String.valueOf(value.getMobile()).toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
		@Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            resultList = (List<UserAccount>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
        
        @Override
        public CharSequence convertResultToString(Object resultValue) {
        	UserAccount userAccount = (UserAccount) resultValue;
            return userAccount == null ? "" : String.valueOf(userAccount.getMobile());
        }
    }

}
