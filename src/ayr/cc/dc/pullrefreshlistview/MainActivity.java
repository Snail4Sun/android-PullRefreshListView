package ayr.cc.dc.pullrefreshlistview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import ayr.cc.dc.R;
import ayr.cc.dc.pullrefreshlistview.adpter.ListViewAdapter;
import ayr.cc.dc.pullrefreshlistview.custom.PullRefreshListView;
import ayr.cc.dc.pullrefreshlistview.custom.PullRefreshListView.ListViewRefreshData;

public class MainActivity extends Activity implements ListViewRefreshData {

	private PullRefreshListView mListView = null;

	private List<String> mData = null;

	private ListViewAdapter adapter = null;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mListView = (PullRefreshListView) findViewById(R.id.listview);
		initData();
	}

	private void initData() {
		mData = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			mData.add("这是刷新前的数据" + i);
		}
		adapter = new ListViewAdapter(MainActivity.this, mData);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Toast.makeText(MainActivity.this, "点击item", 0).show();
			}
		});

		mListView.setListViewRefreshData(this);
	}

	@Override
	public void downPullRefresh() {
     Handler handler = new Handler();
     handler.postDelayed(new Runnable() {
		
		@Override
		public void run() {
			downPullRefreshData();
			mListView.downPullRefreshComplete();
		}
	}, 2000);
	}

	private void downPullRefreshData() {
		for (int i = 0; i < 2; i++) {
			mData.add(0, "刷新后的数据");
		}
		adapter.notifyDataSetChanged();
	}
}
