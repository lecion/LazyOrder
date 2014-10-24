package com.cisoft.lazyorder.core.goods;

import android.content.Context;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Goods;
import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.finals.ApiConstants;

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
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_COM_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_COM_SIZE, String.valueOf(size));
        super.asyncUrlGet(ApiConstants.METHOD_COMMODITY_FIND_ALL_BY_MER_ID, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Goods> goodsList = new ArrayList<Goods>();
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
