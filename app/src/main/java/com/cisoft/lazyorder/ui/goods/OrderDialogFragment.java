package com.cisoft.lazyorder.ui.goods;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;

import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.bitmap.KJBitmap;
import org.kymjs.aframe.ui.AnnotateUtil;
import org.kymjs.aframe.ui.BindView;

/**
 * Created by Lecion on 10/27/14.
 */
public class OrderDialogFragment extends DialogFragment {
    @BindView(id = R.id.iv_goods_order)
    private ImageView ivGoodsOrder;


    @BindView(id = R.id.btn_dec)
    private Button btnDec;

    @BindView(id = R.id.btn_inc)
    private Button btnInc;

    @BindView(id = R.id.et_order_count)
    private EditText etOrderCount;

    private Goods goods;

    private KJBitmap kjb;

    public static final String KEY_GOODS = "goods";



    public static OrderDialogFragment newInstance(Goods goods) {
        OrderDialogFragment f = new OrderDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_GOODS, goods);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getDialog().requestWindowFeature(STYLE_NO_TITLE);
        View v = inflater.inflate(R.layout.fragment_goods_order_layout, container, false);
        AnnotateUtil.initBindView(this);
        initWidget();
        return v;
    }

    private void initData() {

    }
    private void initWidget() {
        kjb = KJBitmap.create();
        KJLoger.debug("goodspic " + goods.getCmPrice());
        //kjb.display(ivGoodsOrder, goods.getCmPicture(), true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.goods = (Goods) getArguments().getSerializable(KEY_GOODS);
        }

    }
}
