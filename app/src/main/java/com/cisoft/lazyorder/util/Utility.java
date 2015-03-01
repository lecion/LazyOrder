package com.cisoft.lazyorder.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.cisoft.lazyorder.AppConfig;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapConfig;

/**
 * Created by Lecion on 11/4/14.
 */
public class Utility {
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static KJBitmap getKjBitmapInstance() {
        BitmapConfig bitmapConfig = new BitmapConfig();
        bitmapConfig.CACHEPATH = AppConfig.IMAGE_CACHE_PATH;
        KJBitmap KjBitmap = KJBitmap.create(bitmapConfig);

        return KjBitmap;
    }
}
