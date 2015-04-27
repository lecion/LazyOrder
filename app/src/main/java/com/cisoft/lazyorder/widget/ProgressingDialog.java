package com.cisoft.lazyorder.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.cisoft.lazyorder.R;

public class ProgressingDialog extends Dialog {

    private int progressingTextRes;

    public ProgressingDialog(Context context) {
        super(context, R.style.ProgressingDialog);
    }

    public ProgressingDialog(Context context, int progressingTextRes) {
        this(context);
        this.progressingTextRes = progressingTextRes;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressing_dialog);

        if (progressingTextRes != 0) {
            TextView tvTitle = (TextView)findViewById(R.id.progressing_text);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(progressingTextRes);
        }
    }
}