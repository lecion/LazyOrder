package com.cisoft.lazyorder;

import java.io.File;

/**
 * Created by comet on 2014/10/16.
 */
public class AppConfig {


    //缓存失效时间（单位：分）
    public static final long CACHE_EFFECTIVE_TIME = 60 * 24 * 2;//两天

    //是否是DEBUG模式（缓存会失效）
    public static final boolean IS_DEBUG = true;

    //app在内存卡里的路径
    public static final String APP_PATH = "CISoft/LazyOrder";

    //网络缓存路径
    public static final String HTTP_CACHE_PATH = APP_PATH + File.separator + "cache";

    //图片缓存路径
    public static final String IMAGE_CACHE_PATH = APP_PATH + File.separator + "image";
}
