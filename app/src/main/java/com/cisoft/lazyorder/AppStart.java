package com.cisoft.lazyorder;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import com.cisoft.lazyorder.ui.main.MainActivity;
import org.kymjs.kjframe.KJActivity;

/**
 * Created by comet on 2015/2/18.
 */
public class AppStart extends KJActivity {
    /**
     * 用于显示启动界面的背景图片
     */
    protected ImageView mImageView;


    @Override
    public void setRootView() {
        mImageView = new ImageView(this);
        mImageView.setBackgroundResource(R.drawable.welcome);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(mImageView);
    }

    @Override
    public void initWidget() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2F, 0.9F);
        alphaAnimation.setDuration(1000);
        animationSet.addAnimation(alphaAnimation);
        // 监听动画过程
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                checkVersion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                redirectTo();
            }
        });
        mImageView.startAnimation(animationSet);
    }

    /**
     * 跳转到...
     */
    public void redirectTo() {
        skipActivity(this, MainActivity.class);
    }

    /**
     * 判断首次使用
     */
    private boolean firstsInstall() {
        return true;
    }

    /**
     * 检查更新
     */
    private void checkVersion() {}
}
