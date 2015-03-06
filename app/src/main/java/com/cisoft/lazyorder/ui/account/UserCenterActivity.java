package com.cisoft.lazyorder.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.account.User;
import com.cisoft.lazyorder.core.account.I_LoginStateObserver;
import com.cisoft.lazyorder.core.account.LoginStateObserver;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.ui.about.SettingActivity;
import com.cisoft.lazyorder.ui.address.ManageAddressActivity;
import com.cisoft.lazyorder.util.Utility;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.helper.BitmapOperateUtil;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.widget.RoundImageView;

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

    @BindView(id = R.id.item_edit_password, click = true)
    private UserCenterItemView mItemEditPwd;
    @BindView(id = R.id.item_alter_phone_binding, click = true)
    private UserCenterItemView mItemAlterPhoneBinding;
    @BindView(id = R.id.item_manage_address, click = true)
    private UserCenterItemView mItemManageAddress;

    private AppContext mAppContext;
    private KJBitmap mKjBitmap;
    private Bitmap mLoadingBitmap;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_user_center);
    }

    @Override
    public void initData() {
        mAppContext = (AppContext)getApplication();
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

        // 根据登录状态显示相应的内容
        initUserInfoByLoginState();
    }


    public void initUserInfoByLoginState() {
        if (mAppContext.isLogin()) {
            mTvUserAccount.setText(mAppContext.getLoginAccount());
            Utility.getKjBitmapInstance().display(mRivUserFace,
                    mAppContext.getLoginInfo().getUserFaceUrl());
        } else {
            mTvUserAccount.setText("请登录");
            mRivUserFace.setImageResource(R.drawable.default_user_face);
        }
    }


    @Override
    public void widgetClick(View v) {
        if (!mAppContext.isLogin()) {
            LoginActivity.startFrom(this);
            return;
        }

        switch (v.getId()) {
            case R.id.item_manage_address:
                ManageAddressActivity.startFrom(this, ManageAddressActivity.LOOK_ADDRESS_MODE);
                break;
            case R.id.tv_user_account:
                break;
            case R.id.riv_user_face:
                ViewInject.toast("目前不支持更换头像，攻城师正在加班制造");
                break;
            case R.id.item_edit_password:
                startActivity(new Intent(this, EditPasswordActivity.class));
                break;
            case R.id.item_alter_phone_binding:
                startActivity(new Intent(this, AlterPhoneVerifyActivity.class));
                break;
        }
    }

    @Override
    public void onLoginSateChange() {
        initUserInfoByLoginState();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_center, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                SettingActivity.startFrom(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
