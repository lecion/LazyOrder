package com.cisoft.shop.goods.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cisoft.myapplication.R;
import com.cisoft.shop.bean.Goods;
import com.cisoft.shop.bean.GoodsCategory;
import com.cisoft.shop.goods.presenter.GoodsPresenter;
import com.cisoft.shop.widget.DialogFactory;
import com.cisoft.shop.widget.MyListView;

import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoodsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoodsFragment extends BaseFragment implements IGoodsView{

    @BindView(id = R.id.sp_shop_state)
    private Spinner spShopState;

    @BindView(id = R.id.sp_goods_category)
    private Spinner spGoodsCategory;

    @BindView(id = R.id.lv_goods)
    private MyListView lvGoods;

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
        presenter.onLoad(type);
        shopOldState = 0;
    }

    @Override
    protected void initWidget(View parentView) {
        initShopStatus();

        initCategory();

        initGoodsList();
    }

    /**
     * 初始化商品列表
     */
    private void initGoodsList() {
        lvGoods.setPullRefreshEnable(true);
        lvGoods.setPullLoadEnable(true);
        //lvGoods.setEmptyView(llShowNoValueTip);
        lvGoods.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lvGoods.stopRefreshData();
                presenter.onLoad(type);
            }

            @Override
            public void onLoadMore() {
                lvGoods.stopRefreshData();
                if (isLoadMore) {
                    return;
                }
                isLoadMore = true;
                presenter.loadMore(++page, size, type);
            }
        });
        lvGoods.setAdapter(goodsListAdapter);
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

    // TODO: Rename method, update argument and hook method into UI event
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
        Log.d("setGoodsList", goodsList.toString());
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
            KJBitmap.create().display(holder.ivGoodsImg, goods.getCmPicture(), holder.ivGoodsImg.getWidth(), holder.ivGoodsImg.getHeight());
            holder.tvGoodsName.setText(goods.getCmName());
            holder.tvGoodsSales.setText(String.valueOf(goods.getSalesNum()));
            holder.tvGoodsPrice.setText("￥" + String.valueOf(goods.getCmPrice()));
            holder.btnGoodsStatus.setText(goodsStates[goods.getState()]);
            holder.btnGoodsStatus.setBackgroundColor(goodsStateColors[goods.getState()]);
            holder.btnGoodsStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.switchGoodsStatus(position, goods.getState());
                }
            });
            return convertView;
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
            TextView tv = (TextView) v.findViewById(R.id.tv_shop_state_title);
            tv.setText(((GoodsCategory)getItem(position)).getCateName());
            return v;
        }
    }

}
