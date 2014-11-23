package ayr.cc.dc.pullrefreshlistview.custom;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
	/** 刷新的箭头 **/
	private ImageView iv_listview_header_arrow = null;
	/** 头部正在加载的progresssbar **/
	private ProgressBar iv_listview_header_progress = null;
	/** 头部的状态 **/
	private TextView tv_listview_header_state;
	/** 最后刷新的时间 **/
	private TextView tv_listview_header_last_update_time;

	/** 正常状态 **/
	private final int NORMAL = 0;
	/** 下拉刷新 **/
	private final int DOWN_PULL_REFRESH = 1;
	/** 松开刷新 **/
	private final int RELEASE_REFRESH = 2;
	/** 正在刷新 **/
	private final int REFRESHING = 3;
	/** 当前状态 **/
	private int currentRefreshState = NORMAL;
	/**下拉刷新的动画**/
	private Animation upAnimation;
	/**松开刷新的动画**/
	private Animation downAnimation;

	/**是否记录当前的按下信息**/
	private boolean isRemark = false;
	private int mScrollState;
	public PullRefreshListView(Context context) {
		super(context);
		initView(context);
	}

	public PullRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PullRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context){
		mContext = context;
		initHead();
//		initFoot();
		this.setOnScrollListener(this);
	}

	/**
	 * 初始化头部
	 */
	private void initHead() {
		mHeadView = LayoutInflater.from(mContext).inflate(
				R.layout.listview_head, null);
		// 获取mHeadView的高度
		measureView(mHeadView);
		mHeadViewHight = mHeadView.getMeasuredHeight();
		// 隐藏mHeadView
		paddTop(-mHeadViewHight);
		iv_listview_header_arrow = (ImageView) mHeadView
				.findViewById(R.id.iv_listview_header_arrow);
		iv_listview_header_progress = (ProgressBar) mHeadView
				.findViewById(R.id.iv_listview_header_progress);
		tv_listview_header_state = (TextView) mHeadView
				.findViewById(R.id.tv_listview_header_state);
		tv_listview_header_last_update_time = (TextView) mHeadView
				.findViewById(R.id.tv_listview_header_last_update_time);

		initAnimation();
		
		this.addHeaderView(mHeadView);
	}
	
	private void paddTop(int height){
		mHeadView.setPadding(mHeadView.getPaddingLeft(),height, mHeadView.getPaddingRight(), 
				mHeadView.getPaddingBottom());
		mHeadView.invalidate();
	}
	
	/**
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}

	/***
	 * 初始化动画
	 */
	private void initAnimation() {
		upAnimation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(500);
		upAnimation.setFillAfter(true);// 动画执行完成之后,维持

		downAnimation = new RotateAnimation(-180f, -360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		downAnimation.setDuration(500);
		downAnimation.setFillAfter(true);
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
			if(mFirstVisibleItem==0){
				isRemark  = true;
				mDownY = (int) ev.getY();
				Log.d(TAG,""+mFirstVisibleItem+"---"+mDownY);
			}
				
			break;
		case MotionEvent.ACTION_MOVE:
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP:
			if(currentRefreshState==RELEASE_REFRESH){
				currentRefreshState = REFRESHING;
				refreshHeadView();
				//刷新数据
				mLRefreshData.downPullRefresh();
			}else if(currentRefreshState==DOWN_PULL_REFRESH){
				currentRefreshState = NORMAL;
				isRemark = false;
				refreshHeadView();
			}
			break;
		}

		return super.onTouchEvent(ev);
	}

	private void onMove(MotionEvent ev) {
		if(!isRemark){
			return;
		}
		int moveY = (int) ev.getY();
		int offsetY = moveY-mDownY;
		Log.d(TAG, "moveY:"+moveY+"----"+offsetY+"---mDownY:"+mDownY);
		int topPadding = offsetY - mHeadViewHight;
		switch (currentRefreshState) {
		case NORMAL:
			if(offsetY>0){
				currentRefreshState = DOWN_PULL_REFRESH;
				refreshHeadView();
			}
			break;
		case DOWN_PULL_REFRESH:
			paddTop(topPadding);
			if(offsetY>=(mHeadViewHight+30)&&mScrollState==SCROLL_STATE_TOUCH_SCROLL){
				currentRefreshState = RELEASE_REFRESH;
				refreshHeadView();
			}
			break;
		case RELEASE_REFRESH:
			paddTop(topPadding);
//			Log.d(TAG, ""+offsetY+"---"+mHeadViewHight);
			if(offsetY<=(mHeadViewHight+30)){
				currentRefreshState = DOWN_PULL_REFRESH;
				refreshHeadView();
			}else if(offsetY<=0){
				currentRefreshState  =NORMAL;
				isRemark = false;
				refreshHeadView();
			}
			break;
		default:
			break;
		}
	}

	private void refreshHeadView() {
		switch (currentRefreshState) {
		case NORMAL:
			iv_listview_header_arrow.clearAnimation();
			paddTop(-mHeadViewHight);
			break;
		case DOWN_PULL_REFRESH:
			tv_listview_header_state.setText("下拉刷新");
			iv_listview_header_arrow.setVisibility(View.VISIBLE);
			iv_listview_header_progress.setVisibility(View.GONE);
			iv_listview_header_arrow.clearAnimation();
			iv_listview_header_arrow.setAnimation(upAnimation);
			break;
		case RELEASE_REFRESH:
			tv_listview_header_state.setText("松开刷新");
			iv_listview_header_arrow.setVisibility(View.VISIBLE);
			iv_listview_header_progress.setVisibility(View.GONE);
			iv_listview_header_arrow.clearAnimation();
			iv_listview_header_arrow.setAnimation(downAnimation);
			break;
		case REFRESHING:
			paddTop(50);
			iv_listview_header_arrow.clearAnimation();
			iv_listview_header_progress.setVisibility(View.VISIBLE);
			iv_listview_header_arrow.setVisibility(View.GONE);
			tv_listview_header_state.setText("正在刷新...");
			break;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mScrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
	}
	
	/**
	 * ListView下拉刷新,上拉加载接口
	 * @author DC
	 *
	 */
	public interface ListViewRefreshData{
		public void downPullRefresh();
	}
	
	private ListViewRefreshData mLRefreshData = null;

	public void setListViewRefreshData(ListViewRefreshData listViewRefreshData){
		this.mLRefreshData = listViewRefreshData;
	}
	
	public void downPullRefreshComplete(){
		isRemark = false;
		currentRefreshState = NORMAL;
		refreshHeadView();
		//更新时间
		SimpleDateFormat format = new SimpleDateFormat("yyy年mm月dd日 hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String dateStr = format.format(date);
		tv_listview_header_last_update_time.setText(dateStr);
	}
}
