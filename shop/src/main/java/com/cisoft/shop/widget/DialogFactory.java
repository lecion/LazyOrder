package com.cisoft.shop.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cisoft.shop.R;
import com.cisoft.shop.util.DeviceUtil;

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

    public static DialogFragment createMaterialDialog(Context ctx, final String title, final String content, final String yes, final String no, final IConfirm onClick) {
        DialogFragment dialog = new DialogFragment() {
            TextView tvTitle;
            TextView tvContent;
            Button btnOk;
            Button btnNo;

            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
                View v = inflater.inflate(R.layout.layout_material_dialog, container, false);
                initWidget(v);
                if (title.isEmpty()) {
                    tvTitle.setVisibility(View.GONE);
                } else {
                    tvTitle.setText(title);
                }
                tvContent.setText(content);
                btnOk.setText(yes);
                btnNo.setText(no);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClick.onYes();
                        dismiss();
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                return v;
            }

            private void initWidget(View v) {
                tvTitle = (TextView) v.findViewById(R.id.tv_title);
                tvContent = (TextView) v.findViewById(R.id.tv_content);
                btnOk = (Button) v.findViewById(R.id.btn_ok);
                btnNo = (Button) v.findViewById(R.id.btn_no);
                if (DeviceUtil.isLollipop()) {
                    btnOk.setBackgroundResource(android.R.color.transparent);
                    btnNo.setBackgroundResource(android.R.color.transparent);
                }

            }

            @Override
            public void onResume() {
                super.onResume();
                Window window = getDialog().getWindow();
                window.setBackgroundDrawableResource(R.drawable.material_dialog_window);
            }

        };
        return dialog;
    }

}
