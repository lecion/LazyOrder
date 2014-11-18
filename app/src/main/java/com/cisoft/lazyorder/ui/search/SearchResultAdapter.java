package com.cisoft.lazyorder.ui.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.widget.OrderNumView;

import org.kymjs.aframe.bitmap.KJBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 14/11/19.
 */
public class SearchResultAdapter extends BaseAdapter{
    //TODO 完成搜索页面，继续完成适配器，解决商品添加bug，解决商品展开bug
    private SearchActivity context;

    private int shopId;

    private String shopName;

    private String shopAddress;

    private int mLastVisiblePosition = -1;

    private boolean isExpand = false;

    private List<Goods> goodsList;

    private KJBitmap kjb;


    public SearchResultAdapter(Context context, int shopId, String shopName, String shopAddress) {
        this.shopAddress = shopAddress;
        this.goodsList = new ArrayList<Goods>();
        this.context = (SearchActivity) context;
        this.shopId = shopId;
        this.shopName = shopName;
        kjb = KJBitmap.create();
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        Goods g = goodsList.get(position);
        g.setShopId(shopId);
        g.setShopName(shopName);
        return g;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_goods_list_cell, parent, false);
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
        holder.tvGoodsAddress.setText(shopAddress);
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
                if (context != null) {
                    context.onAddToCart(g);
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
                if (context != null) {
                    context.onAddToCart(g, orderNumView.getNum());
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
        final ViewGroup decorView = (ViewGroup)context.getWindow().getDecorView();
        LinearLayout ll = new LinearLayout(context);
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
        ImageView ivAnim = new ImageView(context);
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
        return context.getCartLocation();
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
        as.setDuration(300);
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

    /**
     * 设置data
     * @param l
     */
    public void setData(List<Goods> l) {
        goodsList.clear();
        goodsList.addAll(l);
        notifyDataSetChanged();
    }

    public void clearData() {
        goodsList.clear();
        notifyDataSetChanged();
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
}
