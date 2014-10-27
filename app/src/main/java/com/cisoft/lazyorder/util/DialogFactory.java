package com.cisoft.lazyorder.util;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cisoft.lazyorder.R;

import org.kymjs.aframe.ui.KJActivityManager;
import org.kymjs.aframe.utils.DensityUtils;

/**
 * Created by comet on 2014/10/23.
 */
public class DialogFactory {

    public static Dialog createToastDialog(Context context, String tip) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.toast_dialog_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = DensityUtils.getScreenW(KJActivityManager.create().topActivity());
        lp.width = (int) (0.6 * width);
        TextView tvLoadLabel = (TextView) dialog.findViewById(R.id.tvLoadLabel);
        if (tip == null || tip.length() == 0) {
            tvLoadLabel.setText("");
        } else {
            tvLoadLabel.setText(tip);
        }
        return dialog;
    }
}
