package com.cisoft.lazyorder.ui.about;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import com.cisoft.lazyorder.AppConfig;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.core.about.AboutNetwork;
import com.cisoft.lazyorder.core.account.LoginStateObserver;
import com.cisoft.lazyorder.finals.SPConstants;
import com.cisoft.lazyorder.finals.UrlConstants;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.ui.account.AboutActivity;
import com.cisoft.lazyorder.ui.common.WebViewActivity;
import com.cisoft.lazyorder.widget.section.SectionCheckBoxItemView;
import com.cisoft.lazyorder.widget.section.SectionTextItemView;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.DensityUtils;
import org.kymjs.kjframe.utils.PreferenceHelper;


public class SettingActivity extends BaseActivity
        implements SectionCheckBoxItemView.OnCheckBoxClickListener, SectionTextItemView.OnTextItemClickListener{

    @BindView(id = R.id.clear_cache_setting)
    private SectionTextItemView clearCacheSettingView;
    @BindView(id = R.id.feedback_setting)
    private SectionTextItemView feedbackSettingView;
    @BindView(id = R.id.check_update_setting)
    private SectionTextItemView checkUpdateSettingView;
    @BindView(id = R.id.no_drawing_mode_setting)
    private SectionCheckBoxItemView noDrawingModeSettingView;
    @BindView(id = R.id.about_us_setting)
    private SectionTextItemView aboutUsSettingView;
    @BindView(id = R.id.function_desc_setting)
    private SectionTextItemView functionDescSettingView;
    @BindView(id = R.id.welcome_page_setting)
    private SectionTextItemView welcomePageSettingView;


    @BindView(id = R.id.logout_setting)
    private SectionTextItemView logoutSettingView;
    private AppConfig mAppConfig;
    private AppContext mAppContext;
    private String mCurrentVersionStr;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_setting);
    }


    @Override
    public void initData() {
        mAppContext = (AppContext) getApplication();
        mAppConfig = AppConfig.getAppConfig(this);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mCurrentVersionStr = getString(R.string.settings_version_current, packageInfo.versionName);
            updateVersionState();
            return;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            throw new RuntimeException(nameNotFoundException);
        }
    }

    @Override
    public void initWidget() {
        setTitle(R.string.title_activity_setting);
        clearCacheSettingView.setListener(this);
        feedbackSettingView.setListener(this);
        checkUpdateSettingView.setListener(this);
        noDrawingModeSettingView.setChecked(mAppConfig.enableNoDrawingMode());
        noDrawingModeSettingView.setListener(this);
        aboutUsSettingView.setListener(this);
        functionDescSettingView.setListener(this);
        welcomePageSettingView.setListener(this);
        logoutSettingView.setListener(this);
        if (!mAppContext.isLogin()) {
            logoutSettingView.setVisibility(View.GONE);
        }
    }

    private void updateVersionState() {
        checkUpdateSettingView.getTextView().setTextSize(11.0f);
        checkUpdateSettingView.getTextView().setGravity(View.FOCUS_LEFT);
        String latestVersionName = PreferenceHelper.readString(this, SPConstants.SP_FILE_NAME,
                SPConstants.KEY_LATEST_VERSION_NAME, null);
        if (latestVersionName != null) {
            checkUpdateSettingView.setText("New");
            checkUpdateSettingView.getTextView().setTextColor(getResources().getColor(R.color.white));
            checkUpdateSettingView.getTextView().setPadding(DensityUtils.dip2px(this, 4.0f), 0,
                    DensityUtils.dip2px(this, 4.0f), 0);
            checkUpdateSettingView.getTextView().setBackgroundResource(R.drawable.ic_unread_count_bg);
        } else {
            checkUpdateSettingView.setText(mCurrentVersionStr);
            checkUpdateSettingView.getTextView().setTextColor(getResources().getColor(R.color.color_99));
            checkUpdateSettingView.getTextView().setBackgroundDrawable(null);
        }
    }

    public void onCheckBoxClick(SectionCheckBoxItemView view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.no_drawing_mode_setting:
                enableNoDrawingMode(isChecked);
                break;
        }
    }

    private void enableNoDrawingMode(boolean enable) {
        mAppConfig.setEnableNoDrawingMode(enable);
    }

    public void onTextItemClick(SectionTextItemView sectionTextItemView) {
        switch (sectionTextItemView.getId()) {
            case R.id.clear_cache_setting:
                // TODO
                break;
            case R.id.feedback_setting:
                FeedbackActivity.startFrom(this);
                break;
            case R.id.check_update_setting:
                new AboutNetwork(this).checkVersionUpdate();
                break;
            case R.id.about_us_setting:
                AboutActivity.startFrom(this);
                break;
            case R.id.function_desc_setting:
                WebViewActivity.startFrom(this, getString(R.string.settings_title_function_desc),
                        UrlConstants.URL_FUNCTION_DESC_URL);
                break;
            case R.id.welcome_page_setting:
                //TODO
                break;
            case R.id.logout_setting:
                logout();
                break;
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_confirm_logout_content))
                .setPositiveButton(getString(R.string.btn_confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mAppContext.logout();
                                LoginStateObserver.getInstance().notifyStateChanged();
                                ViewInject.toast(getString(R.string.toast_success_to_logout));
                                SettingActivity.this.finish();
                            }
                        }).setNegativeButton(getString(R.string.btn_cancel), null)
                .create().show();
    }

    public static void startFrom(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult(intent, 11);
    }
}
