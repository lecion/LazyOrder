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
     * 从网络获取商品数据
     * @param page 页码
     * @param size 每页显示的数量
     */
    public void loadGoodsDataFromNet(int page, int size) {
        this.loadGoodsDataFromNet(page, size, ApiConstants.KEY_COM_SALES_NUM);
    }

    /**
     *
     * @param page 页码
     * @param size 每页显示的数量
     * @param sortType 排序方式，默认为人气
     */
    public void loadGoodsDataFromNet(final int page, int size, final String sortType) {
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_COM_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_COM_SIZE, String.valueOf(size));
        super.asyncUrlGet(ApiConstants.METHOD_COMMODITY_FIND_ALL_BY_MER_ID, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) throws JSONException {
                List<Goods> goodsList = new ArrayList<Goods>();
                JSONObject jsonObj = new JSONObject(result);
                JSONArray jsonGoodsDataArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);

                for (int i = 0; i < jsonGoodsDataArr.length(); i++) {
                    goodsList.add(new Goods(jsonGoodsDataArr.getJSONObject(i)));
                }
                if (sortType.equals(GoodsFragment.ORDER_POP)) {
                    //销量排序
                    GoodsFragment f = ((GoodsFragment)((Activity)context).getFragmentManager().findFragmentByTag(GoodsFragment.ORDER_POP));
                    KJLoger.debug("GoodsFragment " + sortType + f);
                    f.setGoodsData(goodsList);
                } else {
                    GoodsFragment f = ((GoodsFragment)((Activity)context).getFragmentManager().findFragmentByTag(GoodsFragment.ORDER_PRICE));
                    KJLoger.debug("GoodsFragment " + sortType + f);
                    f.setGoodsData(goodsList);
                }
                KJLoger.debug("loadGoodsDataFromNet " + result);


            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode) {
                KJLoger.debug("loadGoodsDataFromNet " + stateCode);
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
                stateInfo = context.getResources().getString(R.string.no_net_receiver);
                break;
            case ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION:
                stateInfo = context.getResources().getString(R.string.service_have_error_exception);
                break;
        }
        return stateInfo;
    }
}
