package com.cisoft.shop;

/**
 * Created by comet on 2014/10/16.
 */
public class AppConfig {


	//缓存失效时间（单位：秒）
	public static final long CACHE_EFFECTIVE_TIME = 60 * 60 * 24 * 2;//两天

    //是否是DEBUG模式（缓存会失效）
	public static final boolean IS_DEBUG = false;

    public static final int TYPE_MERCHANT = 1;

    public static final int TYPE_EXPMER = 2;
}
