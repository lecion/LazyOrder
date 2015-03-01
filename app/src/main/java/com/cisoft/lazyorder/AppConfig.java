package com.cisoft.lazyorder;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by comet on 2014/10/16.
 */
public class AppConfig {

    //是否是DEBUG模式（缓存会失效）
    public static final boolean IS_DEBUG = false;

    //缓存失效时间（单位：分）
    public static final long CACHE_EFFECTIVE_TIME = 60 * 24 * 2; //两天
    //app在内存卡里的路径
    public static final String APP_PATH = "CISoft/LazyOrder";
    //网络缓存路径
    public static final String HTTP_CACHE_PATH = APP_PATH + File.separator + "cache";
    //图片缓存路径
    public static final String IMAGE_CACHE_PATH = APP_PATH + File.separator + "image";
    //配置文件的文件名
    public static final String CONFIG_FILE_NAME = "config";

    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }


    /**
     * 添加额外的Properties
     * @param ps
     */
    public void addProps(Properties ps) {
        Properties props = getProps();
        props.putAll(ps);
        setProps(props);
    }

    /**
     * 设置properties对象
     * @param p
     */
    public void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            File dirConf = mContext.getDir(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
            File conf = new File(dirConf, CONFIG_FILE_NAME);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到Properties对象
     * @return
     */
    public Properties getProps() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            File dirConf = mContext.getDir(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + CONFIG_FILE_NAME);
            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return props;
    }

    /**
     * 为制定key的property设置值
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        Properties props = getProps();
        props.setProperty(key, value);
        setProps(props);
    }

    /**
     * 得到制定key的property值
     * @param key
     * @return
     */
    public String getValue(String key) {
        Properties props = getProps();
        return (props != null) ? props.getProperty(key) : null;
    }

    /**
     * 删除指定key的property
     * @param key
     */
    public void remove(String... key) {
        Properties props = getProps();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }
}
