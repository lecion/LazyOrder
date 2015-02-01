package com.cisoft.lazyorder.ui.main.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.core.account.I_LoginStateObserver;
import com.cisoft.lazyorder.core.account.LoginStateObserver;

/**
 * Created by comet on 2014/11/22.
 */
public class DrawerMenuHeaderView extends MenuItemView implements I_LoginStateObserver {

    private ImageView userAvatar;
    private TextView balance;
    private TextView userName;
    private TextView userPoint;

    private Context context;

    public DrawerMenuHeaderView(Context context) {
        this(context, null, 0);
    }

    public DrawerMenuHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerMenuHeaderView(Context context, AttributeSet attrs, int paramInt) {
        super(context, attrs, paramInt);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.drawer_menu_header, this, true);

        userAvatar = (ImageView) findViewById(R.id.menu_header_user_avatar);
        userName = (TextView) findViewById(R.id.menu_header_user_name);
        balance = (TextView) findViewById(R.id.menu_header_balance);
        userPoint = (TextView) findViewById(R.id.menu_header_user_point);

        LoginStateObserver.getInstance().attach(this);

        control();
    }

    private void control() {
        AppContext app = (AppContext) context.getApplicationContext();
        boolean isLogin = app.isLogin();
        if (isLogin) {
            loginShow();
        } else {
            notLoginShow();
        }
    }




    private void notLoginShow() {
        userAvatar.setImageResource(R.drawable.drawe_menu_header_anonymous_avatar_bg);
        userName.setText("个人中心");
        balance.setVisibility(View.GONE);
//        balance.setText("登录可享更多特权");
        userPoint.setVisibility(View.GONE);
    }

    private void loginShow() {
        AppContext app = (AppContext) context.getApplicationContext();

        userAvatar.setImageResource(R.drawable.drawe_menu_header_anonymous_avatar_bg);
        userName.setText(app.getLoginPhoneNum());
        balance.setVisibility(View.GONE);
        userPoint.setVisibility(View.GONE);
    }


    @Override
    public void onLoginSateChange() {
        control();
    }
}
