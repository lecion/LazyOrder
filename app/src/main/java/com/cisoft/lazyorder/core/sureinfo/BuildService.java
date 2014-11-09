package com.cisoft.lazyorder.core.sureinfo;

import android.content.Context;

import com.cisoft.lazyorder.AppConfig;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.sureinfo.Build;
import com.cisoft.lazyorder.core.AbsService;
import com.cisoft.lazyorder.finals.ApiConstants;
import com.cisoft.lazyorder.widget.ChoiceAddressDialog;
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
public class BuildService extends AbsService{

    public BuildService(Context context) {
        super(context, ApiConstants.MODULE_BUILD);
    }

    /**
     * 根据学校id从网络获取该校的楼栋列表
     * @param schoolId
     * @param loadFinish
     */
    public void loadBuildListFromNet(int schoolId, final ChoiceAddressDialog.BuildDataLoadFinish loadFinish){
        KJStringParams params = new KJStringParams();
        params.put(ApiConstants.KEY_BUILD_SCHOOL_ID, String.valueOf(schoolId));

        String url = packageApiUrlByMethodNameAndParams(ApiConstants.METHOD_BUILD_FIND_ALL, params);
        String result = null;
        result = httpCacher.get(url);
        if (result != null && !AppConfig.IS_DEBUG) {
            List<Build> builds = new ArrayList<Build>();
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray buildArr = jsonObj.getJSONArray(ApiConstants.KEY_BUILD_DATA);
                JSONObject buildObj = null;
                Build build = null;
                for (int i = 0; i < buildArr.length(); i++) {
                    buildObj = buildArr.getJSONObject(i);
                    build = new Build(buildObj);
                    builds.add(build);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(loadFinish != null){
                loadFinish.onLoadFinish(builds);
            }
        } else {
            super.asyncUrlGet(ApiConstants.METHOD_BUILD_FIND_ALL, params, false, new SuccessCallback() {
                @Override
                public void onSuccess(String result) {
                    List<Build> builds = new ArrayList<Build>();
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        JSONArray buildArr = jsonObj.getJSONArray(ApiConstants.KEY_BUILD_DATA);
                        JSONObject buildObj = null;
                        Build build = null;
                        for (int i = 0; i < buildArr.length(); i++) {
                            buildObj = buildArr.getJSONObject(i);
                            build = new Build(buildObj);
                            builds.add(build);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(loadFinish != null){
                        loadFinish.onLoadFinish(builds);
                    }

                }
            }, new FailureCallback() {

                @Override
                public void onFailure(int stateCode) {
                    ViewInject.toast(getResponseStateInfo(stateCode));
                }
            });
        }
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
                stateInfo = context.getResources().getString(R.string.fail_to_load_build_list);
                break;
            case ApiConstants.RESPONSE_STATE_SUCCESS:
                stateInfo = context.getResources().getString(R.string.success_to_load_build_list);
                break;
            default:
                stateInfo = super.getResponseStateInfo(stateCode);
                break;
        }

        return stateInfo;
    }
}
