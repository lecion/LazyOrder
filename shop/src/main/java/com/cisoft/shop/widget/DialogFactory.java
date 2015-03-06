package com.cisoft.shop.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cisoft.shop.R;

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

    public static Dialog createConfirmDialog(Context context, String title, String msg, String yes, String no, final IConfirm onClick) {
        if (title == null || title.equals("")) {
            title = "友情提醒";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(msg).setPositiveButton(yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onClick != null) {
                    onClick.onYes();
                }
            }
        }).setNegativeButton(no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public interface IConfirm{
        public void onYes();
    }

}
