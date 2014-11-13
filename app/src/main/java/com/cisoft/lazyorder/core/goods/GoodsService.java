package com.cisoft.lazyorder.core.goods;

import android.app.Activity;
import android.content.Context;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.goods.GoodsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.KJLoger;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 10/21/14.
 */
public class GoodsService extends AbsService {

    public GoodsService(Context context, String moduleName) {
        super(context, moduleName);
    }

    /**
     * 根据店铺ID获得所有商品
     * @param page 页码
     * @param size 每页显示的数量
     * @param sortType 排序方式，默认为人气
     */
    public void loadGoodsList(int shopId, final int page, int size, final String sortType, final onNetwordFinished<Goods> onNetwordFinished) {
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_COM_MER_ID, String.valueOf(shopId));
        params.put(ApiConstants.KEY_COM_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_COM_SIZE, String.valueOf(size));
        params.put(ApiConstants.KEY_COM_SORT, sortType);
        super.asyncUrlGet(ApiConstants.METHOD_COMMODITY_FIND_ALL_BY_MER_ID, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Goods> goodsList = new ArrayList<Goods>();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(result);
                    JSONArray jsonArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        goodsList.add(new Goods(jsonArr.getJSONObject(i)));
                    }
                    if (onNetwordFinished != null) {
                        onNetwordFinished.onSuccess(goodsList);
                    }
                } catch (JSONException e) {
                    //这里是json格式不对，无法完成解析
                    if (onNetwordFinished != null) {
                        onNetwordFinished.onFailure(getResponseStateInfo(ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION));
                    }
                }

            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                //这里出错，根据code查询错误信息
                if (onNetwordFinished != null) {
                    onNetwordFinished.onFailure(getResponseStateInfo(stateCode));
                }
            }
        });
    }


    public void loadGoodsListByType(int shopId, int typeId, final int page, int size, final String sortType) {
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_COM_MER_ID, String.valueOf(shopId));
        params.put(ApiConstants.KEY_COM_TYPE_ID, String.valueOf(typeId));
        params.put(ApiConstants.KEY_COM_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_COM_SIZE, String.valueOf(size));
        params.put(ApiConstants.KEY_COM_SORT, sortType);
        final GoodsFragment f = getFragmentBySortType(sortType);
        super.asyncUrlGet(ApiConstants.METHOD_COMMODITY_FIND_BY_MER_AND_TYPE_ID, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                List<Goods> goodsList = new ArrayList<Goods>();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(result);
                    JSONArray jsonGoodsDataArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
                    //判断数据是否为空
                    if (jsonGoodsDataArr.length() == 0) {
                        if (page == 1) {
                            ViewInject.toast(context.getResources().getString(R.string.have_not_goods_list));
                            f.showNoValueTip();
                        } else {
                            ViewInject.toast(context.getResources().getString(R.string.have_no_more_goods_list));
                            f.setPullLoadEnable(false);
                        }
                        return;
                    }
                    for (int i = 0; i < jsonGoodsDataArr.length(); i++) {
                        goodsList.add(new Goods(jsonGoodsDataArr.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ViewInject.toast("解析数据异常，请稍后再试");
                }
                f.setGoodsData(goodsList);
                f.restoreState();
                KJLoger.debug("loadGoodsListByType " + result);
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                KJLoger.debug("loadGoodsListByType " + stateCode);
                ViewInject.toast(getResponseStateInfo(stateCode));
                f.showNoValueTip();
            }
        });

    }


    @Override
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RESPONSE_STATE_FAILURE:
                stateInfo = context.getResources().getString(R.string.fail_to_load_goods_list);
                break;
            case ApiConstants.RESPONSE_STATE_SUCCESS:
                stateInfo = context.getResources().getString(R.string.success_to_load_goods_list);
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

    /**
     * 根据sortType获得相应类型的GoodsFragment
     * @param sortType
     * @return
     */
    public GoodsFragment getFragmentBySortType(String sortType) {
        GoodsFragment f = null;
        if (sortType.equals(GoodsFragment.ORDER_POP)) {
            //销量排序
            f = ((GoodsFragment)((Activity)context).getFragmentManager().findFragmentByTag(GoodsFragment.ORDER_POP));
        } else {
            f = ((GoodsFragment)((Activity)context).getFragmentManager().findFragmentByTag(GoodsFragment.ORDER_PRICE));
        }
        return f;
    }

    /**
     * 网络请求完毕后回调
     */
    public interface onNetwordFinished<T> {
        public void onSuccess(List<T> l);

        public void onFailure(String info);
    }
}
