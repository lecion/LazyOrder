package com.cisoft.lazyorder.ui.search;

import android.app.ActionBar;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cisoft.lazyorder.R;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

import java.lang.reflect.Field;

/**
 * Created by Lecion on 10/17/14.
 */
public class SearchActivity extends BaseActivity{
    @BindView(id = R.id.lv_search_results)
    private ListView lvSearchResult;

    @BindView(id = R.id.search_view)
    private SearchView searchView;

    public SearchActivity() {
        setHiddenActionBar(false);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_search);
        initActionBar();
    }

    private void initActionBar() {
        getActionBar().setDisplayShowHomeEnabled(true);
        //使应用程序能够在当前应用程序向上导航
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.nav_back_arrow);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setCustomView(LayoutInflater.from(this).inflate(R.layout.searchview_actionbar, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void initWidget() {
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(true);
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/submit_area", null, null);
        LinearLayout searchPlate = (LinearLayout) searchView.findViewById(searchPlateId);
        ((ImageView)searchPlate.getChildAt(0)).setImageResource(R.drawable.ic_search);

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);

        int closeBtnId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeBtn = (ImageView)searchView.findViewById(closeBtnId);
        closeBtn.setImageResource(R.drawable.ic_search);

        int searchButtonId = searchView.getContext().getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchButton = (ImageView) searchView.findViewById(searchButtonId);
        searchButton.setBackgroundResource(R.drawable.ic_launcher);
        searchButton.setImageResource(R.drawable.ic_search);

        setSearchViewBackground(searchView);

    }

    public void setSearchViewBackground(SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.parseColor("#e60012"));

            Field submitAredField = argClass.getDeclaredField("mSubmitArea");
            submitAredField.setAccessible(true);
            View submitView = (View)submitAredField.get(searchView);
            submitView.setBackgroundColor(Color.parseColor("#e60012"));


        } catch (Exception e) {
            e.printStackTrace();
        }
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
