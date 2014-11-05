package com.cisoft.lazyorder.ui.sureorder;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.bean.goods.GoodsCart;
import com.cisoft.lazyorder.ui.search.SearchActivity;
import com.cisoft.lazyorder.ui.shop.ShopActivity;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;
import java.util.List;

public class SureOrderActivity extends BaseActivity {

    @BindView(id = R.id.tvShopName)
    private TextView tvShopName;

    @BindView(id = R.id.lvOrderList)
    private ListView lvOrderList;

    private List<Goods> orderListData;
    private OrderListAdapter orderListAdapter;

    public SureOrderActivity() {
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_sure_order);
    }

    @Override
    protected void initData() {
        orderListData = GoodsCart.getInstance().getAllGoods();
    }

    @Override
    protected void initWidget() {
        initActionBar();
        initAdapter();
    }

    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setTitle("  确认订单");
    }

    private void initAdapter() {
        orderListAdapter = new OrderListAdapter(this, orderListData);
        lvOrderList.setAdapter(orderListAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sure_order, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
