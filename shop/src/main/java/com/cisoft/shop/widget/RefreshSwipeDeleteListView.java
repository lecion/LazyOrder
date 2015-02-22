package com.cisoft.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
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

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private void initView(Context context) {
        header = LayoutInflater.from(context).inflate(R.layout.header_layout, this, false);
        this.addHeaderView(header);
        measureView(header);
    }

    private void measureView(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        Log.d("measureView", "不是空的" + lp.height + " " + lp.width);
    }

    private void hideHeader(int top) {
        header.setPadding(header.getPaddingLeft(), -top, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }


}
