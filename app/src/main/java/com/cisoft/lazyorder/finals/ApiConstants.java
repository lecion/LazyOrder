package com.cisoft.lazyorder.finals;

/**
 * Created by comet on 2014/10/18.
 */
public class ApiConstants {

    public static final String SERVER_URL = "http://comit-wh.net/lazyorder/index.php";
    //public static final String SERVER_URL = "http://lazyorder.yliec.com";
    public static final String URL_SEPERATOR = "/";

    /* 这里存放响应的状态码 */
    public static final int RESPONSE_STATE_SUCCESS = 200;
    public static final int RESPONSE_STATE_FAILURE = 404;
    public static final int RESPONSE_STATE_SERVICE_EXCEPTION = 103;
    public static final int RESPONSE_STATE_NOT_NET = 102;
    public static final int RESPONSE_STATE_NET_POOR = 101;

    /* 这里存放全局使用的key */
    public static final String KEY_STATE = "state";
    public static final String KEY_DATA = "data";

    /* 这里存放API接口里的模块名（控制器名）,以"MODULE_"打头 */
    public static final String MODULE_MERCHANTS = "merchants";
    public static final String MODULE_MER_CATEGORY = "merCategory";

    public static final String MODULE_COMMODITY = "commodity";
    public static final String MODULE_COM_CATEGORY = "category";
    public static final String MODULE_COM_DISCUSS = "discuss";

    public static final String MODULE_BUILD = "building";

    public static final String MODULE_HISTORY_ORDER = "order";


    /* 这里存放API接口里的方法名,以"METHOD_+模块名"打头 */
    public static final String METHOD_MERCHANTS_FIND_BY_TYPE_ID = "findMerchantsOrCanteen";
    public static final String METHOD_MER_CATEGORY_FIND_ALL = "findAll";

    public static final String METHOD_COMMODITY_FIND_ALL_BY_MER_ID = "findCommodityByMerchantsId";
    public static final String METHOD_COMMODITY_FIND_BY_MER_AND_TYPE_ID = "findCommodityByMerchantsIdAndTypeId";
    public static final String METHOD_CATEGORY_FIND_ALL_BY_MER_ID = "findCategoryByMerchantsId";
    public static final String METHOD_DISCUSS_FIND_ALL_BY_COM_ID = "findDiscussByCommodityId";

    public static final String METHOD_BUILD_FIND_ALL = "findBuildings";

    public static final String METHOD_HIS_ORDER_FIND_ALL = "findOrderByUserPhone";


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

    public static final String KEY_COM_MER_ID = "merId";
    public static final String KEY_COM_PAGE = "page";
    public static final String KEY_COM_SIZE = "size";
    public static final String KEY_COM_SORT = "sortType";
    public static final String KEY_COM_ID = "id";
    public static final String KEY_COM_NAME = "cmName";
    public static final String KEY_COM_PICTURE = "cmPicture";
    public static final String KEY_COM_CAT_ID = "catId";
    public static final String KEY_COM_CAT_NAME = "catName";
    public static final String KEY_COM_SALES_NUM = "salesNum";
    public static final String KEY_COM_PRICE = "cmPrice";
    public static final String KEY_COM_TYPE_ID = "typeId";

    public static final String KEY_CAT_MER_ID = "merId";
    public static final String KEY_CAT_ID = "id";
    public static final String KEY_CAT_NAME = "catName";


    public static final String KEY_DIS_COM_ID = "comId";
    public static final String KEY_DIS_PAGE = "page";
    public static final String KEY_DIS_SIZE = "size";
    public static final String KEY_DIS_ID = "id";
    public static final String KEY_DIS_USER_NAME = "userName";
    public static final String KEY_DIS_USER_ID = "userId";
    public static final String KEY_DIS_CONTENT = "discussContent";
    public static final String KEY_DIS_CONTENT_TIME = "contentTime";


    public static final String KEY_BUILD_DATA = "data";
    public static final String KEY_BUILD_ID = "id";
    public static final String KEY_BUILD_NAME = "building";
    public static final String KEY_BUILD_SCHOOL_ID = "schoolId";


    public static final String KEY_HIS_ORDER_DATA = "data";
    public static final String KEY_HIS_ORDER_PAGE = "page";
    public static final String KEY_HIS_ORDER_PAGER = "size";
    public static final String KEY_HIS_ORDER_USER_PHONE = "userPhone";
    public static final String KEY_HIS_ORDER_ID = "id";
    public static final String KEY_HIS_ORDER_MER_ID = "merId";
    public static final String KEY_HIS_ORDER_MER_NAME = "merName";
    public static final String KEY_HIS_ORDER_TIME = "orderDate";
    public static final String KEY_HIS_ORDER_ADDRESS = "address";
    public static final String KEY_HIS_ORDER_GOOD_LIST = "orderComVOlist";
    public static final String KEY_HIS_ORDER_TOTAL_PRICE = "moneyAll";
    public static final String KEY_HIS_ORDER_GOOD_NAME = "comName";
    public static final String KEY_HIS_ORDER_GOOD_COUNT = "comNum";
}
