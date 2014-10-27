package com.cisoft.lazyorder.ui.sureorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.SureOrder.Order;
import com.cisoft.lazyorder.widget.OrderNumView;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;
import java.util.ArrayList;
import java.util.List;

public class SureOrderActivity extends BaseActivity {

    @BindView(id = R.id.tvShopName)
    private TextView tvShopName;

    @BindView(id = R.id.lvOrderList)
    private ListView lvOrderList;

    private List<Order> orderListData = new ArrayList<Order>();
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
        Order order = null;
        for (int i = 0; i < 10; i++) {
            order = new Order();
            order.setGoodName("食物" + i);
            order.setOrderTotal(2);
            order.setGoodPrice(9.5);
            orderListData.add(order);
        }
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

    private class OrderListAdapter extends BaseAdapter{

        private Context context;
        private List<Order> data;

        public OrderListAdapter(Context context, List<Order> data) {
            this.context = context;
            this.data = data;
        }


        public void addData(List<Order> addData) {
            data.addAll(addData);
        }

        public void clear() {
            data.clear();
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Order getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.sure_order_list_cell, null);
                viewHolder = new ViewHolder();
                viewHolder.tvGoodName = (TextView) convertView.findViewById(R.id.tvGoodName);
                viewHolder.onvGoodTotal = (OrderNumView) convertView.findViewById(R.id.onvGoodTotal);
                viewHolder.tvGoodPrice = (TextView) convertView.findViewById(R.id.tvGoodPrice);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Order order = data.get(position);
            viewHolder.tvGoodName.setText(order.getGoodName());
            viewHolder.tvGoodPrice.setText(String.valueOf(order.getGoodPrice()));
            viewHolder.onvGoodTotal.setNum(order.getOrderTotal());

            return convertView;
        }

        public class ViewHolder{
            public TextView tvGoodName;
            public OrderNumView onvGoodTotal;
            public TextView tvGoodPrice;
        }
    }
}
