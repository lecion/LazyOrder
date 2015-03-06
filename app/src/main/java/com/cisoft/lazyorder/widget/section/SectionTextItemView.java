package com.cisoft.lazyorder.widget.section;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by comet on 2015/3/1.
 */
public class SectionTextItemView extends SectionItemView {

    private OnTextItemClickListener listener;
    private TextView textView;

    public SectionTextItemView(Context context) {
        this(context, null);
    }

    public SectionTextItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionTextItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        registerClickListener();
    }

    private void registerClickListener() {

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onTextItemClick(SectionTextItemView.this);
            }
        });
    }

    public String getText() {
        return (String)textView.getText();
    }

    public TextView getTextView() {
        return textView;
    }

    protected void renderExtensionView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView = new TextView(getContext());
        vholder.extension.addView(textView, layoutParams);
    }

    public void setListener(OnTextItemClickListener paramOnTextItemClickListener) {
        this.listener = paramOnTextItemClickListener;
    }

    public void setText(String paramString) {
        this.textView.setText(paramString);
    }

    public static abstract interface OnTextItemClickListener {

        public abstract void onTextItemClick(SectionTextItemView sectionTextItemView);

    }
}
