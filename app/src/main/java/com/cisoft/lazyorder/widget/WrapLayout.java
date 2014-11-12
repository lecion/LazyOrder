package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lecion on 11/12/14.
 */
public class WrapLayout extends ViewGroup {
    public WrapLayout(Context context) {
        this(context, null);
    }

    public WrapLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int width = 0;
        int height = 0;

        int maxWidth = 0;
        int maxHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int childWidth = getChildWidth(child);
            int childHeight = getChildHeight(child);
            if ((maxWidth + childWidth) <= widthSize) {
                maxWidth += childWidth;
                maxHeight = Math.max(childHeight, maxHeight);
            } else {
                //下一行的宽度
                width += Math.max(widthSize, maxWidth);
                maxWidth = childWidth;
                height = maxHeight;
                //下一行的高度
                maxHeight = childHeight;
            }
            if (i + 1 == childCount) {
                width = Math.max(width, maxWidth);
                height += maxHeight;
            }

            width = widthMode == MeasureSpec.EXACTLY ? widthSize : width;
            height = heightMode == MeasureSpec.EXACTLY ? heightSize : height;
            setMeasuredDimension(width, height);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int parentWidth = getMeasuredWidth();
        int maxWidth = 0;

        int lastLeft = 0;
        int lastTop = 0;

        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);
            int childWidth = getChildWidth(child);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            lastLeft += lp.leftMargin;
            if (maxWidth == 0) {
                lastTop += lp.topMargin;
            }

            if (maxWidth + childWidth <= parentWidth) {
                maxWidth += childWidth;
            } else {
                maxWidth = 0;
            }
            child.layout(lastLeft, lastTop, lastLeft + child.getMeasuredWidth(), lastTop + child.getMeasuredHeight());
            lastLeft += child.getMeasuredWidth();
            if (maxWidth == 0) {
                lastLeft = 0;
                lastTop += child.getMeasuredHeight();
            }

        }
    }

    /**
     * 获取view的宽度，包含margin
     * @param v
     * @return
     */
    private int getChildWidth(View v) {
        MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
        return lp.leftMargin + lp.rightMargin + v.getMeasuredWidth();
    }

    /**
     * 获取view的高度，包含margin
     * @param v
     * @return
     */
    private int getChildHeight(View v) {
        MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
        return lp.topMargin + lp.bottomMargin + v.getMeasuredHeight();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
