package com.cisoft.lazyorder.ui.goods;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.core.goods.GoodsService;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.bitmap.KJBitmapConfig;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.fragment.BaseFragment;
import org.kymjs.aframe.ui.widget.KJListView;

import java.util.ArrayList;
import java.util.List;

public class GoodsFragment extends BaseFragment implements AbsListView.OnItemClickListener {


    public static final String ARG_GOODS_ORDER = "goods_order";

    private String goodsOrder;

    private List<Goods> goodsData;

//    @BindView(id = R.id.llLoadingGoodsListTip)
//    private LinearLayout llLoadingGoodsListTip;
    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    public static final String ORDER_POP = "pop";

    public static final String ORDER_PRICE = "price";

    private OnFragmentInteractionListener mListener;

    private int shopId;

    private int page;

    public static int size = 5;

    private KJBitmap kjb;


    /**
     * The fragment's ListView/GridView.
     */
    @BindView(id = R.id.lv_goods)
    private KJListView lvGoods;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private BaseAdapter mAdapter;
    private GoodsService goodsService;


    public static GoodsFragment newInstance(String goodsOrder) {
        GoodsFragment fragment = new GoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GOODS_ORDER, goodsOrder);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GoodsFragment() {
        page = 1;
        size = 5;
        goodsData = new ArrayList<Goods>();

        initAdapter();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            goodsOrder = getArguments().getString(ARG_GOODS_ORDER);
        }
        goodsService = new GoodsService(getActivity(), ApiConstants.MODULE_COMMODITY);

    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_goods_list, container, false);
        return view;
    }

    /**
     * 设置数据，如果page为1，则直接使用当前结果
     * @param goodsData
     */
    public void setGoodsData(List<Goods> goodsData) {
        if (page == 1) {
            this.goodsData = goodsData;
        }
        this.goodsData.addAll(goodsData);
        mAdapter.notifyDataSetChanged();
        //Log.d("setGoodsData", goodsOrder + " this :" + this);
    }

    @Override
    protected void initData() {
        goodsService.loadGoodsDataFromNet(shopId, 1, size, goodsOrder);
    }


    @Override
    protected void initWidget(View parentView) {
        initBitmap();
        initGoodsList();
    }

    private void initBitmap() {
        KJBitmapConfig kjBitmapConfig = new KJBitmapConfig();
        kjBitmapConfig.loadingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        kjb = KJBitmap.create(kjBitmapConfig);
    }


    private void initAdapter() {

        mAdapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return goodsData.size();
            }

            @Override
            public Object getItem(int position) {
                return goodsData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_list_cell, parent, false);
                    holder.tvGoodsTitle = (TextView) convertView.findViewById(R.id.tv_goods_title);
                    holder.tvGoodsAddress = (TextView) convertView.findViewById(R.id.tv_address);
                    holder.tvGoodsType = (TextView) convertView.findViewById(R.id.tv_goods_type);
                    holder.tvGoodsCount = (TextView) convertView.findViewById(R.id.tv_goods_count);
                    holder.tvGoodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);
                    holder.ivGoodsThumb = (ImageView) convertView.findViewById(R.id.iv_goods_thumb);
                    convertView.setTag(holder);
                }
                holder = (ViewHolder) convertView.getTag();
                Goods item = (Goods) getItem(position);
                holder.tvGoodsTitle.setText(item.getCmName());
                holder.tvGoodsPrice.setText(String.valueOf(item.getCmPrice()));
                holder.tvGoodsCount.setText(String.valueOf(item.getSalesNum()));
                holder.tvGoodsType.setText(item.getCatName());
                //kjb.display(holder.ivGoodsThumb, item.getCmPicture(), holder.ivGoodsThumb.getWidth(), holder.ivGoodsThumb.getHeight());
                return convertView;
            }
        };
    }

    /**
     * 初始化Goods列表
     */
    private void initGoodsList() {

        lvGoods.setAdapter(mAdapter);
        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewInject.toast("" + id);
                KJLoger.debug("aaaa");
            }
        });
        //TODO debug注释，记得取消
        /** /
        lvGoods.setOnRefreshListener(new KJRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                goodsService.loadGoodsDataFromNet(shopId, page, size, goodsOrder);
                //llLoadingGoodsListTip.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadMore() {
                goodsService.loadGoodsDataFromNet(shopId, ++page, size, goodsOrder);
            }
        });
        /**/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            shopId = ((GoodsActivity)activity).getShopId() == 0 ? 1: ((GoodsActivity)activity).getShopId();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ViewInject.toast("" + id);
        if (null != mListener) {
            if (position >= goodsData.size()) {
                return;
            }
            mListener.onGoodsItemClick(String.valueOf(goodsData.get(position).getId()));
        }
    }

    /**
     * 显示出没有数据的提示
     */
    public void showNoValueTip() {
        //llLoadingGoodsListTip.setVisibility(View.GONE);
        lvGoods.setVisibility(View.GONE);
        llShowNoValueTip.setVisibility(View.VISIBLE);
    }

    /**
     * 网络请求结束后回复状态
     */
    public void restoreState() {
        ///llLoadingGoodsListTip.setVisibility(View.GONE);
        //lvGoods.stopRefreshData();
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
        public void onGoodsItemClick(String id);

    }

    public void setPullLoadEnable(boolean enable) {
        //lvGoods.setPullLoadEnable(enable);
    }

    private class ViewHolder {
        TextView tvGoodsTitle;
        TextView tvGoodsAddress;
        TextView tvGoodsType;
        TextView tvGoodsCount;
        TextView tvGoodsPrice;
        ImageView ivGoodsThumb;
    }


    public void init(int page) {
        this.page = page;
        this.goodsOrder = ORDER_POP;
    }
}