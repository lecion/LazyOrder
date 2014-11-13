package com.cisoft.lazyorder.core.goods;

import android.app.Activity;
import android.content.Context;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Comment;
import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.ui.goods.GoodsFragment;
import com.cisoft.lazyorder.widget.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.ui.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lecion on 2014/11/1.
 */
public class GoodsCommentService extends AbsService{

    public GoodsCommentService(Context context) {
        super(context, ApiConstants.MODULE_COM_DISCUSS);
    }


    /**
     * 根据商品ID从网络加载评论数据
     * @param lvComment
     * @param page
     * @param pager
     * @param commentHandler
     */
    public void loadAllCommentByGoodsId(MyListView lvComment, int id, final int page, int pager, String sortType, final GoodsFragment.CommentHandler commentHandler){
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_DIS_COM_ID, String.valueOf(id));
        params.put(ApiConstants.KEY_MER_PAGE, String.valueOf(page));
        params.put(ApiConstants.KEY_MER_PAGER, String.valueOf(pager));
        final GoodsFragment f = getFragmentBySortType(sortType);
        super.asyncUrlGet(ApiConstants.METHOD_DISCUSS_FIND_ALL_BY_COM_ID, params, false, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                List<Comment> comments = new ArrayList<Comment>();
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray commentArr = jsonObj.getJSONArray(ApiConstants.KEY_MER_DATA);
                    if (commentArr.length() == 0) {
                        if (page == 1) {	//第一页就空数据的话就显示提示
                            f.showNoValueTip();
                        } else {
                            ViewInject.toast("没有更多店家数据了");
                            f.setPullLoadEnable(false);
                        }
                        return;
                    }

                    JSONObject commentObj = null;
                    Comment comment = null;
                    for (int i = 0; i < commentArr.length(); i++) {
                        commentObj = commentArr.getJSONObject(i);
                        comment = new Comment(commentObj);
                        comments.add(comment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                commentHandler.handleComment(comments);

                //((ShopActivity)context).hideLoadingTip();


                /* 将数据添加到适配器中并刷新  * /
                if (page == 1) //下拉刷新，先清空集合，再添加
                    ((ShopActivity)context).shopListAdapter.clearAll();
                ((ShopActivity)context).shopListAdapter.addData(comments);
                ((ShopActivity)context).shopListAdapter.refresh();
                ((ShopActivity)context).lvShopList.stopRefreshData();
                /**/

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode) {
                ViewInject.toast(getResponseStateInfo(stateCode));
                //((ShopActivity)context).lvShopList.stopRefreshData();
                //((ShopActivity)context).hideLoadingTip();
                if (page == 1) {
//                    ((ShopActivity)context).showNoValueTip();
                }
            }
        });
    }




    /**
     * 从网络加载指定类别的店家列表的数据
     * @param page
     * @param pager
     */
    /*public void loadShopDataByTypeId(int typeId, final int page, int pager){
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

                // 将数据添加到适配器中并刷新
                if (page == 1) //下拉刷新，先清空集合，再添加
                    ((ShopActivity)context).shopListAdapter.clearAll();
                ((ShopActivity)context).shopListAdapter.addData(shops);
                ((ShopActivity)context).shopListAdapter.refresh();
                ((ShopActivity)context).lvShopList.stopRefreshData();

            }}, new FailureCallback() {

            @Override
            public void onFailure(int stateCode) {
                ViewInject.toast(getResponseStateInfo(stateCode));
                ((ShopActivity)context).lvShopList.stopRefreshData();
                ((ShopActivity)context).hideLoadingTip();
                if (page == 1) {
                    ((ShopActivity)context).showNoValueTip();
                }
            }
        });
    }

*/

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
            default:
                stateInfo = super.getResponseStateInfo(stateCode);
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
        if (sortType.equals(GoodsFragment.ORDER_SALES_NUM)) {
            //销量排序
            f = ((GoodsFragment)((Activity)context).getFragmentManager().findFragmentByTag(GoodsFragment.ORDER_SALES_NUM));
        } else {
            f = ((GoodsFragment)((Activity)context).getFragmentManager().findFragmentByTag(GoodsFragment.ORDER_PRICE));
        }
        return f;
    }
}
