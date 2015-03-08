package com.cisoft.lazyorder.ui.goods;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.core.goods.GoodsNetwork;
import com.cisoft.lazyorder.core.goods.ISwitchType;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.widget.EmptyView;
import com.cisoft.lazyorder.widget.RefreshListView;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.ui.FrameFragment;
import org.kymjs.kjframe.ui.ViewInject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2015/2/26.
 */
public class GoodsFragment extends FrameFragment implements ISwitchType, AdapterView.OnItemClickListener {

    public static final String ARG_GOODS_ORDER = "goods_order";
    public static final String ARG_GOODS_TYPE = "goods_type";
    public static final String ORDER_SALES_NUM = "salesNum";
    public static final String ORDER_PRICE = "cmPrice";

    @BindView(id = R.id.ll_root_view)
    private LinearLayout mRootView;
    @BindView(id = R.id.lv_goods)
    private RefreshListView lvGoods;
    private EmptyView mEmptyView;

    private List<Goods> goodsData;
    private OnFragmentInteractionListener mListener;
    private GoodsNetwork goodsNetwork;
    private GoodsListAdapter mAdapter;

    private int shopId;
    private int type;
    private int page = 1;
    public static int size = 5;
    private String goodsOrder;
    private HideListItemHolder hideListItemHolder;


