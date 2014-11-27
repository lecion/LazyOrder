package com.cisoft.lazyorder.ui.usercenter;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.main.menu.MenuItemContent;

import org.kymjs.aframe.bitmap.utils.BitmapOperateUtil;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.widget.RoundImageView;

/**
 * Created by comet on 2014/11/23.
 */
public class UserCenterFragment extends MenuItemContent {

    @BindView(id = R.id.iv_blur_image_bg)
    private ImageView ivBlurImageBg;

    @BindView(id = R.id.iv_user_avatar)
    private RoundImageView ivUserAvatar;

    @BindView(id = R.id.update_nickname, click = true)
    private UserCenterItemView updateNickname;

    @BindView(id = R.id.manage_address, click = true)
    private UserCenterItemView manageAddress;

    @BindView(id = R.id.logout, click = true)
    private UserCenterItemView logout;

    @Override
    protected View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_user_center, null);

        return view;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);

        BitmapOperateUtil.SetMistyBitmap(ivBlurImageBg, BitmapFactory.decodeResource(getResources(), R.drawable.user_center_blur_bg));

        // 设置圆形颜色
        ivUserAvatar.setBorderOutsideColor(0xffffffff);
        // 设置圆形宽度
        ivUserAvatar.setBorderThickness(1);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.update_nickname:
                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
