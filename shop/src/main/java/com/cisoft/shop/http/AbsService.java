package com.cisoft.shop.http;

import android.content.Context;

import com.cisoft.shop.Api;
import com.cisoft.shop.R;
import com.cisoft.shop.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.http.cache.HttpCache;
import org.kymjs.aframe.ui.ViewInject;
import org.kymjs.aframe.utils.ErrHandleUtils;
import org.kymjs.aframe.utils.SystemTool;

import java.io.IOException;
import java.net.MalformedURLException;

public abstract class AbsService {
    protected Context context;
    protected HttpCache httpCacher;
    protected KJHttp kjHttp;
    protected String moduleName;

    protected AbsService(Context context, String moduleName){
        this.context = context;
        this.moduleName = moduleName;

        kjHttp = new KJHttp();
        httpCacher = HttpCache.create();
        httpCacher.setDebug(AppConfig.IS_DEBUG);
        httpCacher.setEffectiveTime(AppConfig.CACHE_EFFECTIVE_TIME);
    }


    /**
     * 以get的方式异步请求Api的网络数据并保存数据到缓存
     * @param methodName Api接口的方法名，如"findAll.json"
     * @param params Api接口的参数对
     * @param successCallback 成功后的回调
     * @param failureCallback 失败后的回调
     */
    protected void asyncUrlGet(String methodName, final KJStringParams params, final SuccessCallback successCallback, final FailureCallback failureCallback){
        this.asyncUrlGet(methodName, params, true, successCallback, failureCallback);
    }


    /**
     * 是否保存成功的数据到缓存里
     * @param methodName
     * @param params
     * @param isSaveCache 是否保存数据为缓存
     * @param successCallback
     * @param failureCallback
     */
    protected void asyncUrlGet(String methodName, KJStringParams params, final boolean isSaveCache, final SuccessCallback successCallback, final FailureCallback failureCallback){
        
    	//判断网络状态
    	if (!SystemTool.checkNet(context)) {
    		ErrHandleUtils.sendNotNetReceiver(context);
    		if (failureCallback != null) {
                failureCallback.onFailure(Api.RESPONSE_STATE_NOT_NET);
            }
    		return;
    	}

        final String url = packageApiUrlByMethodNameAndParams(methodName, params);

        kjHttp.urlGet(url, new StringCallBack() {
            @Override
            public void onSuccess(String result) {
                System.out.println("url:" + url);
                System.out.println("result:" + result);

                try {
                    JSONObject jsonObj = new JSONObject(result);
                    int stateCode = jsonObj.getInt(Api.KEY_STATE);
                    if (stateCode == Api.RESPONSE_STATE_SUCCESS) {
                        //获取数据正常,就保存到缓存里 */
                        if (isSaveCache && !AppConfig.IS_DEBUG)
                            httpCacher.add(url, result);

                        if (successCallback != null) {
                            successCallback.onSuccess(result);
                        }
                    } else {
                        if (failureCallback != null) {
                            failureCallback.onFailure(stateCode);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    //解析不了,就说明是服务器端的原因了
                    if (failureCallback != null) {
                        failureCallback.onFailure(Api.RESPONSE_STATE_SERVICE_EXCEPTION);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (failureCallback != null) {
                    if (t instanceof MalformedURLException) {
                        ViewInject.longToast("不是标准的URL");
                    } else if (t instanceof IOException) {
                        failureCallback.onFailure(Api.RESPONSE_STATE_NET_POOR);
                    } else {
                        failureCallback.onFailure(Api.RESPONSE_STATE_FAILURE);
                    }
                }
            }
        });
    }




    /**
     * 以post的方式异步请求Api的网络数据并将获取到的数据保存为缓存
     * @param methodName Api接口的方法名，如"findAll.json"
     * @param params Api接口的参数对
     * @param successCallback 成功后的回调
     * @param failureCallback 失败后的回调
     */
    protected void asyncUrlPost(String methodName, final KJStringParams params, final SuccessCallback successCallback, final FailureCallback failureCallback){
        this.asyncUrlPost(methodName, params, true, successCallback, failureCallback);
    }


    /**
     *
     * @param methodName
     * @param params
     * @param isSaveCache 是否获取到数据后保存为缓存
     * @param successCallback
     * @param failureCallback
     */
    protected void asyncUrlPost(String methodName, final KJStringParams params, final boolean isSaveCache, final SuccessCallback successCallback, final FailureCallback failureCallback) {

        //判断网络状态
        if (!SystemTool.checkNet(context)) {
            ErrHandleUtils.sendNotNetReceiver(context);
            if (failureCallback != null) {
                failureCallback.onFailure(Api.RESPONSE_STATE_NOT_NET);
            }
            return;
        }


        final String url = packageApiUrlByMethodNameAndParams(methodName, null);

        kjHttp.urlPost(url, params, new StringCallBack() {
            @Override
            public void onSuccess(String result) {
                System.out.println("url:" + url);
                System.out.println("params:" + params.toString());
                System.out.println("result:" + result);

                try {
                    JSONObject jsonObj = new JSONObject(result);
                    int stateCode = jsonObj.getInt(Api.KEY_STATE);
                    if (stateCode == Api.RESPONSE_STATE_SUCCESS) {
                        //获取数据正常,就保存到缓存里 */
                        if (isSaveCache && !AppConfig.IS_DEBUG)
                            httpCacher.add(url, result);

                        if (successCallback != null) {
                            successCallback.onSuccess(result);
                        }
                    } else {
                        if (failureCallback != null) {
                            failureCallback.onFailure(stateCode);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    //解析不了,就说明是服务器端的原因了
                    if (failureCallback != null) {
                        failureCallback.onFailure(Api.RESPONSE_STATE_SERVICE_EXCEPTION);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (failureCallback != null) {
                    if (t instanceof MalformedURLException) {
                        ViewInject.longToast("不是标准的URL");
                    } else if (t instanceof IOException) {
                        failureCallback.onFailure(Api.RESPONSE_STATE_NET_POOR);
                    } else {
                        failureCallback.onFailure(Api.RESPONSE_STATE_FAILURE);
                    }
                }
            }
        });
    }

    /**
     * 通过方法名包装访问api的url（不带参数的url）
     * @param methodName
     * @return
     */
    protected String packageApiUrlByMethodNameAndParams(String methodName, KJStringParams params) {
        StringBuilder sb = new StringBuilder();
        sb.append(Api.SERVER_URL);
        sb.append(Api.URL_SEPERATOR);
        sb.append(moduleName);
        sb.append(Api.URL_SEPERATOR);
        sb.append(methodName);
        if (params != null) {
            sb.append("?");
            sb.append(params.toString());
        }

        return sb.toString();
    }


    /**
     * 根据请求api响应的状态码来获取对应的信息
     * @param stateCode
     * @return
     */
    public String getResponseStateInfo(int stateCode){
        String stateInfo = "";
        switch (stateCode) {
            case Api.RESPONSE_STATE_NOT_NET:
                stateInfo = context.getResources().getString(R.string.no_net_service);
                break;
            case Api.RESPONSE_STATE_NET_POOR:
                stateInfo = context.getResources().getString(R.string.net_too_poor);
                break;
            case Api.RESPONSE_STATE_SERVICE_EXCEPTION:
                stateInfo = context.getResources().getString(R.string.service_have_error_exception);
                break;
        }

        return stateInfo;
    }



    public static interface SuccessCallback{
        public void onSuccess(String result) throws JSONException;
    }

    public static interface FailureCallback{
        public void onFailure(int stateCode);
    }
}
