package com.cisoft.lazyorder.widget.section;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import org.kymjs.kjframe.utils.DensityUtils;

/**
 * Created by comet on 2015/3/1.
 */
public abstract class SectionItemView extends RelativeLayout {

    protected ViewHolder vholder;

    public SectionItemView(Context context) {
        this(context, null);
    }

    public SectionItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typeArr = context.obtainStyledAttributes(attrs, R.styleable.SectionItemView);
        CharSequence name = typeArr.getString(R.styleable.SectionItemView_name);
        Drawable icon = typeArr.getDrawable(R.styleable.SectionItemView_icon);
        typeArr.recycle();

        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(context, 48.0f)));
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.section_list_item, null);

        vholder = new ViewHolder(view);
        vholder.name.setText(name);
        vholder.name.setTextColor(getResources().getColor(R.color.text_light_gray_color));
//        vholder.name.setTextColor(Color.BLACK);

        if (icon != null)
            renderIconView();
        renderExtensionView();
        addView(view);
    }

    private void renderIconView() {}

    public String getName() {
        return (String)this.vholder.name.getText();
    }

    protected abstract void renderExtensionView();

    public void setName(int name) {
        this.vholder.name.setText(name);
    }

    static class ViewHolder {

        FrameLayout extension;
        TextView name;

        public ViewHolder(View view) {
            this.name = ((TextView)view.findViewById(R.id.section_item_name));
            this.extension = ((FrameLayout)view.findViewById(R.id.section_item_ext));
        }
    }
}