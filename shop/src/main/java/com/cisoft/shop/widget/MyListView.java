package com.cisoft.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.cisoft.shop.R;


/**
 * 模仿KJListView写的可上下拉刷新的ListView
 * Created by comit on 10/23/14.
 */
public class MyListView extends ListView implements AbsListView.OnScrollListener {
    //用来保存Y坐标
	private float lastY = -1;

    //用于滚动视图
	private Scroller scroller;

    //用户滚动行为的监听器
    private AbsListView.OnScrollListener scrollListener;

	//供外界用来下拉刷新和加载更多时回调的监听器
	private OnRefreshListener listViewListener;

	private MyListViewHeader headerView;
	private RelativeLayout headerViewContent;

    //HeadView上次更新的时间
//    private TextView headerTimeView;

    //HeadView的高度
	private int headerViewHeight;

    //下拉刷新是否可用的标记位（默认可用）
	private boolean enablePullRefresh = true;

    //是否正处于正在刷新状态的标记位
    private boolean pullRefreshing = false;

	private MyListViewFooter footerView;

    //加载更多是否可用的标记位（默认不可用）
	private boolean enablePullLoad = false;

    //是否正处于加载更多状态的标记位
    private boolean pullLoading;

    //Footer是否准备好的标记位
	private boolean isFooterReady = false;
	
	//ListView Item的总数,用来检测是否在ListView的底部
	private int totalItemCount;

	private int scrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

    //滚动的时间间隔
	private final static int SCROLL_DURATION = 400;

    //上拉ListView底部而触发加载更多的距离
    private final static int PULL_LOAD_MORE_DELTA = 50;

    //屏幕和实际距离的缩放比例
	private final static float OFFSET_RADIO = 1.8f;

	public MyListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		scroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);

		headerView = new MyListViewHeader(context);
		headerViewContent = (RelativeLayout) headerView
				.findViewById(R.id.listview_header_content);
//		headerTimeView = (TextView) headerView
//				.findViewById(R.id.listview_header_time);
		addHeaderView(headerView);

		footerView = new MyListViewFooter(context);

		headerView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						headerViewHeight = headerViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (isFooterReady == false) {
			isFooterReady = true;
			addFooterView(footerView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * 设置下拉刷新是否可用
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		enablePullRefresh = enable;
		if (!enablePullRefresh) {
			headerViewContent.setVisibility(View.INVISIBLE);
		} else {
			headerViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置上拉加载更多是否可用
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		enablePullLoad = enable;
		if (!enablePullLoad) {
			footerView.hide();
			footerView.setOnClickListener(null);
		} else {
			pullLoading = false;
			footerView.show();
			footerView.setState(MyListViewFooter.STATE_NORMAL);
			footerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
		}
	}

    /**
     * 停止刷新（方便外界调用，做一次封装）
     */
    public void stopRefreshData() {
        stopRefresh();
        stopLoadMore();
    }


	/**
	 * 停止下拉刷新,重置HeadView的高度
	 */
	public void stopRefresh() {
		if (pullRefreshing == true) {
			pullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * 停止加载更多,重置FooterView的高度
	 */
	public void stopLoadMore() {
		if (pullLoading == true) {
			pullLoading = false;
			footerView.setState(MyListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * 设置最后一次下拉刷新的时间
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
//		headerTimeView.setText(time);
	}

	private void invokeOnScrolling() {
		if (scrollListener instanceof OnScrollListener) {
			OnScrollListener l = (OnScrollListener) scrollListener;
			l.onScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		headerView.setVisiableHeight((int) delta
				+ headerView.getVisiableHeight());
		if (enablePullRefresh && !pullRefreshing) {
			if (headerView.getVisiableHeight() > headerViewHeight) {
				headerView.setState(MyListViewHeader.STATE_READY);
			} else {
				headerView.setState(MyListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	private void resetHeaderHeight() {
		int height = headerView.getVisiableHeight();
		if (height == 0)
			return;
		if (pullRefreshing && height <= headerViewHeight) {
			return;
		}
		int finalHeight = 0;
		if (pullRefreshing && height > headerViewHeight) {
			finalHeight = headerViewHeight;
		}
		scrollBack = SCROLLBACK_HEADER;
		scroller.startScroll(0, height, 0, finalHeight - height,
                SCROLL_DURATION);
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = footerView.getBottomMargin() + (int) delta;
		if (enablePullLoad && !pullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				footerView.setState(MyListViewFooter.STATE_READY);
			} else {
				footerView.setState(MyListViewFooter.STATE_NORMAL);
			}
		}
		footerView.setBottomMargin(height);

	}

	private void resetFooterHeight() {
		int bottomMargin = footerView.getBottomMargin();
		if (bottomMargin > 0) {
			scrollBack = SCROLLBACK_FOOTER;
			scroller.startScroll(0, bottomMargin, 0, -bottomMargin,
                    SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		pullLoading = true;
		footerView.setState(MyListViewFooter.STATE_LOADING);
		if (listViewListener != null) {
			listViewListener.onLoadMore();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (lastY == -1) {
			lastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - lastY;
			lastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0
					&& (headerView.getVisiableHeight() > 0 || deltaY > 0)) {
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			}
            if (getLastVisiblePosition() == totalItemCount - 1
					&& (footerView.getBottomMargin() > 0 || deltaY < 0)) {
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			break;
		default:
			lastY = -1;
			if (getLastVisiblePosition() == totalItemCount - 1) {
				if (enablePullLoad
				    && footerView.getBottomMargin() > PULL_LOAD_MORE_DELTA
				    && !pullLoading) {
					startLoadMore();
				}
				resetFooterHeight();
			}
            if (getFirstVisiblePosition() == 0) {
                if (enablePullRefresh
                        && headerView.getVisiableHeight() > headerViewHeight) {
                    pullRefreshing = true;
                    headerView.setState(MyListViewHeader.STATE_REFRESHING);
                    if (listViewListener != null) {
                        listViewListener.onRefresh();
                    }
                }
                resetHeaderHeight();
            }
            break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			if (scrollBack == SCROLLBACK_HEADER) {
                headerView.setVisiableHeight(scroller.getCurrY());
			} else {
				footerView.setBottomMargin(scroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l) {
        scrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollListener != null) {
			scrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.totalItemCount = totalItemCount;
		if (scrollListener != null) {
			scrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setOnRefreshListener(OnRefreshListener l) {
		listViewListener = l;
	}


	public interface OnScrollListener extends AbsListView.OnScrollListener {
		public void onScrolling(View view);
	}


	public interface OnRefreshListener {
		public void onRefresh();

		public void onLoadMore();
	}

    public void showFooterLoading() {
        footerView.loading();
    }

    public void showFooterNormal() {
        footerView.normal();
    }
}
