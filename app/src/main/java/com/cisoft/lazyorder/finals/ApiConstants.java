package com.cisoft.lazyorder.finals;

/**
 * Created by comet on 2014/10/18.
 */
public class ApiConstants {


    public final static String HOST = "123.56.84.203:8584/Lazyer";
    public final static String HTTP = "http://";
    public final static String HTTPS = "https://";
    public static final String URL_SEPERATOR = "/";
    public static final String SERVER_URL = HTTP + HOST;


    /* 这里存放响应的状态码 */
    public static final int RES_STATE_SUCCESS = 200;
    public static final int RES_STATE_FAILURE = 400;
    public static final int RES_STATE_NET_POOR = 101;
    public static final int RES_STATE_NOT_NET = 102;
    public static final int RES_STATE_SERVICE_EXCEPTION = 103;
    public static final int RES_STATE_UNKNOWN_EXCEPTION = 104;


    /* 这里存放全局使用的key */
    public static final String KEY_STATE = "state";
    public static final String KEY_DATA = "data";
    public static final String KEY_MESSAGE = "message";


    /* 这里存放API接口里的模块名（控制器名）,以"MODULE_"打头 */
    public static final String MODULE_MERCHANTS = "merchants";
    public static final String MODULE_MER_CATEGORY = "merCategory";
    public static final String MODULE_COMMODITY = "commodity";
    public static final String MODULE_COM_CATEGORY = "category";
    public static final String MODULE_COM_DISCUSS = "discuss";
    public static final String MODULE_ORDER = "order";
    public static final String MODULE_EXPRESS = "express";
    public static final String MODULE_COMMON = "common";
    public static final String MODULE_ABOUT = "about";
    public static final String MODULE_ADVERTISE = "advertise";
    public static final String MODULE_ADDRESS = "address";
    public static final String MODULE_ACCOUNT = "user";
    public static final String MODULE_SETTING = "setting";

    /* 这里存放API接口里的方法名,以"METHOD_+模块名简写"打头 */
    public static final String METHOD_MERCHANTS_FIND_BY_TYPE_ID = "findMerchantsOrCanteen.json";

    public static final String METHOD_MER_CATEGORY_FIND_ALL = "findAll.json";

    public static final String METHOD_COMMODITY_FIND_ALL_BY_MER_ID = "findCommodityByMerchantsId.json";
    public static final String METHOD_COMMODITY_FIND_BY_MER_AND_TYPE_ID = "findCommodityByMerchantsIdAndTypeId.json";
    public static final String METHOD_COMMODITY_FIND_COMMODITY_BY_KEY = "findCommodityByKey.json";
    public static final String METHOD_CATEGORY_FIND_ALL_BY_MER_ID = "findCategoryByMerchantsId.json";
    public static final String METHOD_DISCUSS_FIND_ALL_BY_COM_ID = "findDiscussByCommodityId";


    public static final String METHOD_EXPRESS_FIND_ALL_BY_PHONE = "findExpressByuserPhone.json";

    public static final String METHOD_ORDER_SETTLE = "settleOrder.json";
    public static final String METHOD_ORDER_FIND_ALL = "findOrderByUserPhone.json";
    public static final String METHOD_ORDER_SUBMIT = "saveOrder.json";

    public static final String METHOD_COMMON_GET_SMS_AUTH_CODE = "getNum.json";
    public static final String METHOD_COMMON_VERIFY_PHONE = "phoneVerify.json";

    public static final String METHOD_ABOUT_CHECK_UPDATE = "checkUpdate.json";
    public static final String METHOD_ABOUT_SUBMIT_FEEDBACK = "sendFeedback.json";

    public static final String METHOD_ADDRESS_FIND_ALL = "addressList.json";
    public static final String METHOD_ADDRESS_INSERT = "addAddress.json";
    public static final String METHOD_ADDRESS_UPDATE = "modifyAddress.json";
    public static final String METHOD_ADDRESS_DELETE = "deleteAddress.json";
    public static final String METHOD_ADDRESS_SET_DEFAULT = "setDefaultAddress.json";

    public static final String METHOD_ACCOUNT_REGISTER = "userRegister.json";
    public static final String METHOD_ACCOUNT_LOGIN = "login.json";
    public static final String METHOD_ACCOUNT_UPDATE_PWD = "updatePassword.json";
    public static final String METHOD_ACCOUNT_UPDATE_PHONE = "setUserPhone.json";

    public static final String METHOD_ADVERTISE_FIND_ALL = "findAllAdvertise.json";


    /* 这里存放返回json的key,以"KEY_ + 模块名简写"打头,以模块扎堆↖(^ω^)↗ */
    // MERCHANTS模块下的KEY:
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

    // MER_CATEGORY模块下的KEY:
    public static final String KEY_MC_SCHOOL_ID = "schoolId";
    public static final String KEY_MC_CATEGORY_ID = "id";
    public static final String KEY_MC_CATEGORY_NAME = "merCategoryName";

    // COMMODITY模块下的KEY:
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

    // COM_CATEGORY模块下的KEY:
    public static final String KEY_CAT_MER_ID = "merId";
    public static final String KEY_CAT_ID = "id";
    public static final String KEY_CAT_NAME = "catName";

