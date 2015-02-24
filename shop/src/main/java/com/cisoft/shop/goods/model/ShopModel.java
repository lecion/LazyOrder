package com.cisoft.shop.goods.model;

import android.content.Context;

import com.cisoft.myapplication.R;
import com.cisoft.shop.ApiConstants;
import com.cisoft.shop.bean.Shop;
import com.cisoft.shop.http.AbsService;
import com.cisoft.shop.util.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJStringParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2014/10/20.
 */
public class ShopModel extends AbsService {

    public ShopModel(Context context) {
        super(context, ApiConstants.MODULE_MERCHANTS);
    }

    /**
     * 从网络加载指定类别的店家列表的数据
     * @param page
     * @param pager
     */
    public void loadShopDataByTypeId(int typeId, final int page, int pager, final INetWorkFinished<Shop> loadFinishCallback){
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_MER_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_MER_PAGER, String.valueOf(pager));
        params.put(ApiConstants.KEY_MER_TYPE_ID, String.valueOf(typeId));

        super.asyncUrlGet(ApiConstants.METHOD_MERCHANTS_FIND_BY_TYPE_ID, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Shop> shops = new ArrayList<Shop>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray shopArr = jsonObj.getJSONArray(ApiConstants.KEY_MER_DATA);
                    JSONObject shopObj = null;
                    Shop shop = null;
                    for (int i = 0; i < shopArr.length(); i++) {
                        shopObj = shopArr.getJSONObject(i);
                        shop = new Shop(shopObj);
                        shops.add(shop);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(loadFinishCallback != null){
                    loadFinishCallback.onSuccess(shops);
                }

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode) {
                if(loadFinishCallback != null){
                    loadFinishCallback.onFailure(getResponseStateInfo(stateCode));
                }

            }
        });
    }

    /**
     * 更改商店营业状态
     * @param state
     */
    public void updateOperateState(int state, final IUpdateOperateState finishedListener) {
        Shop shop = L.getShop(context);
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_MER_OPERATE_STATE, String.valueOf(state));
        params.put(ApiConstants.KEY_MER_MER_ID, String.valueOf(shop.getId()));
        asyncUrlGet(ApiConstants.METHOD_MER_UPDATE_OPERATE_STATE, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObj = new JSONObject(result);
                int state = jsonObj.getInt(ApiConstants.KEY_STATE);
                String data = jsonObj.getString(ApiConstants.KEY_DATA);
                if (state == 200) {
                    finishedListener.onSuccess(state);
                } else {
                    finishedListener.onFailure(data);
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                finishedListener.onFailure(getResponseStateInfo(stateCode));
            }
        });
    }

    /**
     * 普通商家登陆
     * @param merPhone
     * @param merPwd
     */
    public void merLogin(String merPhone, String merPwd, final ILoginListener loginListener) {
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_MER_MER_PHONE, merPhone);
        params.put(ApiConstants.KEY_MER_MER_PWD, merPwd);
        asyncUrlGet(ApiConstants.METHOD_MER_MER_LOGIN, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObj = new JSONObject(result);
                int state = jsonObj.getInt(ApiConstants.KEY_STATE);
                if (state == 200) {
                    //登陆成功
                    loginListener.onSuccess(jsonObj.getJSONObject("data"));
                } else {
                    loginListener.onFailure(getResponseStateInfo(state));
                }

            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                loginListener.onFailure(getResponseStateInfo(stateCode));
            }
        });
    }

    public interface IUpdateOperateState{
        public void onSuccess(int code);

        public void onFailure(String msg);
    }

    public interface ILoginListener {
        public void onSuccess(JSONObject data);

        public void onFailure(String msg);
    }

    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RESPONSE_STATE_ERROR_LOGIN:
                stateInfo = "账号或密码错误";
                break;
            case ApiConstants.RESPONSE_STATE_SUCCESS:
                stateInfo = "加载商品成功";
                break;
            case ApiConstants.RESPONSE_STATE_NOT_NET:
                stateInfo = context.getResources().getString(R.string.no_net_service);
                break;
            case ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION:
                stateInfo = context.getResources().getString(R.string.service_have_error_exception);
                break;
        }
        return stateInfo;
    }
}
