package com.cisoft.lazyorder.ui.common;

import android.view.Menu;
import android.view.MenuItem;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.ui.BaseActivity;
import com.cisoft.lazyorder.widget.ProgressWebView;
import org.kymjs.kjframe.ui.BindView;

public class WebViewActivity extends BaseActivity {

    @BindView(id = R.id.wv_show_content)
    private ProgressWebView vbShowContent;

    public static final String CONENT_URL = "contentUrl";
    public static final String CONENT_TITLE = "contentTitle";

    private String contentUrl;
    private String contentTitle;


    @Override
    public void setRootView() {
        setContentView(R.layout.activity_web_view);
    }

    @Override
    public void initData() {
        contentUrl = getIntent().getStringExtra(CONENT_URL);
        contentTitle = getIntent().getStringExtra(CONENT_TITLE);
    }

    @Override
    public void initWidget() {
        setTitle(contentTitle);
        vbShowContent.loadUrl(contentUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
