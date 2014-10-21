package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.cisoft.lazyorder.R;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.utils.DensityUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by comet on 2014/10/21.
 * 依赖KJFrame的广告轮播器
 */
public class AdRotator extends FrameLayout {

    private Context context;

    //用于定时器调度广告图切换
    private Handler mHandler = new Handler();

    //存放广告图url的数组
    private String[] imageUrlArr;


    private List<ImageView> adImageList = new ArrayList<ImageView>();

    //上个标签点的索引
    private int prevPointIndex = 0;

    private ViewPager vpAdImageShow;

    //存放标签点的容器
    private LinearLayout llDotsContainer;

    //广告图默认加载时的loading图
    private Bitmap loadingBitmap;

    //广告的宽度和高度
    private int adWidth;
    private int adHeight;

    //轮播广告数量（默认5个）
    private int adCount = 5;

    //自动轮播的时间间隔(单位：秒)（默认3秒钟）
    private int changeTimeInterval = 3;

    //自动轮播启用开关(默认开启)
    private boolean isAutoPlay = true;

    //是否能够设置url地址
    private boolean enableSetUrl = false;


    public AdRotator(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ad_rotator_layout, this);
    }


    /**
     * 构造函数，用于运行布局界面使用
     * @param context
     * @param attrs
     */
    public AdRotator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;


        //读取可选属性
        TypedArray typeArr = context.obtainStyledAttributes(attrs, R.styleable.adRotatorAttrs);
        isAutoPlay = typeArr.getBoolean(R.styleable.adRotatorAttrs_autoPlay, true);
        adCount = typeArr.getInt(R.styleable.adRotatorAttrs_count, 5);
        changeTimeInterval = typeArr.getInt(R.styleable.adRotatorAttrs_changeTimeInterval, 3);
        typeArr.recycle();
    }


    /**
     * 初始化组件及数据，放在这里是因为此回调方法之后可以获取实际宽度和高度
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            initAdImageList();
            initDots();
            initViewPagerAdapter();
            if (isAutoPlay) {
                startAutoPlay();
            }
        }
    }


    /**
     * 初始化广告图的集合
     */
    private void initAdImageList() {
        adWidth = getWidth();
        adHeight = getHeight();
        ImageView mImageView;
        loadingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        for(int i = 0; i < adCount; i++) {
            mImageView = new ImageView(context);
            mImageView.setImageBitmap(loadingBitmap);
            adImageList.add(mImageView);
        }
        enableSetUrl = true;
    }


    /**
     * 初始化标签点
     */
    private void initDots() {
        llDotsContainer = (LinearLayout) findViewById(R.id.llPointGroup);
        LayoutParams params;
        for(int i = 0; i < adCount; i++) {
            LinearLayout dot = new LinearLayout(context);
            dot.setBackgroundResource(R.drawable.ad_rotator_point_bg);
            params = new LayoutParams(DensityUtils.dip2px(context, 10), DensityUtils.dip2px(context, 5));
            dot.setLayoutParams(params);
            dot.setEnabled(false);
            llDotsContainer.addView(dot);
        }
    }

    /**
     * 初始化展现广告图的ViewPager
     */
    private void initViewPagerAdapter() {
        vpAdImageShow = (ViewPager) findViewById(R.id.vpAdImageShow);
        vpAdImageShow.setAdapter(new ViewPagerAdapter());
        vpAdImageShow.setOnPageChangeListener(new ViewPagerChangeListener());
    }


    /**
     *
     * 设置广告图片的url地址
     * @param imageUrlArr
     */
    public void setImageUrl(String[] imageUrlArr) {
        if (imageUrlArr.length == adCount) {
            this.imageUrlArr = imageUrlArr;
            new AsycLoadAdImage().execute();
        } else {
            KJLoger.debug("image array length should be" + adCount);
        }
    }


    /**
     * 开启自动播放功能
     */
    public void startAutoPlay() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        vpAdImageShow.setCurrentItem(vpAdImageShow.getCurrentItem() + 1);
                    }
                });
            }
        }, changeTimeInterval * 1000, changeTimeInterval * 1000);
    }


    /**
     * 用于异步设置广告图url（因为外界设置数据居然有时会在initAdImageList()之前调用，所以用了这个蛋疼的办法来解决）
     */
    private class AsycLoadAdImage extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            while(true) {
                if (enableSetUrl) {
                    break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            KJBitmap kjBitmap = KJBitmap.create();
            for (int i = 0; i< adCount; i++) {
                kjBitmap.display(adImageList.get(i), imageUrlArr[i], loadingBitmap, adWidth, adHeight);
            }
        }
    }


    /**
     * 监听器，主要用来设置标签页的位置
     */
    private class ViewPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i2) {}

        @Override
        public void onPageSelected(int position) {
            int newPosition = position % adCount;
            llDotsContainer.getChildAt(prevPointIndex).setEnabled(false);
            llDotsContainer.getChildAt(newPosition).setEnabled(true);
            prevPointIndex = newPosition;
        }

        @Override
        public void onPageScrollStateChanged(int i) {}

    }

    /**
     * 适配器
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //设置这么大是为了能够无限滚动（不出意外应该是无限。。。）
            return Integer.MAX_VALUE;
        }

        /**
         * 复用Object返回true，复用view返回false 复用的是Object
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 销毁对象
         *
         * @param position
         * 被销毁对象的索引位置
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) adImageList.get(position % adImageList.size()));
        }

        /**
         * 初始化一个对象
         *
         * @param position
         * 初始化对象的索引位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView((ImageView) adImageList.get(position % adImageList.size()));
            return adImageList.get(position % adImageList.size());
        }
    }
}
