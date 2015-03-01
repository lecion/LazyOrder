package com.cisoft.lazyorder.ui.main.menu;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuIcon;
import com.cisoft.lazyorder.AppContext;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.core.account.I_LoginStateObserver;
import com.cisoft.lazyorder.core.account.LoginStateObserver;
import com.cisoft.lazyorder.core.common.DrawerOpenStateObserver;
import com.cisoft.lazyorder.finals.SPConstants;
import com.cisoft.lazyorder.ui.account.UserCenterActivity;
import com.cisoft.lazyorder.util.Utility;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.FrameFragment;
import org.kymjs.kjframe.utils.PreferenceHelper;
import org.kymjs.kjframe.widget.RoundImageView;

/**
 * Created by comet on 2014/11/23.
 */
public class DrawerMenuFragment extends FrameFragment implements I_LoginStateObserver{

    private MenuFragmentManager mMenuFragmentManager;
    private NavigationDrawerCallbacks mCallbacks;
    private DrawerLayout mDrawerLayout;
    @BindView(id = R.id.ll_go_to_usercenter, click = true)
    private LinearLayout mLlGoToUserCenter;
    @BindView(id = R.id.lv_drawer_menu_list)
    private ListView mDrawerListView;
    @BindView(id = R.id.riv_user_face)
    private RoundImageView mIvUserFace;
    @BindView(id = R.id.tv_user_account)
    private TextView mTvUserAccount;
    private View mDrawerMenuView;
    private MaterialMenuIcon mMaterialMenuIcon;
    private AppContext mAppContext;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        if (bundle != null) {
            mCurrentSelectedPosition = bundle.getInt(SPConstants.KEY_DRAWER_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        View view = inflater.inflate(R.layout.fragment_drawer_menu, container, false);
        return view;
    }


    @Override
    protected void initData() {
        mMenuFragmentManager = MenuFragmentManager.getInstance(getActivity());
        mUserLearnedDrawer = PreferenceHelper.readBoolean(getActivity(), SPConstants.SP_FILE_NAME,
                SPConstants.KEY_DRAWER_USER_LEARNED, false);
        LoginStateObserver.getInstance().attach(this);
        mAppContext = (AppContext) getActivity().getApplication();
    }


    @Override
    protected void initWidget(View parentView) {
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mDrawerListView.setAdapter(new DrawerListAdapter(getActivity(),
                mMenuFragmentManager.mMenuItems));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        initUserInfoByLoginState();
        selectItem(mCurrentSelectedPosition);
    }


    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.ll_go_to_usercenter:
                UserCenterActivity.startFrom(getActivity());
                break;
        }
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mDrawerMenuView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mMaterialMenuIcon = new MaterialMenuIcon(getActivity(), Color.WHITE,
                MaterialMenuDrawable.Stroke.THIN);
        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mDrawerMenuView = drawerView;
                mMaterialMenuIcon.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        isDrawerOpen() ? 2 - slideOffset : slideOffset);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    PreferenceHelper.write(getActivity(), SPConstants.SP_FILE_NAME,
                            SPConstants.KEY_DRAWER_USER_LEARNED, true);
                }

                invalidateOptionsMenu();
            }
        });

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mDrawerMenuView);
        }
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mDrawerMenuView);
    }


    public void initUserInfoByLoginState() {
        if (mAppContext.isLogin()) {
            mTvUserAccount.setText(mAppContext.getLoginAccount());
            Utility.getKjBitmapInstance().display(mIvUserFace,
                    mAppContext.getLoginInfo().getUserFaceUrl());
        } else {
            mTvUserAccount.setText("请登录");
            mIvUserFace.setImageResource(R.drawable.default_user_face);
        }
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mDrawerMenuView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMaterialMenuIcon.onSaveInstanceState(outState);
        outState.putInt(SPConstants.KEY_DRAWER_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    /**
     * 重置actionbar上的菜单项
     */
    private void invalidateOptionsMenu() {
        DrawerOpenStateObserver.getInstance().notifyStateChanged(isDrawerOpen());
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.menu_drawer, menu);
            showGlobalContextActionBar();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!isDrawerOpen()) {
                    mDrawerLayout.openDrawer(mDrawerMenuView);
                } else {
                    mDrawerLayout.closeDrawer(mDrawerMenuView);
                }
                getActivity().invalidateOptionsMenu();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }


    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }

    /*
    * 登录状态改变后
    */
    @Override
    public void onLoginSateChange() {
        initUserInfoByLoginState();
    }

}
