package com.cisoft.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.cisoft.myapplication.R;

/**
 * Created by Lecion on 2/22/15.
 */
public class RefreshSwipeDeleteListView extends ListView implements AbsListView.OnScrollListener {
    View header;
    int headerHeight;
    private int lastY;
    private int firstVisibleItem;
    private boolean startFlag;
    private int scrollState;
    State state = State.NORMAL;

    enum State {
        NORMAL, PULL, RELEASE, REFRESHING;
    }

    public RefreshSwipeDeleteListView(Context context) {
        this(context, null);
    }

    public RefreshSwipeDeleteListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshSwipeDeleteListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    private void initView(Context context) {
        header = LayoutInflater.from(context).inflate(R.layout.header_layout, this, false);
        measureView(header);
        headerHeight = header.getMeasuredHeight();
        moveHeader(headerHeight);
        this.addHeaderView(header);
    }

    private void measureView(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int widthMeasureSpec = getChildMeasureSpec(0, 0, lp.width);
        int heightMeasureSpec;
        int tempHeight = lp.height;
        if (tempHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }


    private void moveHeader(int top) {
        header.setPadding(header.getPaddingLeft(), -top, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    startFlag = true;
                }
                //获得上一次落点Y
                lastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                startFlag = false;
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 处理move
     *
     * @param ev
     */
    private void onMove(MotionEvent ev) {
        int y = (int) ev.getY();
        int deltaY = y - lastY;
        switch (state) {
            case NORMAL:
                if (deltaY > 0 && startFlag && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = State.PULL;
                    //TODO 从普通状态变成下拉刷新状态
                    moveHeader(deltaY);
                }
                break;
            case PULL:
                moveHeader(deltaY);
                break;
            case RELEASE:
                break;
            case REFRESHING:
                break;
        }
    }

}
