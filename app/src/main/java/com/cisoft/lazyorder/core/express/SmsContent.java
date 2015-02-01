package com.cisoft.lazyorder.core.express;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import com.cisoft.lazyorder.bean.express.SmsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by comet on 2014/12/3.
 */
public class SmsContent {

    private Activity activity;
    private Uri uri;
    private final String SMS_URI_INBOX = "content://sms/inbox";

    public SmsContent(Activity activity) {
        this.activity = activity;
        this.uri = Uri.parse(SMS_URI_INBOX);
    }

    /**
     * 获取所有的收件箱里的短信
     * @return
     */
    public List<SmsInfo> getAllInboxSms() {
        List<SmsInfo> infos = new ArrayList<SmsInfo>();

        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type" };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, "date desc");
        int personColumn = cursor.getColumnIndex("person");
        int addressColumn = cursor.getColumnIndex("address");
        int bodyColumn = cursor.getColumnIndex("body");
        int dateColumn = cursor.getColumnIndex("date");
        int typeColumn = cursor.getColumnIndex("type");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                SmsInfo smsInfo = new SmsInfo();
                smsInfo.setPerson(cursor.getString(personColumn));
                smsInfo.setAddress(cursor.getString(addressColumn));
                smsInfo.setSmsbody(cursor.getString(bodyColumn));
                smsInfo.setDate(
                    dateFormat.format(new Date(Long.parseLong(cursor.getString(dateColumn))))
                );
                smsInfo.setType(cursor.getString(typeColumn));

                infos.add(smsInfo);
            }
        }

        return infos;
    }
}