    // DISCUSS模块下的KEY:
    public static final String KEY_DIS_COM_ID = "comId";
    public static final String KEY_DIS_PAGE = "page";
    public static final String KEY_DIS_SIZE = "size";
    public static final String KEY_DIS_ID = "id";
    public static final String KEY_DIS_USER_NAME = "userName";
    public static final String KEY_DIS_USER_ID = "userId";
    public static final String KEY_DIS_CONTENT = "discussContent";
    public static final String KEY_DIS_CONTENT_TIME = "contentTime";

    // ORDER模板下的KEY
    public static final String KEY_ORDER_PAGE = "page";
    public static final String KEY_ORDER_PAGER = "size";
    public static final String KEY_ORDER_ID = "id";
    public static final String KEY_ORDER_NUMBER = "orderNumber";
    public static final String KEY_ORDER_TIME = "orderTime";
    public static final String KEY_ORDER_SHOP_ID = "merId";
    public static final String KEY_ORDER_SHOP_NAME = "merName";
    public static final String KEY_ORDER_USER_ID = "userId";
    public static final String KEY_ORDER_NAME = "userName";
    public static final String KEY_ORDER_PHONE = "userPhone";
    public static final String KEY_ORDER_ADDRESS = "address";
    public static final String KEY_ORDER_GOODS_LIST = "orderComVOList";
    public static final String KEY_ORDER_PRICE = "orderPrice";    //订单价格
    public static final String KEY_ORDER_DEDUCTION = "deduction";   //优惠减免
    public static final String KEY_ORDER_SHIPPING_FEE = "distributionPrice";  //配送费
    public static final String KEY_ORDER_SETTLED_PRICE = "settledPrice";    //结算后的价格
    public static final String KEY_ORDER_EXTRA_MSG = "extraMsg";
    public static final String KEY_ORDER_COM_ID = "comId";
    public static final String KEY_ORDER_COM_NAME = "comName";
    public static final String KEY_ORDER_GOODS_COUNT = "comNum";
    public static final String KEY_ORDER_JSON_STR = "jsonData";



    // EXPRESS模板下的KEY
    public static final String KEY_EXPRESS_PAGE = "page";
    public static final String KEY_EXPRESS_PAGER = "size";
    public static final String KEY_EXPRESS_ID = "id";
    public static final String KEY_EXPRESS_ADDRESS = "address";
    public static final String KEY_EXPRESS_EXTRA_MSG = "content";
    public static final String KEY_EXPRESS_SHIPPING_FEE = "distributionPrice";
    public static final String KEY_EXPRESS_NUMBER = "expressNumber";
    public static final String KEY_EXPRESS_SUBMIT_TIME = "expressTime";
    public static final String KEY_EXPRESS_OBTAIN_TIME = "getmessageTime";
    public static final String KEY_EXPRESS_SMS_CONTENT = "message";
    public static final String KEY_EXPRESS_STATE = "statue";
    public static final String KEY_EXPRESS_NAME = "userName";
    public static final String KEY_EXPRESS_PHONE = "userPhone";


    // ABOUT模块下的KEY:
    public static final String KEY_ABOUT_VERSION_CODE = "versionCode";
    public static final String KEY_ABOUT_VERSION_NAME = "versionName";
    public static final String KEY_ABOUT_DOWNLOAD_URL = "downloadUrl";
    public static final String KEY_ABOUT_UPDATE_CONTENT = "updateContent";
    public static final String KEY_ABOUT_FEEDBACK_CONTENT = "content";
    public static final String KEY_ABOUT_CONTACT_METHOD = "contactMethod";


    // COMMON模块下所需要的KEY
    public static final String KEY_COMMON_USER_PHONE = "phone";
    public static final String KEY_COMMON_SMS_AUTH_CODE = "num";


    // ADVERTISE模块下的KEY:
    public static final String KEY_ADVERTISE_TYPE = "advertiseType";
    public static final String KEY_ADVERTISE_TYPE_ADVERTISE = "ADVERTISE";
    public static final String KEY_ADVERTISE_TYPE_NOTICE = "NOTICE";
    public static final String KEY_ADVERTISE_IMAGE_URL = "advertisePic";
    public static final String KEY_ADVERTISE_CONTENT_URL = "advertiseUrl";
    public static final String KEY_ADVERTISE_CONTENT_TITLE = "advertiseInfo";
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

    // ACCOUNT模块下的KEY:
    public static final String KEY_ACCOUNT_USER_ID = "userId";
    public static final String KEY_ACCOUNT_USER_NAME = "userName";
    public static final String KEY_ACCOUNT_USER_ACCOUNT = "account";
    public static final String KEY_ACCOUNT_USER_PHONE = "userPhone";
    public static final String KEY_ACCOUNT_USER_PWD = "userPwd";
    public static final String KEY_ACCOUNT_USER_EMAIL = "userEmail";
    public static final String KEY_ACCOUNT_USER_FACE_URL = "userFaceUrl";
    public static final String KEY_ACCOUNT_DEF_ADDRESS = "defAddress";
}
