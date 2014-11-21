package ayr.cc.dc.pullrefreshlistview.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import ayr.cc.dc.R;

public class PullRefreshListView extends ListView implements OnScrollListener {
	// private PullRefreshListHead mHeadView = null;
	// private PullRefreshListFoot mFootView = null;
	private static final String TAG = "PullRefreshListView";
	private Context mContext = null;
	/** 头部的View */
	private View mHeadView = null;
	/** 底部的View */
	private View mFootView = null;
	/** 头部的View的高度 */
	private int mHeadViewHight = 0;
	/** 底部的View的高度 */
	private int mFootViewHight = 0;
	/** 第一个显示可以看见的item */
	private int mFirstVisibleItem = 0;
	/** 按下时的Y坐标 **/
	private int mDownY;

	public PullRefreshListView(Context context) {
		this(context, null);
	}

	public PullRefreshListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PullRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initHead();
		initFoot();
	}

	/**
	 * 初始化头部
	 */
	private void initHead() {
		mHeadView = LayoutInflater.from(mContext).inflate(
				R.layout.listview_head, null);
		addHeaderView(mHeadView);
		// 获取mHeadView的高度
		mHeadView.measure(0, 0);
		mHeadViewHight = mHeadView.getMeasuredHeight();
		// 隐藏mHeadView
		mHeadView.setPadding(0, -mHeadViewHight, 0, 0);
	}

	/**
	 * 初始化底部
	 */
	private void initFoot() {
		mFootView = LayoutInflater.from(mContext).inflate(
				R.layout.listview_foot, null);
		addFooterView(mFootView);
		// 获取mFootView的高度
		mFootView.measure(0, 0);
		mFootViewHight = mFootView.getMeasuredHeight();
		// 隐藏mFootView
		mFootView.setPadding(0, -mFootViewHight, 0, 0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownY = (int) ev.getY();
			return true;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_MOVE:
			int currentY = (int) ev.getY();
			// 移动的距离
			int diff = currentY - mDownY;
			// 滑动之后的paddingTop
			int paddingTop = diff + (-mHeadViewHight);
			if (mFirstVisibleItem == 0 && paddingTop > (-mHeadViewHight)) {
				mHeadView.setPadding(0, paddingTop, 0, 0);
//				return true;
			}
			break;
		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
}
