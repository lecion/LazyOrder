package com.cisoft.lazyorder.ui.goods;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
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
import com.cisoft.lazyorder.core.goods.GoodsService;
import com.cisoft.lazyorder.core.goods.INetWorkFinished;
import com.cisoft.lazyorder.core.goods.ISwitchType;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.util.DialogFactory;
import com.cisoft.lazyorder.widget.MyListView;
import com.cisoft.lazyorder.widget.OrderNumView;

import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.bitmap.KJBitmapConfig;
import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class GoodsFragment extends BaseFragment implements AbsListView.OnItemClickListener, ISwitchType{

    public static final String ARG_GOODS_ORDER = "goods_order";

    public static final String ARG_GOODS_TYPE = "goods_type";

    /**
     * 商品排序
     */
    private String goodsOrder;

    /**
     * fragment所属的商品类别
     */
    private int type;

    /**
     * 商品数据
     */
    private List<Goods> goodsData;

    /**
     * 评论数据
     */
    private List<Comment> commentData;

    /**
     * 没有数据的提示
     */
    @BindView(id = R.id.llShowNoValueTip)
    private LinearLayout llShowNoValueTip;

    /**
     * 加载数据的提示
     */
    private Dialog loadingTipDialog;

    public static final String ORDER_SALES_NUM = "salesNum";

    public static final String ORDER_PRICE = "cmPrice";

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

    public GoodsFragment() {
        page = 1;
        size = 5;
        goodsData = new ArrayList<Goods>();
        commentData = new ArrayList<Comment>();
        initAdapter();
    }

    public static GoodsFragment newInstance(String goodsOrder, int goodsType) {
        GoodsFragment fragment = new GoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GOODS_ORDER, goodsOrder);
        args.putInt(ARG_GOODS_TYPE, goodsType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            goodsOrder = getArguments().getString(ARG_GOODS_ORDER);
            type = getArguments().getInt(ARG_GOODS_TYPE);
        }
        goodsService = new GoodsService(getActivity(), ApiConstants.MODULE_COMMODITY);
        //commentService = new GoodsCommentService(getActivity());
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_goods_list, container, false);
        return view;
    }

    @Override
    protected void initData() {
        loadGoodsDataAtFirstTime();
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

    /**
     * 程序第一次页面进入时，加载第一页商品
     */
    public void loadGoodsDataAtFirstTime() {
        showLoadingTip();
        INetWorkFinished<Goods> netWorkFinishedListener = new INetWorkFinished<Goods>() {
            @Override
            public void onSuccess(List<Goods> l) {
                hideLoadingTip();
                if (l.size() == 0) {
                    showNoValueTip();
                } else {
                    setGoodsData(l);
                }
            }
            @Override
            public void onFailure(String info) {
                //TODO 临时处理=>加载商品出错
                ViewInject.toast(info);
                showNoValueTip();
            }
        };
        if (type == 0) {
            //加载全部商品
            goodsService.loadGoodsList(shopId, 1, size, goodsOrder, netWorkFinishedListener);
        } else {
            //根据类别加载商品
            goodsService.loadGoodsListByType(shopId, type, 1, size, goodsOrder, netWorkFinishedListener);
        }
    }

    /**
     * 初始化Goods列表
     */
    private void initGoodsList() {
        lvGoods.setAdapter(mAdapter);
        lvGoods.setOnItemClickListener(this);
        lvGoods.setPullLoadEnable(true);
        lvGoods.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lvGoods.stopRefreshData();
                page = 1;
                loadGoodsDataAtFirstTime();
                //ViewInject.toast("刷新完成");
            }

            @Override
            public void onLoadMore() {
                lvGoods.stopRefreshData();
                INetWorkFinished netWorkFinishedListener = new INetWorkFinished<Goods>() {
                    @Override
                    public void onSuccess(List<Goods> l) {
                        if (l.size() == 0) {
                            ViewInject.toast("已经加载完了~");
                        } else {
                            setGoodsData(l);
                        }
                    }

                    @Override
                    public void onFailure(String info) {
                        //TODO 临时处理=>加载更多商品出错
                        ViewInject.toast(info);
                    }
                };

                if (type == 0) {
                    goodsService.loadGoodsList(shopId, ++page, size, goodsOrder, netWorkFinishedListener);
                } else {
                    goodsService.loadGoodsListByType(shopId, type, ++page, size, goodsOrder, netWorkFinishedListener);
                }
            }
        });
    }

    public void setPullLoadEnable(boolean enable) {
        lvGoods.setPullLoadEnable(enable);
    }

    /**
     * 设置数据，如果page为1，则直接使用当前结果
     * @param goodsData
     */
    public void setGoodsData(List<Goods> goodsData) {
        if (page == 1) {
            this.goodsData = goodsData;
            mAdapter.notifyDataSetChanged();
            //滚动到顶部
            lvGoods.setSelection(0);
            Log.d("setGoodsData", goodsData + " this :" + this);
        } else {
            this.goodsData.addAll(goodsData);
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 显示加载数据的提示
     */
    public void showLoadingTip() {
        if (loadingTipDialog == null) {
            loadingTipDialog = DialogFactory.createToastDialog(getActivity(), "正在加载，请稍等");
        }
        if (!loadingTipDialog.isShowing()) {
            loadingTipDialog.show();
        }
        llShowNoValueTip.setVisibility(View.GONE);
    }

    /**
     * 隐藏加载数据的提示
     */
    public void hideLoadingTip() {
        llShowNoValueTip.setVisibility(View.GONE);
        if (loadingTipDialog != null && loadingTipDialog.isShowing()) {
            loadingTipDialog.dismiss();
        }
    }

    /**
     * 显示出没有数据的提示
     */
    public void showNoValueTip() {
        llShowNoValueTip.setVisibility(View.VISIBLE);
        if(loadingTipDialog !=null && loadingTipDialog.isShowing())
            loadingTipDialog.dismiss();
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
        loadGoodsDataAtFirstTime();
    }

    public interface CommentHandler {
        void handleComment(List<Comment> data);
    }

    /**
     * 与Activity交互接口
     */
    public interface OnFragmentInteractionListener {
        public void onAddToCart(Goods goods);
        public void onAddToCart(Goods goods, int count);
        public int[] getCartLocation();
    }

    /**
     * 初始化商品Adapter
     */
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
            public View getView(final int position, View convertView, ViewGroup parent) {
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
                kjb.display(holder.ivGoodsThumb, item.getCmPicture(), holder.ivGoodsThumb.getWidth(), holder.ivGoodsThumb.getHeight());
                holder.tvGoodsTitle.setText(item.getCmName());

                holder.btnGoodsPrice.setText(String.valueOf(item.getCmPrice()));

                final View animView = holder.btnGoodsPrice;
                holder.btnGoodsPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        startAnimation(v, position);
                    }

                });
                holder.tvGoodsCount.setText(String.valueOf(item.getSalesNum()));
                holder.tvGoodsType.setText(item.getCatName());
                /*以下是展开view*/
                holder.llExpand.setVisibility(View.GONE);
                holder.btnAddToCart.setOnClickListener(new AddToCartListener(holder.orderNumView, item, animView));
                if (mLastVisiblePosition == position + 1 && isExpand) {
                    holder.llExpand.setVisibility(View.VISIBLE);
                }
                return convertView;
            }

            /**
             * 点击价格标签时的动画
             *
             * @param v
             * @param position
             * @return
             */
            private void startAnimation(final View v, int position) {
                final Goods g = (Goods) getItem(position);
                //重新设置价格，防止重用出现问题
                ((Button)v).setText(g.getCmPrice() + "");
                final View animView = createAnimView(v);
                final ViewGroup animLayout = createAnimLayer();
                animLayout.addView(animView);
                int[] startLoc = getAnimStartLocation(v);
                //Log.d("start ", startLoc[0] + " " +startLoc[1]);
                setAnimViewLoc(startLoc, animView);
                int[] endLoc = getAnimEndLocation();
                int[] offset = getAnimOffset(startLoc, endLoc);
                final Animation animation = buildAnimation(offset);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        v.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (mListener !=null) {
                            mListener.onAddToCart(g);
                        }
                        ((ViewGroup)animLayout.getParent()).removeView(animLayout);
                        v.setEnabled(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                //animView.setAnimation(animation);
                animView.startAnimation(animation);
            }

            /**
             * 点击加入购物车按钮时的动画
             *
             * @param v
             * @param g
             */
            private void startAnimation(final View v, final Goods g, final OrderNumView orderNumView, final View disableView) {
                //重新设置价格，防止重用出现问题
                ((Button)v).setText(g.getCmPrice() + "");
                final View animView = createAnimView(v);
                final ViewGroup animLayout = createAnimLayer();
                animLayout.addView(animView);
                int[] startLoc = getAnimStartLocation(v);
                setAnimViewLoc(startLoc, animView);
                int[] endLoc = getAnimEndLocation();
                int[] offset = getAnimOffset(startLoc, endLoc);
                Animation animation = buildAnimation(offset);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        disableView.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (mListener !=null) {
                            mListener.onAddToCart(g, orderNumView.getNum());
                        }
                        //Did 展开view的复用问题：商品数量选择控件被复用=>暂时先这样解决
                        orderNumView.setNum(1);
                        ((ViewGroup)animLayout.getParent()).removeView(animLayout);
                        disableView.setEnabled(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animView.setAnimation(animation);
            }

            /**
             * 创建动画层
             * @return
             */
            private ViewGroup createAnimLayer() {
                final ViewGroup decorView = (ViewGroup) getActivity().getWindow().getDecorView();
                LinearLayout ll = new LinearLayout(getActivity());
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                ll.setBackgroundResource(android.R.color.transparent);
                decorView.addView(ll);
                return ll;
            }

            /**
             * 创建动画对象
             * @param v
             * @return
             */
            private View createAnimView(View v) {
                v.buildDrawingCache();
                Bitmap animBitmap = v.getDrawingCache();
                ImageView ivAnim = new ImageView(getActivity());
                ivAnim.setImageBitmap(animBitmap);
                return ivAnim;
            }

            /**
             * 获得动画开始位置，即价格视图所在位置
             * @param v
             * @return
             */
            private int[] getAnimStartLocation(View v) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                return location;
            }

            /**
             * 获得动画结束位置，即购物车所在位置
             * @return
             */
            private int[] getAnimEndLocation() {
                return mListener.getCartLocation();
            }

            /**
             * 设置动画起始位置
             * @param startLoc
             * @param animView
             */
            private void setAnimViewLoc(int[] startLoc, View animView) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = startLoc[0];
                lp.topMargin = startLoc[1];
                animView.setLayoutParams(lp);
            }

            /**
             * 获得动画x，y坐标的偏移量
             * @param startLoc
             * @param endLoc
             * @return
             */
            private int[] getAnimOffset(int[] startLoc, int[] endLoc) {
                int[] offset = new int[2];
                offset[0] = endLoc[0] - startLoc[0];
                offset[1] = endLoc[1] - startLoc[1];
                return offset;
            }

            /**
             * 根据偏移量创建动画
             * @param offset
             * @return
             */
            private Animation buildAnimation(int[] offset) {
                AnimationSet as = new AnimationSet(false);
                TranslateAnimation translateX = new TranslateAnimation(0, offset[0], 0, 0);
                TranslateAnimation translateY = new TranslateAnimation(0, 0, 0, offset[1]);
                translateY.setInterpolator(new AccelerateInterpolator());
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0);
                scaleAnimation.setInterpolator(new AccelerateInterpolator());
                as.addAnimation(scaleAnimation);
                as.addAnimation(translateX);
                as.addAnimation(translateY);
                as.setDuration(500);
                return as;
            }

            /**
             * 添加到购物车按钮被点击时的监听器
             */
            class AddToCartListener implements View.OnClickListener {
                OrderNumView orderNumView;
                Goods goods;
                View animView;
                public AddToCartListener(OrderNumView orderNumView, Goods item, View animView) {
                    this.orderNumView = orderNumView;
                    this.goods = item;
                    this.animView = animView;
                }

                @Override
                public void onClick(View v) {
                    startAnimation(animView, goods, orderNumView, v);
                }


            }
        };
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            shopId = ((GoodsActivity)activity).getShopId();
            ((GoodsActivity) activity).addSwitchTypeObserver(this);
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
}
