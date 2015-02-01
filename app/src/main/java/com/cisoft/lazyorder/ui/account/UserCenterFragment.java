package com.cisoft.lazyorder.ui.account;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.core.account.I_LoginStateObserver;
import com.cisoft.lazyorder.core.account.LoginStateObserver;
import com.cisoft.lazyorder.ui.address.ManageAddressActivity;
import com.cisoft.lazyorder.ui.main.menu.MenuItemContent;

import org.kymjs.kjframe.bitmap.helper.BitmapOperateUtil;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.widget.RoundImageView;

/**
 * Created by comet on 2014/11/23.
 */
public class UserCenterFragment extends MenuItemContent implements I_LoginStateObserver {
    @BindView(id = R.id.ll_logined_content)
    private LinearLayout mLlLoginedContent;

    @BindView(id = R.id.ll_not_login_content)
    private LinearLayout mLlNotLoginContent;

    @BindView(id = R.id.iv_blur_image_bg)
    private ImageView mIvBlurImageBg;

    @BindView(id = R.id.iv_user_avatar)
    private RoundImageView mIvUserAvatar;

    @BindView(id = R.id.tv_user_nickname)
    private TextView mTvUserNickname;

    @BindView(id = R.id.tv_login, click = true)
    private TextView login;

    @BindView(id = R.id.item_logout, click = true)
    private UserCenterItemView mItemLogout;

    @BindView(id = R.id.item_manage_address, click = true)
    private UserCenterItemView mItemManageAddress;

    private AppContext appContext;

    @Override
    protected View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_user_center, null);

        return view;
    }

    @Override
    protected void initData() {
        LoginStateObserver.getInstance().attach(this);
    }

    @Override
    protected void initWidget(View parentView) {

        //设置虚化的背景
        BitmapOperateUtil.SetMistyBitmap(mIvBlurImageBg,
                BitmapFactory.decodeResource(getResources(), R.drawable.user_center_blur_bg));
        //设置圆角头像的边框
        mIvUserAvatar.setBorderOutsideColor(0xffffffff);
        mIvUserAvatar.setBorderThickness(2);
        //根据登录状态显示相应的内容
        displayByLoginState();
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.item_manage_address:
                startActivity(new Intent(getActivity(), ManageAddressActivity.class));
                break;
            case R.id.item_logout:
                appContext.logout();
                ViewInject.toast("退出登录成功");
                break;
            case R.id.tv_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    /**
     * 根据登录状态统一操作显示内容
     */
    private void displayByLoginState() {
        //判断是否登录
        appContext = (AppContext) getActivity().getApplication();
        if (appContext.isLogin()) {
            displayLoginedContent();
        } else {
            displayNotLoginContent();
        }
    }

    /**
     * 显示登录后的内容
     */
    private void displayLoginedContent() {
        mLlLoginedContent.setVisibility(View.VISIBLE);
        mLlNotLoginContent.setVisibility(View.GONE);
        mItemLogout.setVisibility(View.VISIBLE);
        mItemManageAddress.setVisibility(View.VISIBLE);
        mTvUserNickname.setText(appContext.getLoginPhoneNum());
    }


    /**
     * 显示未登录的内容
     */
    private void displayNotLoginContent() {
        mLlLoginedContent.setVisibility(View.GONE);
        mLlNotLoginContent.setVisibility(View.VISIBLE);
        mItemLogout.setVisibility(View.GONE);
        mItemManageAddress.setVisibility(View.GONE);
    }


    @Override
    public void onLoginSateChange() {
        displayByLoginState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoginStateObserver.getInstance().detach(this);
    }
}
