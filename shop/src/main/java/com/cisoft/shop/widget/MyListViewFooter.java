package com.cisoft.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cisoft.myapplication.R;


public class MyListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context context;

	private View contentView;
	private View progressBar;
	private TextView hintView;
	
	public MyListViewFooter(Context context) {
		super(context);
		initView(context);
	}
	
	public MyListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	
	public void setState(int state) {
		hintView.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		hintView.setVisibility(View.INVISIBLE);
		if (state == STATE_READY) {
			hintView.setVisibility(View.VISIBLE);
			hintView.setText(R.string.listview_footer_hint_ready);
		} else if (state == STATE_LOADING) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			hintView.setVisibility(View.VISIBLE);
			hintView.setText(R.string.listview_footer_hint_normal);
		}
	}
	
	public void setBottomMargin(int height) {
		if (height < 0) return ;
		LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
		lp.bottomMargin = height;
		contentView.setLayoutParams(lp);
	}
	
	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
		return lp.bottomMargin;
	}
	
	
	public void normal() {
		hintView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
	}
	
	
	public void loading() {
		hintView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}
	
	public void hide() {
		LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
		lp.height = 0;
		contentView.setLayoutParams(lp);
	}
	
	public void show() {
		LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		contentView.setLayoutParams(lp);
	}
	
	private void initView(Context context) {
		this.context = context;
		LinearLayout moreView = (LinearLayout)LayoutInflater.from(this.context).inflate(R.layout.listview_footer_layout, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		contentView = moreView.findViewById(R.id.listview_footer_content);
		progressBar = moreView.findViewById(R.id.listview_footer_progressbar);
		hintView = (TextView)moreView.findViewById(R.id.listview_footer_hint_textview);
	}
	
	
}
