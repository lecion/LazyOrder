package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * 侧滑能显示隐藏菜单的ListView
 *
 * @author comet
 */
public class SlideListView extends ListView {
    private int mFrontContentWidth;    //item中默认（不滑动）展现区域的宽度
    private int mBackContentWidth;    //item中隐藏区域的宽度
    private int mDownX;            // 按下点的x值
    private int mDownY;            // 按下点的y值

    private boolean isShownBackContent;    // 隐藏的内容是否已显示

    private ViewGroup mPointChild;    // 当前处理的item
    private int mPointPosition;     // 当前处理的item的position
    private long mPointId;     // 当前处理的item的id
    private LinearLayout.LayoutParams mLayoutParams;    // 当前处理的item的LayoutParams

    private OnItemClickListener mOnItemClickListener;


    public SlideListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                return performActionMove(ev);
            case MotionEvent.ACTION_UP:
                performActionUp(ev);
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 处理action_down事件
     *
     * @param ev
     */
    private void performActionDown(MotionEvent ev) {

        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        // 获取当前点的item
        mPointPosition = pointToPosition(mDownX, mDownY) - getFirstVisiblePosition();
        mPointChild = (ViewGroup) getChildAt(mPointPosition);
        mPointId = pointToRowId(mDownX, mDownY);
        // 获取各个区域的宽度
        if (mFrontContentWidth == 0 || mBackContentWidth == 0) {
            mFrontContentWidth = mPointChild.getChildAt(0).getWidth();
            mBackContentWidth = mPointChild.getChildAt(1).getWidth();
        }

        mLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0)
                .getLayoutParams();
        mLayoutParams.width = mFrontContentWidth;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    /**
     * 处理action_move事件
     *
     * @param ev
     * @return
     */
    private boolean performActionMove(MotionEvent ev) {
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();
        if (Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY)) {
            // 如果向左滑动
            if (nowX < mDownX) {
                // 如果已经显示完全，就不做任何处理
                if (isShownBackContent) return true;
                // 计算要偏移的距离
                int scroll = (mDownX - nowX);
                // 如果大于了隐藏区域的宽度， 则最大为隐藏区域的宽度
                if (scroll >= mBackContentWidth) {
                    scroll = mBackContentWidth;
                }
                // 重新设置leftMargin
                mLayoutParams.leftMargin = -scroll;
                mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);

                // 如果向右滑动
            } else {
                // 如果还未显示隐藏内容，就不做任何处理
                if (!isShownBackContent) return true;
                // 计算要偏移的距离
                int scroll = (nowX - mDownX);
                if (scroll >= mBackContentWidth) {
                    scroll = mBackContentWidth;
                }
                mLayoutParams.leftMargin = scroll - mBackContentWidth;
                mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
            }

            return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 处理action_up事件
     */
    private void performActionUp(MotionEvent ev) {
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();
        if (nowX == mDownX && nowY == mDownY && mOnItemClickListener != null && canClick()) {
            mOnItemClickListener.onItemClick(this, mPointChild, mPointPosition, mPointId);
            return;
        }

		/* 偏移量大于隐藏区域的一半，则显示
		* 否则恢复关闭
		*/
        if (-mLayoutParams.leftMargin >= mBackContentWidth / 2) {
            mLayoutParams.leftMargin = -mBackContentWidth;
            isShownBackContent = true;
        } else {
            turnToNormal();
        }

        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    /**
     * 变为正常状态
     */
    public void turnToNormal() {
        mLayoutParams.leftMargin = 0;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
        isShownBackContent = false;
    }

    /**
     * 当前是否可点击
     *
     * @return 是否可点击
     */
    public boolean canClick() {
        return !isShownBackContent;
    }

//    /**
//     * 兼容ScrollView或ListView嵌套
//     *
//     * @param widthMeasureSpec
//     * @param heightMeasureSpec
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(AdapterView adapterView, View view,
                                int position, long id);
    }
}
