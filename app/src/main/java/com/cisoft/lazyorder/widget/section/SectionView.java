package com.cisoft.lazyorder.widget.section;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by comet on 2015/3/1.
 */
public class SectionView extends LinearLayout {

    private CharSequence footerText;
    private LayoutInflater inflater;

    public SectionView(Context context) {
        this(context, null);
    }

    public SectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typeArr = context.obtainStyledAttributes(attrs, R.styleable.SectionView);
        footerText = typeArr.getString(R.styleable.SectionView_footerText);
        typeArr.recycle();

        inflater = LayoutInflater.from(context);
        setOrientation(VERTICAL);
        setPadding(0, context.getResources().getDimensionPixelOffset(R.dimen.section_space), 0, 0);
    }

    protected void onFinishInflate() {
        if (!StringUtils.isEmpty(footerText)) {
            TextView tvFooterText = (TextView)inflater.inflate(R.layout.section_list_footer, null);
            tvFooterText.setText(footerText);
            addView(tvFooterText);
        }
    }
}
