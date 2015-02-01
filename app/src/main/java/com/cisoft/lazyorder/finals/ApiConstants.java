package com.cisoft.lazyorder.finals;

/**
 * Created by comet on 2014/10/18.
 */
public class ApiConstants {

    //短信提供商的电话号码
    public static final String SMS_PROVIDER_PHONE = "106903662027";

    public static final String SERVER_URL = "http://comit-wh.net/lazyorder/index.php";
    //public static final String SERVER_URL = "http://lazyorder.yliec.com";
    public static final String URL_SEPERATOR = "/";

    //功能说明的url
    public static final String FUNCTION_DESC_URL = "";

    /* 这里存放响应的状态码 */
    public static final int RESPONSE_STATE_SUCCESS = 200;
    public static final int RESPONSE_STATE_FAILURE = 400;
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

    public static final String MODULE_BUILDING = "building";

    public static final String MODULE_ORDER = "order";

    public static final String MODULE_COMMON = "common";
    public static final String MODULE_ABOUT = "about";
    public static final String MODULE_ADVERTISE = "advertise";
    public static final String MODULE_ADDRESS = "address";






    /* 这里存放API接口里的方法名,以"METHOD_+模块名"打头 */
    public static final String METHOD_MERCHANTS_FIND_BY_TYPE_ID = "findMerchantsOrCanteen";
    public static final String METHOD_MER_CATEGORY_FIND_ALL = "findAll";

    public static final String METHOD_COMMODITY_FIND_ALL_BY_MER_ID = "findCommodityByMerchantsId";
    public static final String METHOD_COMMODITY_FIND_BY_MER_AND_TYPE_ID = "findCommodityByMerchantsIdAndTypeId";
    public static final String METHOD_COMMODITY_FIND_COMMODITY_BY_KEY = "findCommodityByKey";
    public static final String METHOD_CATEGORY_FIND_ALL_BY_MER_ID = "findCategoryByMerchantsId";
    public static final String METHOD_DISCUSS_FIND_ALL_BY_COM_ID = "findDiscussByCommodityId";

    public static final String METHOD_BUILDING_FIND_ALL = "findBuildings";

    public static final String METHOD_ORDER_FIND_ALL = "findOrderByUserPhone";
    public static final String METHOD_ORDER_SAVE_ORDER = "saveOrder";
    public static final String METHOD_ORDER_EXPRESS_ORDER_LIST = "expressOrderList";

    public static final String METHOD_COMMON_GET_SMS_AUTH_CODE = "getNum";
    public static final String METHOD_COMMON_VERIFY_PHONE = "phoneVerify";

    public static final String METHOD_ABOUT_CHECK_UPDATE = "checkUpdate";
    public static final String METHOD_ABOUT_SUBMIT_FEEDBACK = "submitFeedback";

    public static final String METHOD_ADDRESS_FIND_ALL = "addressList";
    public static final String METHOD_ADDRESS_INSERT = "addAddress";
    public static final String METHOD_ADDRESS_UPDATE = "modifyAddress";
    public static final String METHOD_ADDRESS_DELETE = "deleteAddress";
    public static final String METHOD_ADDRESS_SET_DEFAULT = "setDefaultAddress";





    /* 这里存放返回json的key,以"KEY_ + 模块名简写"打头,以模块扎堆↖(^ω^)↗ */
    public static final String KEY_MER_PAGE = "page";
    public static final String KEY_MER_PAGER = "size";
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
    public static final String KEY_COM_KEY_NAME = "keyName";

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


    public static final String KEY_BUILDING_DATA = "data";
    public static final String KEY_BUILDING_ID = "id";
    public static final String KEY_BUILDING_NAME = "building";
    public static final String KEY_BUILDING_SCHOOL_ID = "schoolId";



    /*   ORDER模板下的KEY   */

    //ORDER模块下的SAVE_ORDER方法所需要的KEY：
    public static final String KEY_ORDER_SAVE_ORDER_TYPE = "orderType";
    public static final String KEY_ORDER_SAVE_ORDER_TYPE_DISH = "DISH";
    public static final String KEY_ORDER_SAVE_ORDER_TYPE_EXPRESS = "EXPRESS";
    public static final String KEY_ORDER_SAVE_ORDER_USER_NAME = "userName";
    public static final String KEY_ORDER_SAVE_ORDER_USER_PHONE = "userPhone";
    public static final String KEY_ORDER_SAVE_ORDER_BUILDING_ID = "buildingId";
    public static final String KEY_ORDER_SAVE_ORDER_ROOM_NUM = "dormitory";
    public static final String KEY_ORDER_SAVE_ORDER_SHOP_ID = "merchantId";
    public static final String KEY_ORDER_SAVE_ORDER_EXTRA_MSG = "content";
    public static final String KEY_ORDER_SAVE_ORDER_SMS_CONTENT = "message";
    public static final String KEY_ORDER_SAVE_ORDER_COM_LIST = "comList";
    public static final String KEY_ORDER_SAVE_ORDER_COM_LIST_ITEM_COM_ID = "comId";
    public static final String KEY_ORDER_SAVE_ORDER_COM_LIST_ITEM_ORDERED_COUNT = "num";
    public static final String KEY_ORDER_SAVE_ORDER_JSON_DATA = "jsonData";
    public static final String KEY_ORDER_SAVE_ORDER_DATA = "data";
    public static final String KEY_ORDER_SAVE_ORDER_AFTER_MSG = "message";
    public static final String KEY_ORDER_SAVE_ORDER_AFTER_ORDER_NUM = "orderNumber";
    public static final String KEY_ORDER_SAVE_ORDER_AFTER_MONEY_ALL = "moneyAll";

