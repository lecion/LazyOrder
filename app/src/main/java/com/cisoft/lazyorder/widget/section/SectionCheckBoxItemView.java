package com.cisoft.lazyorder.widget.section;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import com.cisoft.lazyorder.R;
import org.kymjs.kjframe.utils.DensityUtils;

public class SectionCheckBoxItemView extends SectionItemView {

    private CheckBox checkBox;
    private OnCheckBoxClickListener listener;

    public SectionCheckBoxItemView(Context context) {
        super(context);
    }

    public SectionCheckBoxItemView(Context paramContext, AttributeSet attrs) {
        super(paramContext, attrs);
    }

    public SectionCheckBoxItemView(Context paramContext, AttributeSet attrs, int defStyle) {
        super(paramContext, attrs, defStyle);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    protected void renderExtensionView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.check_box_width),
                LayoutParams.WRAP_CONTENT);
        checkBox = new CheckBox(getContext());
        layoutParams.setMargins(DensityUtils.dip2px(getContext(), 14.0f), 0, 0, 0);
        checkBox.setBackgroundColor(0);
        checkBox.setButtonDrawable(R.drawable.slide_check_box);
        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                if (listener != null)
                    listener.onCheckBoxClick(SectionCheckBoxItemView.this, checkBox.isChecked());
            }
        });
        vholder.extension.addView(checkBox, layoutParams);
    }

    public void setChecked(boolean isChecked) {
        checkBox.setChecked(isChecked);
    }

    public void setListener(OnCheckBoxClickListener onCheckBoxClickListener) {
        listener = onCheckBoxClickListener;
    }

    public static abstract interface OnCheckBoxClickListener {

        public abstract void onCheckBoxClick(SectionCheckBoxItemView sectionCheckBoxItemView, boolean isChecked);

    }
}
