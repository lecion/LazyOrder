package com.cisoft.shop.util;

import android.content.Context;
import android.text.TextUtils;

import com.cisoft.shop.SpConstants;

import org.kymjs.aframe.utils.PreferenceHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Lecion on 2/20/15.
 */
public class IOUtil {
    public static <T> String encode(T t) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String serStr = null;
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(t);
            serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = URLEncoder.encode(serStr, "utf-8");
            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serStr;
    }

    public static <T> T decode(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        T t = null;
        try {
            String res = URLDecoder.decode(str, "utf-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(res.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            t = (T)objectInputStream.readObject();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 保存登陆信息
     * @param phone
     * @param pwd
     * @param t 保存的对象
     */
    public static <T> void saveLoginInfo(Context ctx, int type, String phone, String pwd, T t) {
        PreferenceHelper.write(ctx, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_TYPE, type);
        PreferenceHelper.write(ctx, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_PHONE, phone);
        PreferenceHelper.write(ctx, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_PWD, pwd);
        PreferenceHelper.write(ctx, SpConstants.SP_FILE_NAME, SpConstants.KEY_LOGIN_OBJ, IOUtil.encode(t));
    }
}
