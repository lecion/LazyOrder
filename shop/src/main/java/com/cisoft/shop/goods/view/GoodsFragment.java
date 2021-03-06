package com.cisoft.shop.goods.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cisoft.shop.R;
import com.cisoft.shop.bean.Goods;
import com.cisoft.shop.bean.GoodsCategory;
import com.cisoft.shop.bean.Shop;
import com.cisoft.shop.goods.presenter.GoodsPresenter;
import com.cisoft.shop.util.DeviceUtil;
import com.cisoft.shop.util.L;
import com.cisoft.shop.widget.DialogFactory;
import com.cisoft.shop.widget.MyListView;

import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class GoodsFragment extends BaseFragment implements IGoodsView{

    @BindView(id = R.id.iv_shop_logo)
    private ImageView ivShopLogo;

    @BindView(id = R.id.tv_shop_name)
    private TextView tvShopName;

    @BindView(id = R.id.tv_shop_time_show)
    private TextView tvShopTime;

    @BindView(id = R.id.tv_shop_privilege_show)
    private TextView tvShopPrivilege;

    @BindView(id = R.id.sp_shop_state)
    private Spinner spShopState;

    @BindView(id = R.id.sp_goods_category)
    private Spinner spGoodsCategory;

    @BindView(id = R.id.lv_goods)
    private MyListView lvGoods;

    @BindView(id = R.id.rb_pop)
    private RadioButton rbPop;

    @BindView(id = R.id.rb_price)
    private RadioButton rbPrice;

    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    @BindView(id = R.id.btn_reload)
    private Button btnReload;

    private String sortType = SORT_SALES;

    private GoodsCategoryAdapter goodsCategoryAdapter;

    private GoodsListAdapter goodsListAdapter;

    private GoodsPresenter presenter;

    private String[] shopOperatingStates = {"正在营业", "停止营业"};

    private String[] goodsStates = {"已经售罄", "正在销售"};

    private int[] goodsStateColors = {Color.rgb(27, 137, 226), Color.rgb(178, 18, 29)};

    private List<GoodsCategory> goodsCategoryList;

    private List<Goods> goodsList = null;

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

    private int page = 1;
    private int size = 5;

    public static GoodsFragment newInstance(String param1) {
        GoodsFragment fragment = new GoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public GoodsFragment() {
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
        return layoutInflater.inflate(R.layout.fragment_goods, viewGroup, false);
    }

    @Override
    protected void initData() {
        presenter = new GoodsPresenter(getActivity(), this);
        goodsCategoryList = new ArrayList<GoodsCategory>();
        goodsCategoryList.add(new GoodsCategory(0, "全部"));
        goodsCategoryAdapter = new GoodsCategoryAdapter();
        goodsList = new ArrayList<Goods>();
        goodsListAdapter = new GoodsListAdapter();
        presenter.onLoad(type, sortType);
        shopOldState = 0;
    }

    @Override
    protected void initWidget(View parentView) {
        initShopStatus();

        initSortButton();

        initCategory();

        initGoodsList();

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLoad(type, sortType);
            }
        });
    }

    private void initSortButton() {
        rbPop.setOnClickListener(this);
        rbPrice.setOnClickListener(this);
    }

    /**
     * 初始化商品列表
     */
    private void initGoodsList() {
        lvGoods.setPullLoadEnable(false);
        //lvGoods.setEmptyView(llShowNoValueTip);
        lvGoods.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lvGoods.stopRefreshData();
                presenter.onLoad(type, sortType);
            }

            @Override
            public void onLoadMore() {
                lvGoods.stopRefreshData();
                if (isLoadMore) {
                    return;
                }
                isLoadMore = true;
                showMoreProgress();
                presenter.loadMore(++page, size, type, sortType);
            }
        });
        lvGoods.setAdapter(goodsListAdapter);
    }

    /**
     * 初始化商店状态
     */
    private void initShopStatus() {
        Shop shop = L.app(this).getShop();
        tvShopName.setText(shop.getName());
        tvShopTime.setText(shop.getOpenTime() + "-" + shop.getCloseTime());
        tvShopPrivilege.setText(shop.getPromotionInfo());
        KJBitmap.create().display(ivShopLogo, shop.getFaceImgUrl());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_goods_shop_state_cell, shopOperatingStates);
        spShopState.setAdapter(adapter);
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
        spShopState.setSelection(shop.getOperatingState());
    }

    /**
     * 初始化商品类别
     */
    private void initCategory() {
        spGoodsCategory.setAdapter(goodsCategoryAdapter);
        spGoodsCategory.setSelection(0);
        spGoodsCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.switchGoodsType(goodsCategoryList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void setGoodsList(List<Goods> goodsList) {
//        Log.d("setGoodsList", goodsList.toString());
        if (page == 1) {
            this.goodsList.clear();
            this.goodsList.addAll(goodsList);
            //滚动到顶部
            lvGoods.setSelection(0);
            Log.d("setGoodsData", goodsList + " this :" + this);
        } else {
            this.goodsList.addAll(goodsList);
        }
        goodsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setCategoryList(List<GoodsCategory> l) {
        this.goodsCategoryList = l;
        //goodsCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void setGoodsCategory(int type) {
        this.type = type;
    }

    @Override
    public void setGoodsStatus(int position, int state) {
        goodsList.get(position).setState(state);
        goodsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public void setDefaultSort() {
        rbPop.toggle();
        setSortType(SORT_SALES);
    }

    @Override
    public void setPullLoadEnable(boolean flag) {
        lvGoods.setPullLoadEnable(flag);
    }

    @Override
    public void showMoreProgress() {
        lvGoods.showFooterLoading();
    }

    @Override
    public void hideMoreProgress() {
        lvGoods.showFooterNormal();
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
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.rb_pop:
                presenter.switchSortType(type, SORT_SALES);
                break;
            case R.id.rb_price:
                presenter.switchSortType(type, SORT_PRICE);
                break;
        }
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
        public void onFragmentInteraction(Uri uri);
    }

    private class GoodsListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return goodsList.size();
        }

        @Override
        public Object getItem(int position) {
            return goodsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ((Goods)getItem(position)).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Goods goods = (Goods) getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_list_cell, parent, false);

                holder.ivGoodsImg = (ImageView) convertView.findViewById(R.id.iv_goods_img);
                holder.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
                holder.tvGoodsSales = (TextView) convertView.findViewById(R.id.tv_goods_sales_show);
                holder.tvGoodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);
                holder.btnGoodsStatus = (Button) convertView.findViewById(R.id.btn_goods_status);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            if (!TextUtils.isEmpty(goods.getCmPicture()) && goods.getCmPicture().startsWith("http")) {
                KJBitmap.create().display(holder.ivGoodsImg, goods.getCmPicture(), DeviceUtil.dp2px(getActivity(), 72), DeviceUtil.dp2px(getActivity(), 56));
            }
            holder.tvGoodsName.setText(goods.getCmName());
            holder.tvGoodsSales.setText(String.valueOf(goods.getSalesNum()));
            holder.tvGoodsPrice.setText("￥" + String.valueOf(goods.getCmPrice()));
            holder.btnGoodsStatus.setText(goodsStates[goods.getState()]);
            holder.btnGoodsStatus.setBackgroundResource(getGoodsStateBG(goods.getState()));
            holder.btnGoodsStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.switchGoodsStatus(position, goods.getId(), goods.getState());
                }
            });
            return convertView;
        }

        private int getGoodsStateBG(int state) {
            return state == 0 ? R.drawable.selector_red_corners_button : R.drawable.selector_blue_corners_button;
        }
    }


    private static class ViewHolder {
        ImageView ivGoodsImg;
        TextView tvGoodsName;
        TextView tvGoodsSales;
        TextView tvGoodsPrice;
        Button btnGoodsStatus;
    }

    private class GoodsCategoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return goodsCategoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return goodsCategoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return goodsCategoryList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_shop_state_cell, parent, false);
            TextView tv = (TextView) v.findViewById(android.R.id.text1);
            tv.setText(((GoodsCategory)getItem(position)).getCateName());
            return v;
        }
    }

}
