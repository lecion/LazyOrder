package com.cisoft.lazyorder.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.BaseActivity;
import org.kymjs.kjframe.ui.BindView;

public class AboutActivity extends BaseActivity {

    @BindView(id = R.id.tv_version_name)
    private TextView tvVersionName;

    private PackageInfo packageInfo;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_about);
    }

    @Override
    public void initData() {
        //获取当前程序的版本信息
        try {
            packageInfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initWidget() {
        tvVersionName.setText(getString(R.string.app_name) + "" + packageInfo.versionName);
    }

    public static void startFrom(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }
}
