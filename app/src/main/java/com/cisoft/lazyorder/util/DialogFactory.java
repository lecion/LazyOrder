package com.cisoft.lazyorder.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.cisoft.lazyorder.R;
import org.kymjs.aframe.ui.KJActivityManager;
import org.kymjs.aframe.utils.DensityUtils;
import org.kymjs.kjframe.ui.KJActivityStack;

/**
 * Created by comet on 2014/10/23.
 */
public class DialogFactory {

    public static Dialog createToastDialog(Context context, String tip) {
        final Dialog dialog = new Dialog(context, R.style.toastDialog);
        dialog.setContentView(R.layout.layout_wait_toast_dialog);
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

    public static Dialog createSuccessToastDialog(Context context, String tip) {
        final Dialog dialog = new Dialog(context, R.style.toastDialog);
        dialog.setContentView(R.layout.layout_success_toast_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = DensityUtils.getScreenW(KJActivityStack.create().topActivity());
        lp.width = (int) (0.4 * width);
        TextView tvLoadLabel = (TextView) dialog.findViewById(R.id.tvLoadLabel);
        if (tip == null || tip.length() == 0) {
            tvLoadLabel.setText("");
        } else {
            tvLoadLabel.setText(tip);
        }
        return dialog;
    }

    public static Dialog createConfirmDialog(Context context, String msg, final IConfirm onClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("友情提醒").setMessage(msg).setPositiveButton("残忍清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onClick != null) {
                    onClick.onYes();
                }
            }
        }).setNegativeButton("再看看", new DialogInterface.OnClickListener() {
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

    public static Dialog createWaitToastDialog(Context context, String tip) {
        final Dialog dialog = new Dialog(context, R.style.toastDialog);
        dialog.setContentView(R.layout.layout_wait_toast_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = DensityUtils.getScreenW(KJActivityStack.create().topActivity());
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
