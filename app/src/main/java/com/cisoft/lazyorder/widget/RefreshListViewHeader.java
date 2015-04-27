package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cisoft.lazyorder.R;


public class RefreshListViewHeader extends LinearLayout {
	private LinearLayout container;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private TextView hintTextView;
	private int mState = STATE_NORMAL;

	private Animation rotateUpAnim;
	private Animation rotateDownAnim;
	
	private final int ROTATE_ANIM_DURATION = 180;
	
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
    public final static int STATE_REFRESH_COMPLETE = 3;
    public final static int STATE_REFRESH_FAIL = 4;

	public RefreshListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	public RefreshListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		container = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.view_listview_header, null);
		addView(container, lp);
		setGravity(Gravity.BOTTOM);

		arrowImageView = (ImageView)findViewById(R.id.listview_header_arrow);
		hintTextView = (TextView)findViewById(R.id.listview_header_hint_textview);
		progressBar = (ProgressBar)findViewById(R.id.listview_header_progressbar);
		
		rotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		rotateUpAnim.setFillAfter(true);
		rotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		rotateDownAnim.setFillAfter(true);
	}

	public void setState(int state) {
		if (state == mState) return ;
		
		if (state == STATE_REFRESHING) {	// 显示进度
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
		} else {	// 显示箭头图片
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
            if (state == STATE_REFRESH_COMPLETE) {
                arrowImageView.setImageResource(R.drawable.refresh_success);
            } else if (state == STATE_REFRESH_FAIL) {
                arrowImageView.setImageResource(R.drawable.refresh_fail);
            }
		}
		
		switch(state){
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				arrowImageView.startAnimation(rotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				arrowImageView.clearAnimation();
			}
            if (mState == STATE_REFRESH_COMPLETE) {
                arrowImageView.setImageResource(R.drawable.listview_arrow);
            }
			hintTextView.setText(R.string.listview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(rotateUpAnim);
				hintTextView.setText(R.string.listview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			hintTextView.setText(R.string.listview_header_hint_loading);
			break;
        case STATE_REFRESH_COMPLETE:
            hintTextView.setText(R.string.listview_header_hint_refresh_complete);
            break;
        case STATE_REFRESH_FAIL:
            hintTextView.setText(R.string.listview_header_hint_refresh_fail);
            break;
		}
		
		mState = state;
	}
	
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) container
				.getLayoutParams();
		lp.height = height;
		container.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return container.getHeight();
	}

}
