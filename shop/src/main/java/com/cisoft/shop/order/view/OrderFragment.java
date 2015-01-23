package com.cisoft.shop.order.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cisoft.myapplication.R;
import com.cisoft.shop.bean.Order;
import com.cisoft.shop.order.presenter.OrderPresenter;
import com.cisoft.shop.widget.DialogFactory;
import com.cisoft.shop.widget.MyListView;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.cisoft.shop.order.view.OrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.cisoft.shop.order.view.OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends BaseFragment implements IOrderView{

    @BindView(id = R.id.sp_shop_state)
    private Spinner spShopState;

    @BindView(id = R.id.lv_order)
    private MyListView lvOrder;

    private OrderListAdapter orderListAdapter;

    private OrderPresenter presenter;

    private String[] shopOperatingStates = {"正在营业", "停止营业"};

    private String[] orderState = {"未准备", "已准备"};

    private int[] goodsStateColors = {Color.rgb(27, 137, 226), Color.rgb(178, 18, 29)};

    private List<Order> orderList = null;

    private boolean isLoadMore = false;

    private int type = 1;

    private int shopOldState;

    private enum GoodsState {
        SOLDOUT, SALES;
    }

    private static final String ARG_PARAM1 = "tag";

    private String FRAGMENT_TAG;

    private OnFragmentInteractionListener mListener;
    private Dialog loadingTipDialog;

    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    private int page = 1;
    private int size = 5;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment GoodsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            FRAGMENT_TAG = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    protected View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_order, viewGroup, false);
    }

    @Override
    protected void initData() {
        presenter = new OrderPresenter(getActivity(), this);
        orderList = new ArrayList<Order>();
        orderListAdapter = new OrderListAdapter();
        presenter.onLoad(type);
        shopOldState = 0;
    }

    @Override
    protected void initWidget(View parentView) {
        initShopStatus();

        initGoodsList();
    }

    /**
     * 初始化商品列表
     */
    private void initGoodsList() {
        lvOrder.setPullRefreshEnable(true);
        lvOrder.setPullLoadEnable(true);
        lvOrder.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lvOrder.stopRefreshData();
                presenter.onLoad(type);
            }

            @Override
            public void onLoadMore() {
                lvOrder.stopRefreshData();
                if (isLoadMore) {
                    return;
                }
                isLoadMore = true;
                presenter.loadMore(++page, size);
            }
        });
        lvOrder.setAdapter(orderListAdapter);
    }

    /**
     * 初始化商店状态
     */
    private void initShopStatus() {
        spShopState.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.fragment_goods_shop_state_cell, shopOperatingStates));
        spShopState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int newState = position;
                presenter.switchShopState(shopOldState, newState);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setOperatingState(int state) {
        this.shopOldState = state;
        spShopState.setSelection(state);
    }

    @Override
    public void setOrderList(List<Order> orderList) {
        Log.d("setGoodsList", orderList.toString());
        if (page == 1) {
            this.orderList.clear();
            this.orderList.addAll(orderList);
            //滚动到顶部
            lvOrder.setSelection(0);
            Log.d("setGoodsData", orderList + " this :" + this);
        } else {
            this.orderList.addAll(orderList);
        }
        orderListAdapter.notifyDataSetChanged();
    }
    @Override
    public void showProgress() {
        if (loadingTipDialog == null) {
            loadingTipDialog = DialogFactory.createToastDialog(getActivity(), "正在加载，请稍等");
        }
        if (!loadingTipDialog.isShowing()) {
            loadingTipDialog.show();
        }
        llShowNoValueTip.setVisibility(View.GONE);
    }

    @Override
    public void showNoData() {
        llShowNoValueTip.setVisibility(View.VISIBLE);
        if(loadingTipDialog !=null && loadingTipDialog.isShowing())
            loadingTipDialog.dismiss();
    }

    @Override
    public void hideProgress() {
        llShowNoValueTip.setVisibility(View.GONE);
        if (loadingTipDialog != null && loadingTipDialog.isShowing()) {
            loadingTipDialog.dismiss();
        }
    }

    @Override
    public void setOnLoadMore(boolean flag) {
        this.isLoadMore = flag;
    }

    @Override
    public void setPage(int i) {
        this.page = i;
    }

    @Override
    public void setOrderStatus(int position, int state) {
        //TODO 处理state
        orderList.get(position).setOrderState("CREATE");
        orderListAdapter.notifyDataSetChanged();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class OrderListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return orderList.size();
        }

        @Override
        public Object getItem(int position) {
            return orderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ((Order)getItem(position)).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //TODO 状态改变， 订单中的商品显示
            final Order order = (Order) getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_list_cell, parent, false);

                holder.tvOrderNumber = (TextView) convertView.findViewById(R.id.tv_order_number);
                holder.tvOrderMoneyAll = (TextView) convertView.findViewById(R.id.tv_order_money_all);
                holder.tvOrderTimeGo = (TextView) convertView.findViewById(R.id.tv_order_time_go);
                holder.btnOrderStatus = (Button) convertView.findViewById(R.id.btn_order_status);
                holder.spOrderGoodsList = (Spinner) convertView.findViewById(R.id.sp_order_goods_list);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.tvOrderNumber.setText(order.getOrderNumber());
            holder.tvOrderMoneyAll.setText("￥" + String.valueOf(order.getMoneyAll()));
            holder.tvOrderTimeGo.setText(String.valueOf(order.getTimeGo()));
            holder.btnOrderStatus.setText(order.getOrderState());
//            holder.btnOrderStatus.setBackgroundColor(goodsStateColors[order.getState()]);
//            holder.btnOrderStatus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    presenter.switchGoodsStatus(position, order.getState());
//                }
//            });
            return convertView;
        }
    }


    private static class ViewHolder {
        TextView tvOrderNumber;
        TextView tvOrderMoneyAll;
        TextView tvOrderTimeGo;
        Button btnOrderStatus;
        Spinner spOrderGoodsList;
    }

}
