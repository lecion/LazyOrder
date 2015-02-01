package com.cisoft.lazyorder.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 带进度条的WebView
 * Created by comet on 2014/12/8.
 */
public class ProgressWebView extends WebView {


    ProgressBar progressBar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //生成水平进度条
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
        addView(progressBar);
        getSettings().setAllowFileAccess(true);
        getSettings().setJavaScriptEnabled(true);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //自身加载新链接,不做外部跳转
            view.loadUrl(url);
            return true;
        }

    }


    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged (WebView view, int newProgress){
            if (newProgress == 100) {
                progressBar.setVisibility(GONE);
            } else {
                if (progressBar.getVisibility() == GONE)
                    progressBar.setVisibility(VISIBLE);
                progressBar.setProgress(newProgress);
            }

            super.onProgressChanged(view, newProgress);
        }
    }
}
