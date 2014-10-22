package com.cisoft.lazyorder.core.shop;

import android.content.Context;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.shop.Shop;
import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.shop.ShopActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comet on 2014/10/20.
 */
public class ShopService extends AbsService{

    public ShopService(Context context) {
        super(context, ApiConstants.MODULE_MERCHANTS);
    }


    /**
     * 从网络加载全部（不区分类别）店家列表的数据
     * @param page
     * @param pager
     */
    public void loadAllShopData(final int page, int pager){
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_MER_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_MER_PAGER, String.valueOf(pager));

        super.asyncUrlGet(ApiConstants.METHOD_MERCHANTS_FIND_ALL, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Shop> shops = new ArrayList<Shop>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray shopArr = jsonObj.getJSONArray(ApiConstants.KEY_MER_DATA);


                    if (shopArr.length() == 0) {
                        if (page == 1) {	//第一页就空数据的话就显示提示
                            ((ShopActivity)context).showNoValueTip();
                        } else {
                            ViewInject.toast("没有更多店家数据了");
                            ((ShopActivity)context).lvShopList.setPullLoadEnable(false);
                        }
                        return;
                    }

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

                ((ShopActivity)context).hideLoadingTip();


                /* 将数据添加到适配器中并刷新  */
                if (page == 1) //下拉刷新，先清空集合，再添加
                    ((ShopActivity)context).shopListAdapter.clearAll();
                ((ShopActivity)context).shopListAdapter.addData(shops);
                ((ShopActivity)context).shopListAdapter.refresh();
                ((ShopActivity)context).lvShopList.stopRefreshData();

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode) {
                ViewInject.toast(getResponseStateInfo(stateCode));
                if (page == 1) {
                    ((ShopActivity)context).showNoValueTip();
                }
            }
        });
    }



    /**
     * 从网络加载指定类别的店家列表的数据
     * @param page
     * @param pager
     */
    public void loadShopDataByTypeId(int typeId, final int page, int pager){
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_MER_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_MER_PAGER, String.valueOf(pager));
        params.put(ApiConstants.KEY_MER_TYPE_ID, String.valueOf(typeId));

        super.asyncUrlGet(ApiConstants.METHOD_MERCHANTS_FIND_BY_TYPE_ID, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Shop> shops = new ArrayList<Shop>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray shopArr = jsonObj.getJSONArray(ApiConstants.KEY_MER_DATA);

                    if (shopArr.length() == 0) {
                        if (page == 1) {	//第一页就空数据的话就显示提示
                            ((ShopActivity)context).showNoValueTip();
                        } else {
                            ViewInject.toast("没有更多店家数据了");
                            ((ShopActivity)context).lvShopList.setPullLoadEnable(false);
                        }
                        return;
                    }

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

                ((ShopActivity)context).hideLoadingTip();

                /* 将数据添加到适配器中并刷新  */
                if (page == 1) //下拉刷新，先清空集合，再添加
                    ((ShopActivity)context).shopListAdapter.clearAll();
                ((ShopActivity)context).shopListAdapter.addData(shops);
                ((ShopActivity)context).shopListAdapter.refresh();
                ((ShopActivity)context).lvShopList.stopRefreshData();

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode) {
                ViewInject.toast(getResponseStateInfo(stateCode));
                if (page == 1) {
                    ((ShopActivity)context).showNoValueTip();
                }
            }
        });
    }



    /**
     * 根据请求api响应的状态码来获取对应的信息
     * @param stateCode
     * @return
     */
    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RESPONSE_STATE_FAILURE:
                stateInfo = context.getResources().getString(R.string.fail_to_load_shop_list);
                break;
            case ApiConstants.RESPONSE_STATE_SUCCESS:
                stateInfo = context.getResources().getString(R.string.success_to_load_shop_list);
                break;
            case ApiConstants.RESPONSE_STATE_NOT_NET:
                stateInfo = context.getResources().getString(R.string.no_net_service);
                break;
            case ApiConstants.RESPONSE_STATE_NET_POOR:
                stateInfo = context.getResources().getString(R.string.net_too_poor);
                break;
            case ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION:
                stateInfo = context.getResources().getString(R.string.service_have_error_exception);
                break;
        }

        return stateInfo;
    }
}
