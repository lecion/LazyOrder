package com.cisoft.lazyorder.ui.account;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cisoft.lazyorder.R;

/**
 * Created by comet on 2014/11/26.
 */
public class UserCenterItemView extends LinearLayout {

    private ImageView mIvItemIcon;
    private TextView mTvItemTitle;

    public UserCenterItemView(Context context) {
        this(context, null, 0);
    }

    public UserCenterItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserCenterItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.fragment_user_center_item, this, true);
        setBackgroundResource(R.drawable.selector_list_cell);

        TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.userCenterItemAttrs);
        String itemTitle = localTypedArray.getString(R.styleable.userCenterItemAttrs_item_title);
        Drawable itemIcon = localTypedArray.getDrawable(R.styleable.userCenterItemAttrs_item_icon);
        localTypedArray.recycle();

        mIvItemIcon = (ImageView) findViewById(R.id.iv_item_icon);
        mTvItemTitle = (TextView) findViewById(R.id.iv_item_title);
        mIvItemIcon.setImageDrawable(itemIcon);
        mTvItemTitle.setText(itemTitle);
    }
}
