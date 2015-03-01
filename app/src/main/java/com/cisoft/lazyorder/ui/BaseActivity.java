package com.cisoft.lazyorder.ui;

import android.app.ActionBar;
import android.graphics.drawable.BitmapDrawable;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import org.kymjs.kjframe.KJActivity;

/**
 * Created by comet on 2015/2/18.
 */
public abstract class BaseActivity extends KJActivity{

    private TextView tvTitleView;

    public void setContentView(int viewId) {
        super.setContentView(viewId);
        initTitleView();
    }

    public void setContentView(View view) {
        super.setContentView(view);
        initTitleView();
    }

    public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
        super.setContentView(paramView, paramLayoutParams);
        initTitleView();
    }

    public void initTitleView() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.action_bar_title_item);
            tvTitleView = (TextView)actionBar.getCustomView()
                    .findViewById(R.id.action_bar_title);
            tvTitleView.setText(getTitle());
        }
    }

    protected TextView getTitleView(){
        if (tvTitleView == null)
            initTitleView();
        return tvTitleView;
    }

    public void setTitle(int title) {
        setTitle(getString(title));
    }

    protected void setTitle(String title) {
        getTitleView().setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
