package com.cisoft.lazyorder.ui.about;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.about.UpdateInfo;
import com.cisoft.lazyorder.core.about.AboutNetwork;
import com.cisoft.lazyorder.core.about.UpdateService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.finals.UrlConstants;
import com.cisoft.lazyorder.ui.common.WebViewActivity;
import com.cisoft.lazyorder.ui.main.menu.BaseMenuItemFragment;
import com.cisoft.lazyorder.util.DialogFactory;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;

/**
 * Created by comet on 2014/11/30.
 */
public class AboutUsFragment extends BaseMenuItemFragment {

    @BindView(id = R.id.tv_check_update, click = true)
    private TextView tvCheckUpdate;

    @BindView(id = R.id.tv_function_desc, click = true)
    private TextView tvfunctionDesc;

    @BindView(id = R.id.tv_submit_feedback, click = true)
    private TextView tvSubmitFeedback;

    private PackageInfo packageInfo;
    private AboutNetwork aboutNetwork;


    public static final String BR_CLRER_STATE_BAR = "com.android.broadcast.clearstatebar";


    @Override
    protected View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_about_us, null);

        return view;
    }


    @Override
    protected void initData() {
        //获取当前程序的版本信息
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //初始化接口数据提供类
        aboutNetwork = new AboutNetwork(getActivity());
    }


    @Override
    protected void initWidget(View parentView) {
        initialTitleBar();
    }

    /**
     * 初始化标题栏
     */
    private void initialTitleBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.title_fragment_about_us));
    }


    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_update:
                checkUpdate();
                break;
            case R.id.tv_submit_feedback:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.tv_function_desc:
                Intent funcDescIntent = new Intent(getActivity(), WebViewActivity.class);
                funcDescIntent.putExtra(WebViewActivity.CONENT_URL, UrlConstants.URL_FUNCTION_DESC_URL);
                startActivity(funcDescIntent);
                break;
        }
    }

    /**
     * 执行版本检查更新
     */
    private void checkUpdate() {
        final Dialog tipDialog = DialogFactory.createWaitToastDialog(getActivity(), "正在检查更新");
        tipDialog.show();

        //先从网络上获取到最新的版本信息
        aboutNetwork.obtainLastestVersionInfo(new AboutNetwork.OnLoadVersionInfoFinish() {
            @Override
            public void onSuccess(final UpdateInfo updateInfo) {
                tipDialog.dismiss();

                //根据比较版本号来检查是否有更新
                if (updateInfo.getVersionCode() > packageInfo.versionCode) {
                    //显示提示更新的对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("发现新版本：" + updateInfo.getVersionName());
                    builder.setMessage(Html.fromHtml(updateInfo.getUpdateContent()).toString());
                    builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            //开启一个服务用于在状态栏上显示下载
                            Intent intent = new Intent(getActivity(), UpdateService.class);
                            intent.putExtra(UpdateService.VERSION_NAME, updateInfo.getVersionName());
                            intent.putExtra(UpdateService.DOWNLOAD_URL, updateInfo.getDownloadUrl());
                            getActivity().startService(intent);

                            //注册一个广播用于监听当清除状态栏时停止下载
                            IntentFilter intentFilter = new IntentFilter();
                            intentFilter.addAction(BR_CLRER_STATE_BAR);
                            getActivity().registerReceiver(new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    String action = intent.getAction();
                                    if (action.equals(BR_CLRER_STATE_BAR)) {
                                        AlertDialog.Builder confirmDialogBuilder = new AlertDialog.Builder(getActivity());
                                        confirmDialogBuilder.setTitle("确认");
                                        confirmDialogBuilder.setMessage("您确定要取消下载吗？");
                                        confirmDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();

                                                getActivity().stopService(new Intent(getActivity(), UpdateService.class));
                                            }
                                        });
                                        confirmDialogBuilder.setNegativeButton("取消", null);
                                        confirmDialogBuilder.create().show();
                                    }
                                }

                            }, intentFilter);
                        }
                    });

                    builder.create().show();
                } else {
                    ViewInject.toast("当前已是最新版本");
                }
            }

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                tipDialog.dismiss();
                ViewInject.toast(errorMsg);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!getMenuOpenState()) {
            initialTitleBar();
            inflater.inflate(R.menu.menu_about, menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
