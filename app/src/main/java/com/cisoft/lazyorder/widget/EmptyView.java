package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cisoft.lazyorder.R;

import org.kymjs.kjframe.utils.StringUtils;

import java.util.Stack;

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

    private Stack<View> mVisibleViews;
    private OnClickReloadListener mOnClickReloadListener;

    public static final int NO_NETWORK = 1;
    public static final int NO_DATA = 2;

    public EmptyView(Context context, ViewGroup rootView) {
        super(context);
        mContext = context;
        mRootView = rootView;
        LayoutInflater.from(context).inflate(R.layout.view_empty_view, this);
        initData();
        initWidget();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mVisibleViews = new Stack<View>();
    }


    /**
     * 初始化组件
     */
    private void initWidget() {
        mIvReload = (ImageView) findViewById(R.id.iv_reload);
        mTvReload1 = (TextView) findViewById(R.id.tv_reload1);
        mTvReload2 = (TextView) findViewById(R.id.tv_reload2);
        setOnClickListener(this);
        // 默认设置为无数据的空视图状态
        setNoDataState(0, null);
    }

    /**
     * 显示指定类型的空视图
     * @param state
     */
    public void showEmptyView(int state) {
        showEmptyView(state, 0, null);
    }

    /**
     * 显示指定类型、提示图片和提示文字的空视图
     * @param state
     * @param imgResId
     * @param text
     */
    public void showEmptyView(int state, int imgResId, String text) {
        switch (state) {
            case NO_NETWORK:
                setNoNetworkState(imgResId, text);
                break;
            case NO_DATA:
                setNoDataState(imgResId, text);
                break;
        }
        View view = null;
        for (int i = 0; i < mRootView.getChildCount(); i++){
            view = mRootView.getChildAt(i);
            if(view.getVisibility() == View.VISIBLE){
                mVisibleViews.push(view);
                view.setVisibility(View.GONE);
            }
        }
        mRootView.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }


    /**
     * 隐藏空视图
     */
    public void hideEmptyView() {
        mRootView.removeView(this);
        View view = null;
        for (int i = 0; i < mVisibleViews.size(); i++){
            view = mVisibleViews.pop();
            view.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 设置为网络问题的空视图状态
     */
    private void setNoNetworkState(int imgResId, String text) {
        mIvReload.setVisibility(View.VISIBLE);
        mTvReload1.setVisibility(View.VISIBLE);
        mTvReload2.setVisibility(View.GONE);

        // 若指定了图片，则使用指定图片;反之则使用默认的
        if (imgResId != 0) {
            mIvReload.setImageResource(imgResId);
        } else {
            mIvReload.setImageResource(R.drawable.no_network);
        }

        // 若指定了提示文字，则使用指定文字;反之则使用默认的
        if (!StringUtils.isEmpty(text)) {
            mTvReload1.setText(text);
        } else {
            mTvReload1.setText(mContext.getText(R.string.text_no_network));
        }
    }

    /**
     * 设置为无数据的空视图状态
     */
    private void setNoDataState(int imgResId, String text) {
        mIvReload.setVisibility(View.VISIBLE);
        mTvReload1.setVisibility(View.GONE);
        mTvReload2.setVisibility(View.VISIBLE);

        // 若指定了图片，则使用指定图片;反之则使用默认的
        if (imgResId != 0) {
            mIvReload.setImageResource(imgResId);
        } else {
            mIvReload.setImageResource(R.drawable.no_data);
        }

        // 若指定了提示文字，则使用指定文字;反之则使用默认的
        if (!StringUtils.isEmpty(text)) {
            mTvReload2.setText(text);
        } else {
            mTvReload2.setText(mContext.getText(R.string.text_no_record));
        }
    }

    /**
     * 设置重新加载的监听器
     * @param onClickReloadListener
     */
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

    /**
     * 供外界重新加载的监听器
     */
    public interface  OnClickReloadListener {
        public void onReload();
    }
}
