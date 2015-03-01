package com.cisoft.lazyorder.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.core.account.I_LoginStateObserver;
import com.cisoft.lazyorder.core.account.LoginStateObserver;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.ui.address.ManageAddressActivity;
import com.cisoft.lazyorder.util.Utility;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.helper.BitmapOperateUtil;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.widget.RoundImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2014/11/23.
 */
public class UserCenterActivity extends BaseActivity implements I_LoginStateObserver {

    @BindView(id = R.id.iv_blur_image_bg)
    private ImageView mIvBlurImageBg;
    @BindView(id = R.id.riv_user_face, click = true)
    private RoundImageView mRivUserFace;
    @BindView(id = R.id.tv_user_account, click = true)
    private TextView mTvUserAccount;
    @BindView(id = R.id.item_logout, click = true)
    private UserCenterItemView mItemLogout;
    @BindView(id = R.id.item_edit_password, click = true)
    private UserCenterItemView mItemEditPwd;
    @BindView(id = R.id.item_alter_phone_binding, click = true)
    private UserCenterItemView mItemAlterPhoneBinding;
    @BindView(id = R.id.item_manage_address, click = true)
    private UserCenterItemView mItemManageAddress;

    private AppContext mAppContext;
    private KJBitmap mKjBitmap;
    private Bitmap mLoadingBitmap;
    private List<View> loginStateViews;
    private List<View> notLoginStateViews;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_user_center);
    }

    @Override
    public void initData() {
        mAppContext = (AppContext)getApplication();
        loginStateViews = new ArrayList<View>();
        notLoginStateViews = new ArrayList<View>();
        LoginStateObserver.getInstance().attach(this);
        mKjBitmap = Utility.getKjBitmapInstance();
        mLoadingBitmap = BitmapFactory.decodeResource(mAppContext.getResources(), R.drawable.default_user_face);
    }

    @Override
    public void initWidget() {
        // 设置虚化的背景
        BitmapOperateUtil.SetMistyBitmap(mIvBlurImageBg,
                BitmapFactory.decodeResource(getResources(), R.drawable.user_center_blur_bg));
        // 设置圆角头像的边框
        mRivUserFace.setBorderOutsideColor(0xFFF2F2F2);

        loginStateViews.add(mItemLogout);
        loginStateViews.add(mItemEditPwd);
        loginStateViews.add(mItemManageAddress);
        loginStateViews.add(mItemAlterPhoneBinding);

        // 根据登录状态显示相应的内容
        if (mAppContext.isLogin()) {
            showLoginState();
        } else {
            showNotLoginState();
        }
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.item_manage_address:
                startActivity(new Intent(this, ManageAddressActivity.class));
                break;
            case R.id.item_logout:
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
                                    }
                                }).setNegativeButton(getString(R.string.btn_cancel), null)
                        .create().show();
                break;
            case R.id.tv_user_account:
                if (!mAppContext.isLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.riv_user_face:
                if (mAppContext.isLogin()) {
                    ViewInject.toast("目前不支持更换头像，攻城师正在加班制造");
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.item_edit_password:
                startActivity(new Intent(this, EditPasswordActivity.class));
                break;
            case R.id.item_alter_phone_binding:
                startActivity(new Intent(this, AlterPhoneVerifyActivity.class));
                break;
        }
    }


    /**
     * 显示未登录状态
     */
    private void showNotLoginState() {
        mRivUserFace.setImageResource(R.drawable.default_user_face);
        mTvUserAccount.setText(mAppContext.getText(R.string.text_please_login));

        for (View view : loginStateViews) {
            view.setVisibility(View.GONE);
        }
        for (View view : notLoginStateViews) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示登录状态
     */
    private void showLoginState() {
        User loginUser = mAppContext.getLoginInfo();
//        mKjBitmap.display(mRivUserFace, loginUser.getUserFaceUrl(), mLoadingBitmap);
        mRivUserFace.setImageResource(R.drawable.default_user_face);
        mTvUserAccount.setText(mAppContext.getLoginAccount());

        for (View view : loginStateViews) {
            view.setVisibility(View.VISIBLE);
        }
        for (View view : notLoginStateViews) {
            view.setVisibility(View.GONE);
        }
    }


    @Override
    public void onLoginSateChange() {
        // 根据登录状态显示相应的内容
        if (mAppContext.isLogin()) {
            showLoginState();
        } else {
            showNotLoginState();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoginStateObserver.getInstance().detach(this);
    }

    public static void startFrom(Activity activity) {
        Intent i = new Intent(activity, UserCenterActivity.class);
        activity.startActivity(i);
    }
}
