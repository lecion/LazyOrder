package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cisoft.lazyorder.R;

/**
 *
 * 自定义的空视图组件
 *
 * Created by comet on 2014/10/25.
 */
public class EmptyView extends FrameLayout implements View.OnClickListener{

    private Context mContext;
    private ViewGroup mRootView;
    private ImageView mIvReload;
    private TextView mTvReload1;
    private TextView mTvReload2;
    private OnClickReloadListener mOnClickReloadListener;
    public static final int NO_NETWORK = 1;
    public static final int NO_DATA = 2;

    public EmptyView(Context context, ViewGroup rootView) {
        super(context);
        mContext = context;
        mRootView = rootView;
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this);
        initView();
        setNoDataState();
    }

    private void initView() {
        mIvReload = (ImageView) findViewById(R.id.iv_reload);
        mTvReload1 = (TextView) findViewById(R.id.tv_reload1);
        mTvReload2 = (TextView) findViewById(R.id.tv_reload2);
        setOnClickListener(this);
    }

    private void setNoNetworkState() {
        mIvReload.setVisibility(View.VISIBLE);
        mTvReload1.setVisibility(View.VISIBLE);
        mTvReload2.setVisibility(View.GONE);
        mIvReload.setImageResource(R.drawable.no_network);
        mTvReload1.setText(mContext.getText(R.string.no_network));
    }

    private void setNoDataState() {
        mIvReload.setVisibility(View.VISIBLE);
        mTvReload1.setVisibility(View.GONE);
        mTvReload2.setVisibility(View.VISIBLE);
        mIvReload.setImageResource(R.drawable.no_data);
        mTvReload2.setText(mContext.getText(R.string.no_record));
    }

    public void showEmptyView(int state) {
        switch (state) {
            case NO_NETWORK:
                setNoNetworkState();
                break;
            case NO_DATA:
                setNoDataState();
                break;
        }
        mRootView.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void hideEmptyView() {
        mRootView.removeView(this);
    }

    public void setOnClickReloadListener(OnClickReloadListener onClickReloadListener) {
        mOnClickReloadListener = onClickReloadListener;
    }

    @Override
    public void onClick(View view) {
        hideEmptyView();
        if(mOnClickReloadListener != null) {
            mOnClickReloadListener.onReload();
        }
    }

    public interface  OnClickReloadListener {
        public void onReload();
    }
}
