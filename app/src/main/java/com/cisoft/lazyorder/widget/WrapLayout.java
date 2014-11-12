package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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

    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;
        // 存储每一行所有的childView
        List<View> lineViews = new ArrayList<View>();
        int cCount = getChildCount();
        // 遍历所有的孩子
        for (int i = 0; i < cCount; i++)
        {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果已经需要换行
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width)
            {
                // 记录这一行所有的View以及最大高度
                mLineHeight.add(lineHeight);
                // 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
                mAllViews.add(lineViews);
                lineWidth = 0;// 重置行宽
                lineViews = new ArrayList<View>();
            }
            /**
             * 如果不需要换行，则累加
             */
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);
        }
        // 记录最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        int left = 0;
        int top = 0;
        // 得到总行数
        int lineNums = mAllViews.size();
        for (int i = 0; i < lineNums; i++)
        {
            // 每一行的所有的views
            lineViews = mAllViews.get(i);
            // 当前行的最大高度
            lineHeight = mLineHeight.get(i);

            Log.e("", "第" + i + "行 ：" + lineViews.size() + " , " + lineViews);
            Log.e("", "第" + i + "行， ：" + lineHeight);

            // 遍历当前行所有的View
            for (int j = 0; j < lineViews.size(); j++)
            {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE)
                {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                //计算childView的left,top,right,bottom
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc =lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                Log.e("", child + " , l = " + lc + " , t = " + t + " , r ="
                        + rc + " , b = " + bc);

                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.rightMargin
                        + lp.leftMargin;
            }
            left = 0;
            top += lineHeight;
        }





//        for (int i = 0; i < childCount; i++) {
//
//            View child = getChildAt(i);
//            int childWidth = getChildWidth(child);
//            int childHeight = getChildHeight(child);
//            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//
//            lastLeft += lp.leftMargin;
//            if (maxWidth == 0) {
//                lastTop += lp.topMargin;
//            }
//
//            if (maxWidth + childWidth <= parentWidth) {
//                maxWidth += childWidth;
//                maxHeight = Math.max(child.getMeasuredHeight(), maxHeight);
//            } else {
//                maxWidth = 0;
//                maxHeight += child.getMeasuredHeight();
//            }
//            Log.d("onLayout", "child" + i + " l " + lastLeft + " t " + lastTop + " r " + (lastLeft + child.getMeasuredWidth()) + " b " + (lastTop + child.getMeasuredHeight()) + " maxWidth " + maxWidth);
//            child.layout(lastLeft, lastTop, lastLeft + child.getMeasuredWidth(), lastTop + child.getMeasuredHeight());
//            lastLeft += child.getMeasuredWidth() + lp.rightMargin;
//            if (maxWidth == 0) {
//                lastLeft = 0;
//                lastTop += maxHeight + lp.bottomMargin;
//            }
//
//        }
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

    class Line {
        private int height;
        private List<View> line;

        Line(int height, List line) {
            this.height = height;
            this.line = line;
        }

        Line() {
            height = 0;
            line = new ArrayList<View>();
        }

        public void add(View v) {
            line.add(v);
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public int size() {
            return line.size();
        }

        public View get(int i) {
            return line.get(i);
        }

        public void clear() {
            this.line.clear();
            this.height = 0;
        }
    }
}
