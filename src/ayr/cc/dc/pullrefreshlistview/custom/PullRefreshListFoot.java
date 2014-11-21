package ayr.cc.dc.pullrefreshlistview.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ayr.cc.dc.R;

public class PullRefreshListFoot extends ViewGroup {
	private Context mContext = null;
	private View mView;

	public PullRefreshListFoot(Context context) {
		this(context, null);
	}

	public PullRefreshListFoot(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public PullRefreshListFoot(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initViews();
	}

	private void initViews() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.listview_foot, null);
	    this.addView(mView);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

}