    //ORDER模块下的FIND_ALL方法所需要的KEY：
    public static final String KEY_ORDER_FIND_ALL_DATA = "data";
    public static final String KEY_ORDER_FIND_ALL_PAGE = "page";
    public static final String KEY_ORDER_FIND_ALL_PAGER = "size";
    public static final String KEY_ORDER_FIND_ALL_USER_PHONE = "userPhone";
    public static final String KEY_ORDER_FIND_ALL_ID = "id";
    public static final String KEY_ORDER_FIND_ALL_SHOP_ID = "merId";
    public static final String KEY_ORDER_FIND_ALL_SHOP_NAME = "merName";
    public static final String KEY_ORDER_FIND_ALL_SUBMIT_TIME = "orderDate";
    public static final String KEY_ORDER_FIND_ALL_ORDER_NUM = "orderNum";
    public static final String KEY_ORDER_FIND_ALL_EXTRA_MSG = "message";
    public static final String KEY_ORDER_FIND_ALL_ADDRESS = "address";
    public static final String KEY_ORDER_FIND_ALL_MONEY_ALL = "moneyAll";
    public static final String KEY_ORDER_FIND_ALL_GOODS_LIST = "orderComVOlist";
    public static final String KEY_ORDER_FIND_ALL_GOODS_LIST_ITEM_GOODS_ID = "comId";
    public static final String KEY_ORDER_FIND_ALL_GOODS_LIST_ITEM_GOODS_NAME = "comName";
    public static final String KEY_ORDER_FIND_ALL_GOODS_LIST_ITEM_GOODS_ORDERED_COUNT = "comNum";

    //ORDER模块下的EXPRESS_ORDER_LIST方法所需要的KEY：
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_DATA = "data";
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_USER_PHONE = "userPhone";
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_PAGE = "page";
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_PAGER = "size";
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_ID = "id";
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_BUILDING_ID = "buildingName";
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_ROOM_NUM = "dormitory";
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_SSM = "message";
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_EXTRA_MSG = "content";
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_DELIVERY_MONEY = "deliveryMoney";
    public static final String KEY_ORDER_EXPRESS_ORDER_LIST_SUBMIT_TIME = "submitTime";


    public static final String KEY_ORDER_GOODS_NAME = "comName";
    public static final String KEY_ORDER_GOODS_COUNT = "comNum";

    //ABOUT模块下的KEY:
    public static final String KEY_ABOUT_VERSION_CODE = "versionCode";
    public static final String KEY_ABOUT_VERSION_NAME = "versionName";
    public static final String KEY_ABOUT_DOWNLOAD_URL = "downloadUrl";
    public static final String KEY_ABOUT_UPDATE_CONTENT = "updateContent";
    public static final String KEY_ABOUT_FEEDBACK_CONTENT = "content";
    public static final String KEY_ABOUT_CONTACT_METHOD = "contactMethod";


    //COMMON模块下所需要的KEY

    public static final String KEY_COMMON_USER_PHONE = "phone";
    public static final String KEY_COMMON_SMS_AUTH_CODE = "num";


    //ADVERTISE模块下的KEY:
    public static final String KEY_ADVERTISE_TYPE = "type";
    public static final String KEY_ADVERTISE_IMAGE_URL = "imageUrl";
    public static final String KEY_ADVERTISE_CONTENT_URL = "contentUrl";
    public static final String KEY_ADVERTISE_CONTENT_TITLE = "contentTitle";
    public static final String KEY_ADVERTISE_SHOP_ID = "shopId";
    public static final String KEY_ADVERTISE_SHOP_NAME = "shopName";
    public static final String KEY_ADVERTISE_SHOP_FACE_URL = "faceUrl";
    public static final String KEY_ADVERTISE_SHOP_ADDRESS = "shopAddress";
    public static final String KEY_ADVERTISE_SHOP_PROMOTION_INFO = "promotionInfo";

    // ADDRESS模块下的KEY:
    public static final String KEY_ADDRESS_UID = "userId";
    public static final String KEY_ADDRESS_ID = "addrId";
    public static final String KEY_ADDRESS_NAME = "name";
    public static final String KEY_ADDRESS_PHONE = "phone";
    public static final String KEY_ADDRESS_ADDRESS = "address";
    public static final String KEY_ADDRESS_IS_DEFAULT = "isDefault";
}
