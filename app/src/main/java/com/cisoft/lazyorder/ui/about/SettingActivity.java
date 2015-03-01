package com.cisoft.lazyorder.ui.about;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.BaseActivity;

public class SettingActivity extends BaseActivity {

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initWidget() {
        setTitle(R.string.title_activity_setting);
    }

}
