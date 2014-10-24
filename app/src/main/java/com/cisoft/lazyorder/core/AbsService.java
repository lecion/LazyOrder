package com.cisoft.lazyorder.core;

import android.content.Context;

import com.cisoft.lazyorder.AppConfig;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.aframe.http.KJHttp;
import org.kymjs.aframe.http.KJStringParams;
import org.kymjs.aframe.http.StringCallBack;
import org.kymjs.aframe.http.cache.HttpCache;
import org.kymjs.aframe.ui.ViewInject;

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
     * 以get的方式异步请求Api的网络数据
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
    protected void asyncUrlGet(String methodName, final KJStringParams params, final boolean isSaveCache, final SuccessCallback successCallback, final FailureCallback failureCallback){
        
    	/*
    	//判断网络状态
    	if (!SystemTool.checkNet(context)) {
    		ErrHandleUtils.sendNotNetReceiver(context);
    		if (failureCallback != null) {
                failureCallback.onFailure(ApiConstants.RESPONSE_STATE_NOT_NET);
            }
    		return;
    	}*/

        final String url = packageApiUrlByMethodName(methodName) + "?" + params.toString();

        kjHttp.urlGet(url, new StringCallBack() {
            @Override
            public void onSuccess(String result) {
                System.out.println("url:" + url);
                System.out.println("params:" + params.toString());
                System.out.println("result:" + result);

                try {
                    JSONObject jsonObj = new JSONObject(result);
                    int stateCode = jsonObj.getInt(ApiConstants.KEY_STATE);
                    if (stateCode == ApiConstants.RESPONSE_STATE_SUCCESS) {
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
                        failureCallback.onFailure(ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (AppConfig.IS_DEBUG) {
                    if (t instanceof MalformedURLException) {
                        ViewInject.longToast("不是标准的URL");
                    } else if (t instanceof IOException) {
                        ViewInject.longToast("网络响应缓慢");
                    } else {
                        ViewInject.longToast("未知异常");
                    }
                } else {
                    if (failureCallback != null) {
                        failureCallback.onFailure(ApiConstants.RESPONSE_STATE_FAILURE);
                    }
                }
            }
        });
    }




    /**
     * 以post的方式异步请求Api的网络数据
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
    	/*if (!SystemTool.checkNet(context)) {
    		ErrHandleUtils.sendNotNetReceiver(context);
    		if (failureCallback != null) {
                failureCallback.onFailure(ApiConstants.RESPONSE_STATE_NOT_NET);
            }
    		return;
    	}
    	*/

        final String url = packageApiUrlByMethodName(methodName);

        kjHttp.urlPost(url, params, new StringCallBack() {
            @Override
            public void onSuccess(String result) {
                System.out.println("url:" + url);
                System.out.println("params:" + params.toString());
                System.out.println("result:" + result);

                try {
                    JSONObject jsonObj = new JSONObject(result);
                    int stateCode = jsonObj.getInt(ApiConstants.KEY_STATE);
                    if (stateCode == ApiConstants.RESPONSE_STATE_SUCCESS) {
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
                        failureCallback.onFailure(ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (AppConfig.IS_DEBUG) {
                    if (t instanceof MalformedURLException) {
                        ViewInject.longToast("不是标准的URL");
                    } else if (t instanceof IOException) {
                        ViewInject.longToast("网络响应缓慢");
                    } else {
                        ViewInject.longToast("未知异常");
                    }
                } else {
                    if (failureCallback != null) {
                        failureCallback.onFailure(ApiConstants.RESPONSE_STATE_FAILURE);
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
    protected String packageApiUrlByMethodName (String methodName) {
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConstants.SERVER_URL);
        sb.append(ApiConstants.URL_SEPERATOR);
        sb.append(moduleName);
        sb.append(ApiConstants.URL_SEPERATOR);
        sb.append(methodName);

        return sb.toString();
    }


    /**
     * 根据请求api响应的状态码来获取对应的信息
     * @param stateCode
     * @return
     */
    public abstract String getResponseStateInfo(int stateCode);




    public static interface SuccessCallback{
        public void onSuccess(String result) throws JSONException;
    }

    public static interface FailureCallback{
        public void onFailure(int stateCode);
    }
}
