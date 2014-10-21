package com.cisoft.lazyorder.finals;

/**
 * Created by comet on 2014/10/18.
 */
public class ApiConstants {

    public static final String SERVER_URL = "http://wanghong940821.test.cqyun.net/LazyOrder/index.php";
    public static final String URL_SEPERATOR = "/";

    /* 这里存放响应的状态码 */
    public static final int RESPONSE_STATE_SUCCESS = 200;
    public static final int RESPONSE_STATE_FAILURE = 404;
    public static final int RESPONSE_STATE_SERVICE_EXCEPTION = 1;
    public static final int RESPONSE_STATE_NOT_NET = 0;

    /* 这里存放全局使用的key */
    public static final String KEY_STATE = "state";
    public static final String KEY_DATA = "data";

    /* 这里存放API接口里的模块名（控制器名）,以"MODULE_"打头 */
    public static final String MODULE_MERCHANTS = "merchants";
    public static final String MODULE_MER_CATEGORY = "merCategory";


    /* 这里存放API接口里的方法名,以"METHOD_+模块名"打头 */
    public static final String METHOD_MERCHANTS_FIND_ALL = "findAll";
    public static final String METHOD_MERCHANTS_FIND_BY_TYPE_ID = "findMerchantsOrCanteen";
    public static final String METHOD_MER_CATEGORY_FIND_ALL = "findAll";

    /* 这里存放返回json的key,以"KEY_ + 模块名简写"打头,以模块扎堆↖(^ω^)↗ */
    public static final String KEY_MER_PAGE = "page";
    public static final String KEY_MER_PAGER = "size";
    public static final String KEY_MER_DATA = "data";
    public static final String KEY_MER_ID = "id";
    public static final String KEY_MER_MONTH_SALES = "monthSaleNum";
    public static final String KEY_MER_OPEN_TIME = "merOpenTime";
    public static final String KEY_MER_CLOSE_TIME = "merCloseTime";
    public static final String KEY_MER_NAME = "merName";
    public static final String KEY_MER_FACE_PIC = "merPic";
    public static final String KEY_MER_OPEN_STATE = "operatingState";
    public static final String KEY_MER_ADDRESS = "merAddress";
    public static final String KEY_MER_PROMOTION_INFO = "sales";
    public static final String KEY_MER_TYPE_ID = "typeId";
    
    public static final String KEY_MC_DATA = "data";
    public static final String KEY_MC_CATEGORY_ID = "id";
    public static final String KEY_MC_CATEGORY_NAME = "merCategoryName";
}
