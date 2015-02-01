package com.cisoft.lazyorder.ui.express;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.express.SmsInfo;
import com.cisoft.lazyorder.core.express.SmsContent;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

public class ChoiceSmsActivity extends KJActivity implements AdapterView.OnItemClickListener{

    @BindView(id = R.id.lv_sms_list)
    private ListView lvSmsList;
    private SmsContent smsContent;
    private List<SmsInfo> smsInfos = new ArrayList<SmsInfo>();

    public static final String IMPORT_SMS = "importSms";

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_choice_sms);
    }

    @Override
    public void initData() {
        smsContent = new SmsContent(this);
        smsInfos = smsContent.getAllInboxSms();
    }

    @Override
    public void initWidget() {
        ChoiceSmsListAdapter smsListAdapter = new ChoiceSmsListAdapter(this, smsInfos);
        lvSmsList.setAdapter(smsListAdapter);
        lvSmsList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long l) {
        SmsInfo smsInfo = (SmsInfo) adapterView.getItemAtPosition(position);

        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IMPORT_SMS, smsInfo);
        data.putExtras(bundle);
        setResult(1, data);
        finish();
    }
}
