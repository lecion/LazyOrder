package com.cisoft.lazyorder.ui.main;


import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.orderlist.HistoryOrderFragment;
import com.cisoft.lazyorder.ui.shop.ShopFragment;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.fragment.BaseFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class NavDrawerFragment extends BaseFragment {

    @BindView(id = R.id.lvMenuList)
    private ListView lvMenuList;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout navDrawLayout;
    private View fragmentContainerView;

    private SimpleAdapter menuListAdapter;
    private List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();

    private final String MENU_ITEM_FACE = "menuItemFace";
    private final String MENU_ITEM_NAME = "menuItemName";
    private final String MENU_ITEM_ID = "menuItemId";

    private final int MENU_WANT_ORDER = 1;
    private final int MENU_HISTORY_ORDER = 2;
    private final int MENU_USER_CENTER = 3;
    private final int MENU_ABOUT_WE = 4;

    private HashMap<Integer, Fragment> fragmentsMap = new HashMap<Integer, Fragment>();

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, null, true);

        return view;
    }

    @Override
    protected void initData() {
        initMenuListData();
    }

    @Override
    protected void initWidget(View v) {
        initMenuList();
        selectedMenuItem(MENU_WANT_ORDER);
    }

    /**
     * 初始化导航菜单的数据
     */
    private void initMenuListData(){
        Map<String, Object> map = null;

        map = new HashMap<String, Object>();
        map.put(MENU_ITEM_ID, MENU_WANT_ORDER);
        map.put(MENU_ITEM_FACE, R.drawable.ic_launcher);
        map.put(MENU_ITEM_NAME, "我要订餐");
        menuList.add(map);

        map = new HashMap<String, Object>();
        map.put(MENU_ITEM_ID, MENU_HISTORY_ORDER);
        map.put(MENU_ITEM_FACE, R.drawable.ic_launcher);
        map.put(MENU_ITEM_NAME, "历史订单");
        menuList.add(map);

        map = new HashMap<String, Object>();
        map.put(MENU_ITEM_ID, MENU_USER_CENTER);
        map.put(MENU_ITEM_FACE, R.drawable.ic_launcher);
        map.put(MENU_ITEM_NAME, "个人中心");
        menuList.add(map);

        map = new HashMap<String, Object>();
        map.put(MENU_ITEM_ID, MENU_ABOUT_WE);
        map.put(MENU_ITEM_FACE, R.drawable.ic_launcher);
        map.put(MENU_ITEM_NAME, "关于我们");
        menuList.add(map);
    }


    /**
     * 初始化导航菜单的相关组件信息
     */
    private void initMenuList(){
        menuListAdapter = new SimpleAdapter(getActivity(), menuList, R.layout.fragment_navigation_drawer_list_cell,
                new String[]{MENU_ITEM_FACE, MENU_ITEM_NAME}, new int[]{R.id.ivMenuItemFace, R.id.tvMenuItemName});
        lvMenuList.setAdapter(menuListAdapter);
        lvMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                lvMenuList.setItemChecked(position, true);
                Map<String, Object> map = menuList.get(position);
                selectedMenuItem((Integer) map.get(MENU_ITEM_ID));
            }
        });
    }

    /**
     * 设置上导航栏的参数
     * @param fragmentId
     * @param navDrawLayout
     */
    public void setUp(int fragmentId, DrawerLayout navDrawLayout){
        fragmentContainerView = getActivity().findViewById(fragmentId);
        this.navDrawLayout = navDrawLayout;

        navDrawLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                navDrawLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu();
            }
        };


        navDrawLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });

        navDrawLayout.setDrawerListener(drawerToggle);
    }

    /**
     * 导航抽屉是否打开
     * @return
     */
    public boolean isNavDrawerOpen() {
        return navDrawLayout != null && navDrawLayout.isDrawerOpen(fragmentContainerView);
    }


    /**
     * 选中的菜单项
     */
    private void selectedMenuItem(int menuItemId) {
        if (navDrawLayout != null) {
            navDrawLayout.closeDrawer(fragmentContainerView);
        }

        changeFragment(menuItemId);
    }

    /**
     * 切换fragment显示
     * @param menuItemId
     */
    private void changeFragment(int menuItemId){
        Fragment fragment = getFragmentByMenuItemId(menuItemId);
        /* 先隐藏已经创建了的fragment */
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Iterator iterator = fragmentsMap.entrySet().iterator();
        Fragment tempFragment = null;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            tempFragment = (Fragment) entry.getValue();
            fragmentTransaction.hide(tempFragment);
        }

        /* 显示指定的fragment */
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    /**
     * 根据菜单id得到对应的fragment实例
     * @param menuItemId
     * @return
     */
    private Fragment getFragmentByMenuItemId(int menuItemId){
        Fragment fragment = null;

        /* 若该fragment在集合中已存在,则直接取出 */
        if(fragmentsMap.containsKey(menuItemId)){
            fragment = fragmentsMap.get(menuItemId);

        /* 不存在则新建并添加进集合里 */
        } else {
            fragment = newInstance(menuItemId);
            fragmentsMap.put(menuItemId, fragment);
            addFragment(fragment);
        }

        return fragment;
    }

    /**
     * 创建新的fragment对象示例
     * @param menuItemId
     * @return
     */
    private Fragment newInstance(int menuItemId){
        Fragment fragment = null;

        switch (menuItemId) {
            case MENU_HISTORY_ORDER:
                fragment = new HistoryOrderFragment();
                break;
            case MENU_USER_CENTER:
                break;
            case MENU_ABOUT_WE:
                break;
            default:
                fragment = new ShopFragment();
                break;
        }

        return fragment;
    }

    /**
     * 添加新的fragment进FragmentManager里
     * @param fragment
     */
    private void addFragment(Fragment fragment){
        getFragmentManager().beginTransaction().add(R.id.flContainer, fragment).commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); /* 拦截home图标点击操作 */
    }

    /**
     * 使点击home图标出现导航抽屉的行为有效
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
