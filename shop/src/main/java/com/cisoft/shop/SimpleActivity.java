package com.cisoft.shop;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cisoft.myapplication.R;
import com.cisoft.shop.widget.RefreshDeleteListVIew;
import com.cisoft.shop.widget.SwipeMenu;
import com.cisoft.shop.widget.SwipeMenuCreator;
import com.cisoft.shop.widget.SwipeMenuItem;

import java.util.ArrayList;
import java.util.List;

public class SimpleActivity extends Activity implements RefreshDeleteListVIew.OnRefreshListener {

    private List<String> mAppList = new ArrayList<String>();
    private AppAdapter mAdapter;
    private RefreshDeleteListVIew mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mAppList = getData();
        mListView = (RefreshDeleteListVIew) findViewById(R.id.listView);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
//				SwipeMenuItem openItem = new SwipeMenuItem(
//						getApplicationContext());
//				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//						0xCE)));
//				openItem.setWidth(dp2px(90));
//				openItem.setTitle("Open");
//				openItem.setTitleSize(18);
//				openItem.setTitleColor(Color.WHITE);
//				menu.addMenuItem(openItem);
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setPullLoadEnable(true);
        mListView.setMenuCreator(creator);
        mListView.setOnRefreshListener(this);
        mListView.setOnMenuItemClickListener(new RefreshDeleteListVIew.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        mAppList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        mAppList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        mListView.setOnSwipeListener(new RefreshDeleteListVIew.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });

        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(),
                        position - 1 + " long click", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Toast.makeText(getApplicationContext(),
                        position - 1 + "   click", Toast.LENGTH_LONG).show();
                if (position == mAdapter.getCount()) {

                }
            }
        });
    }

    public List<String> getData() {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            result.add("Item" + i);
        }
        return result;
    }

    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public String getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            String item = getItem(position);
            holder.tv_name.setText(item);
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onRefresh() {
        loadData(0);
    }

    @Override
    public void onLoadMore() {
        loadData(1);
    }

    private void loadData(final int what) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.obj = getData();
                handler.sendMessage(msg);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            List<String> result = (List<String>) msg.obj;
            switch (msg.what) {
                case 0:
                    mAppList.clear();
                    mAppList.addAll(result);
                    break;
                case 1:
                    mAppList.addAll(result);
                    break;
            }
            mListView.stopRefresh();
            mListView.stopLoadMore();
            mAdapter.notifyDataSetChanged();
        }

        ;
    };

}
