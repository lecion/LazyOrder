package com.cisoft.lazyorder.ui.goods;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Comment;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.core.goods.GoodsCommentService;
import com.cisoft.lazyorder.core.goods.GoodsService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.widget.MyListView;
import com.cisoft.lazyorder.widget.OrderNumView;

import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.bitmap.KJBitmapConfig;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class GoodsFragment extends BaseFragment implements AbsListView.OnItemClickListener {


    public static final String ARG_GOODS_ORDER = "goods_order";

    private String goodsOrder;

    /**
     * 商品数据
     */
    private List<Goods> goodsData;


    /**
     * 评论数据
     */
    private List<Comment> commentData;


    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    public static final String ORDER_POP = "pop";

    public static final String ORDER_PRICE = "price";

    private OnFragmentInteractionListener mListener;

    private int shopId;

    private int page;

    public static int size = 5;

    private KJBitmap kjb;

    private int mLastVisiblePosition = -1;

    private View mLastVisibleView;

    private boolean isExpand = false;

    @BindView(id = R.id.lv_goods)
    private MyListView lvGoods;

    private BaseAdapter mAdapter;
    private GoodsService goodsService;
    private GoodsCommentService commentService;



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
        commentData = new ArrayList<Comment>();
        initAdapter();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            goodsOrder = getArguments().getString(ARG_GOODS_ORDER);
        }
        goodsService = new GoodsService(getActivity(), ApiConstants.MODULE_COMMODITY);
        commentService = new GoodsCommentService(getActivity());

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
            Log.d("setGoodsData", goodsData + " this :" + this);
        } else {
            this.goodsData.addAll(goodsData);
        }

        mAdapter.notifyDataSetChanged();

    }

    public void setCommentData(List<Comment> comments) {
        if (page == 1) {
            this.commentData = comments;
        } else {

        }

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
//                Log.d("getView", "mLastVisiblePosition => " + mLastVisiblePosition + "  position=> " + position);

                final Goods item = (Goods) getItem(position);
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_list_cell, parent, false);
                    holder.tvGoodsTitle = (TextView) convertView.findViewById(R.id.tv_goods_title);
                    holder.tvGoodsAddress = (TextView) convertView.findViewById(R.id.tv_address);
                    holder.tvGoodsType = (TextView) convertView.findViewById(R.id.tv_goods_type);
                    holder.tvGoodsCount = (TextView) convertView.findViewById(R.id.tv_goods_count);
                    holder.btnGoodsPrice = (Button) convertView.findViewById(R.id.btn_goods_price);
                    holder.ivGoodsThumb = (ImageView) convertView.findViewById(R.id.iv_goods_thumb);
                    holder.llExpand = (LinearLayout) convertView.findViewById(R.id.ll_expand);
                    holder.orderNumView = (OrderNumView) convertView.findViewById(R.id.order_num_view);
                    holder.btnAddToCart = (Button) convertView.findViewById(R.id.btn_add_to_cart);
                    convertView.setTag(holder);
                }
                holder = (ViewHolder) convertView.getTag();
                holder.tvGoodsTitle.setText(item.getCmName());
                holder.btnGoodsPrice.setText(String.valueOf(item.getCmPrice()));
                holder.btnGoodsPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onAddToCart(item);
                        }
                    }
                });
                holder.tvGoodsCount.setText(String.valueOf(item.getSalesNum()));
                holder.tvGoodsType.setText(item.getCatName());
                kjb.display(holder.ivGoodsThumb, item.getCmPicture(), holder.ivGoodsThumb.getWidth(), holder.ivGoodsThumb.getHeight());

                /*以下是展开view*/
                holder.llExpand.setVisibility(View.GONE);
                holder.btnAddToCart.setOnClickListener(new AddToCartListener(holder.orderNumView, item));
                if (mLastVisiblePosition == position + 1 && isExpand) {
                    holder.llExpand.setVisibility(View.VISIBLE);
                }
                return convertView;
            }

            class AddToCartListener implements View.OnClickListener {
                OrderNumView orderNumView;
                Goods goods;
                public AddToCartListener(OrderNumView orderNumView, Goods item) {
                    this.orderNumView = orderNumView;
                    this.goods = item;
                }

                @Override
                public void onClick(View v) {
                    mListener.onAddToCart(goods, orderNumView.getNum());
                    //Did 展开view的复用问题：商品数量选择控件被复用=>暂时先这样解决
                    orderNumView.setNum(1);
                }
            }
        };
    }

    /**
     * 初始化Goods列表
     */
    private void initGoodsList() {

        lvGoods.setAdapter(mAdapter);
        lvGoods.setOnItemClickListener(this);
        lvGoods.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                goodsService.loadGoodsDataFromNet(shopId, page, size, goodsOrder);
            }

            @Override
            public void onLoadMore() {
                goodsService.loadGoodsDataFromNet(shopId, ++page, size, goodsOrder);
            }
        });
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
            if (mLastVisibleView == null) {
                mLastVisibleView = currentLL;
                currentLL.setVisibility(View.VISIBLE);
                isExpand = true;
            } else {
                //不是第一次点击
                //上次点击与本次不一样，则隐藏上次视图，显示本次视图
                if (mLastVisiblePosition != position) {
                    currentLL.setVisibility(View.VISIBLE);
                    mLastVisibleView.setVisibility(View.GONE);
                    mLastVisibleView = currentLL;
                    isExpand = true;
//                    Log.d("mLastVisiblePosition", mLastVisiblePosition+"");
                } else {
                    //展开则隐藏
                    if (isExpand) {
                        currentLL.setVisibility(View.GONE);
                    } else {
                        currentLL.setVisibility(View.VISIBLE);
                    }
                    isExpand = !isExpand;
                    //Log.d("mLastVisiblePosition", mLastVisiblePosition+"");
                }
            }
            mLastVisiblePosition = position;

            //TODO 解决嵌套Listview的滑动监听
            MyListView lvComment = (MyListView) view.findViewById(R.id.lv_comment);
            lvComment.getParent().requestDisallowInterceptTouchEvent(true);
            Log.d("onItemClick", lvComment +"");
            loadComment(lvComment, position);
        }
    }

    /**
     * 显示评论
     * @param lvComment
     * @param position
     */
    private void loadComment(final MyListView lvComment, int position) {

        int goodsId = goodsData.get(position - 1).getId();
        commentService.loadAllCommentByGoodsId(lvComment, goodsId, 1, 2, goodsOrder, new CommentHandler() {

            @Override
            public void handleComment(List<Comment> data) {
                List<Comment> comments = data;
                CommentAdapter adapter = new CommentAdapter(getActivity(), comments);
                lvComment.setAdapter(adapter);
                //Utility.setListViewHeightBasedOnChildren(lvComment);
            }
        });
    }

    public interface CommentHandler {
        void handleComment(List<Comment> data);
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
        lvGoods.stopRefreshData();
    }


    /**
     * 与Activity交互接口
     */
    public interface OnFragmentInteractionListener {

        public void onAddToCart(Goods goods);

        public void onAddToCart(Goods goods, int count);

    }

    public void setPullLoadEnable(boolean enable) {
        lvGoods.setPullLoadEnable(enable);
    }

    private class ViewHolder {
        TextView tvGoodsTitle;
        TextView tvGoodsAddress;
        TextView tvGoodsType;
        TextView tvGoodsCount;
        Button btnGoodsPrice;
        ImageView ivGoodsThumb;
        /*以下是展开的view的控件*/
        LinearLayout llExpand;
        ListView lvGoodsComment;
        OrderNumView orderNumView;
        Button btnAddToCart;

    }


    public void init(int page) {
        this.page = page;
        this.goodsOrder = ORDER_POP;
    }
}