    public static GoodsFragment newInstance(String goodsOrder, int goodsType) {
        GoodsFragment fragment = new GoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GOODS_ORDER, goodsOrder);
        args.putInt(ARG_GOODS_TYPE, goodsType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_goods_list, container, false);
        return view;
    }

    @Override
    protected void initData() {
        goodsData = new ArrayList<Goods>();
        if (getArguments() != null) {
            goodsOrder = getArguments().getString(ARG_GOODS_ORDER);
            type = getArguments().getInt(ARG_GOODS_TYPE);
        }
        goodsNetwork = new GoodsNetwork(getActivity());
        hideListItemHolder = new HideListItemHolder();
    }

    @Override
    protected void initWidget(View parentView) {
        initGoodsListView();
        loadGoodsListData(false);
    }


    /**
     * 初始化Goods列表
     */
    private void initGoodsListView() {
        lvGoods.setOnItemClickListener(this);
        lvGoods.setPullLoadEnable(false);   //刚开始时不显示"加载更多"的字样
        lvGoods.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1; //下拉刷新时,页码重置为1
                loadGoodsListData(true);
            }

            @Override
            public void onLoadMore() {
                ++page; //页码自增加1
                GoodsNetwork.OnGoodsLoadCallback onGoodsLoadCallback = new GoodsNetwork.OnGoodsLoadCallback() {
                    @Override
                    public void onPreStart() {}

                    @Override
                    public void onSuccess(List<Goods> goodses) {
                        lvGoods.stopRefreshData();

                        if (goodses.size() == 0) {
                            ViewInject.toast(getText(R.string.toast_no_more_data).toString());
                            lvGoods.setPullLoadEnable(false);
                        } else {
                            if (goodses.size() < size) {
                                lvGoods.setPullLoadEnable(false);
                            } else {
                                lvGoods.setPullLoadEnable(true);//有数据才使上拉加载更多可用
                            }
                            mAdapter.addData(goodses);
                            mAdapter.refresh();
                        }
                    }

                    @Override
                    public void onFailure(int stateCode, String errorMsg) {
                        lvGoods.stopRefreshData();

                        //之前有数据显示就不显示EmptyView了,只吐司提示
                        ViewInject.toast(errorMsg);
                    }
                };

                if (type == 0) {
                    goodsNetwork.loadGoodsList(shopId, page, size, goodsOrder, onGoodsLoadCallback);
                } else {
                    goodsNetwork.loadGoodsListByType(shopId, type, page, size, goodsOrder, onGoodsLoadCallback);
                }
            }
        });

        mAdapter = new GoodsListAdapter(getActivity(), goodsData, hideListItemHolder);
        lvGoods.setAdapter(mAdapter);
        mEmptyView = new EmptyView(getActivity(), mRootView);
        mEmptyView.setOnClickReloadListener(new EmptyView.OnClickReloadListener() {
            @Override
            public void onReload() {
                loadGoodsListData(false);
            }
        });
    }


    /**
     * 加载商品
     */
    public void loadGoodsListData(final boolean isRefresh) {
        page = 1;
        GoodsNetwork.OnGoodsLoadCallback onGoodsLoadCallback = new GoodsNetwork.OnGoodsLoadCallback() {
            @Override
            public void onPreStart() {
                if (!isRefresh) {
                    mListener.showWaitTip();
                }
            }

            @Override
            public void onSuccess(List<Goods> goods) {
                if (isRefresh)
                    lvGoods.stopRefreshData();
                else
                    mListener.closeWaitTip();

                if (goods.size() == 0) {
                    lvGoods.setPullLoadEnable(false);
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                } else {
                    if (goods.size() < size) {
                        lvGoods.setPullLoadEnable(false);
                    } else {
                        lvGoods.setPullLoadEnable(true);//有数据才使上拉加载更多可用
                    }

                    mAdapter.clearAll();
                    mAdapter.addData(goods);
                    mAdapter.refresh();
                }
            }

            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if (isRefresh)
                    lvGoods.stopRefreshData();
                else
                    mListener.closeWaitTip();

                lvGoods.setPullLoadEnable(false);
                ViewInject.toast(errorMsg);
                if(stateCode == ApiConstants.RES_STATE_NOT_NET || stateCode == ApiConstants.RES_STATE_NET_POOR) {
                    mEmptyView.showEmptyView(EmptyView.NO_NETWORK);
                } else {
                    mEmptyView.showEmptyView(EmptyView.NO_DATA);
                }
            }
        };

        if (type == 0) {
            goodsNetwork.loadGoodsList(shopId, page, size, goodsOrder, onGoodsLoadCallback);
        } else {
            goodsNetwork.loadGoodsListByType(shopId, type, page, size, goodsOrder, onGoodsLoadCallback);
        }
    }


    /**
     * 商品列表的Item被点击时回调
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            if (position > goodsData.size()) {
                return;
            }
            //mListener.onGoodsItemClick(goodsData.get(position));
            LinearLayout currentLL = (LinearLayout) view.findViewById(R.id.ll_expand);
            //第一次点击
            if (hideListItemHolder.lastVisibleView == null) {
                hideListItemHolder.lastVisibleView = currentLL;
                currentLL.setVisibility(View.VISIBLE);
                hideListItemHolder.isExpand = true;
            } else {
                //不是第一次点击
                //上次点击与本次不一样，则隐藏上次视图，显示本次视图
                if (hideListItemHolder.lastVisiblePosition != position) {
                    currentLL.setVisibility(View.VISIBLE);
                    hideListItemHolder.lastVisibleView.setVisibility(View.GONE);
                    hideListItemHolder.lastVisibleView = currentLL;
                    hideListItemHolder.isExpand = true;
//                    Log.d("mLastVisiblePosition", mLastVisiblePosition+"");
                } else {
                    //展开则隐藏
                    if (hideListItemHolder.isExpand) {
                        currentLL.setVisibility(View.GONE);
                    } else {
                        currentLL.setVisibility(View.VISIBLE);
                    }
                    hideListItemHolder.isExpand = !hideListItemHolder.isExpand;
                    //Log.d("mLastVisiblePosition", mLastVisiblePosition+"");
                }
            }
            hideListItemHolder.lastVisiblePosition = position;

            /** /
             MyListView lvComment = (MyListView) view.findViewById(R.id.lv_comment);
             lvComment.getParent().requestDisallowInterceptTouchEvent(true);
             Log.d("onItemClick", lvComment +"");
             loadComment(lvComment, position);
             /**/
        }
    }


    /**
     * 类型被选择时调用
     * @param type
     */
    @Override
    public void onSwitch(int type) {
        this.type = type;
        mEmptyView.hideEmptyView();// 如果有空视图提示则隐藏
        loadGoodsListData(false);
    }

    /**
     * 与Activity交互接口
     */
    public interface OnFragmentInteractionListener {
        public void onAddToCart(Goods goods);
        public void onAddToCart(Goods goods, int count);
        public int[] getCartLocation();
        public void addSwitchTypeObserver(ISwitchType observer);
        public void showWaitTip();
        public void closeWaitTip();
        public int getShopId();
        public String getShopName();
        public String getShopAddress();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            shopId = mListener.getShopId();
            mListener.addSwitchTypeObserver(this);
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


    public class HideListItemHolder {
        public int lastVisiblePosition = -1;
        public View lastVisibleView;
        public boolean isExpand = false;
    }
}
