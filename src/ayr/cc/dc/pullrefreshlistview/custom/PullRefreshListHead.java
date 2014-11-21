package ayr.cc.dc.pullrefreshlistview.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import ayr.cc.dc.R;

public class PullRefreshListHead extends ViewGroup {
	private Context mContext = null;
	private View mView;

	public PullRefreshListHead(Context context) {
		this(context, null);
	}

	public PullRefreshListHead(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public PullRefreshListHead(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initViews();
	}

	private void initViews() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.listview_head, null);
	    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 100);
		addView(mView, lp);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

}
