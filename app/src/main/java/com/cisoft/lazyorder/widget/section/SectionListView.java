package com.cisoft.lazyorder.widget.section;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.cisoft.lazyorder.R;
import org.kymjs.kjframe.utils.DensityUtils;

/**
 * Created by comet on 2015/3/1.
 */
public class SectionListView extends LinearLayout {

    public SectionListView(Context context) {
        this(context, null);
    }

    public SectionListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.section_list_bg);
        int i = DensityUtils.dip2px(context, 1.0f);
        setPadding(0, i, 0, i);
    }

    private void hideLastItemLine(int childCount) {
        if (childCount > 0) {
            RelativeLayout lastChildView = (RelativeLayout) getChildAt(childCount - 1);
            lastChildView.findViewById(R.id.section_item_line).setVisibility(View.GONE);
        }
    }

    protected void onFinishInflate() {
        hideLastItemLine(getChildCount());
    }

    public void refresh() {
        int i = getChildCount();
        if (i > 0) {
            int j = 0;
            for (int k = 0; k < i; k++)
            {
                RelativeLayout childView = (RelativeLayout)getChildAt(k);
                childView.findViewById(R.id.section_item_line).setVisibility(VISIBLE);
                if (childView.getVisibility() != View.GONE)
                    j++;
            }
            hideLastItemLine(j);
        }
    }
}