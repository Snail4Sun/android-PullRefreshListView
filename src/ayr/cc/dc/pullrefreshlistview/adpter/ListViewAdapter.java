package ayr.cc.dc.pullrefreshlistview.adpter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	private Context mContext = null;
	private List<String> mData = null;

	public ListViewAdapter(Context context, List<String> data) {
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(mContext);
		textView.setText(mData.get(position));
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(30);
		return textView;
	}

}
