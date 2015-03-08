package com.cisoft.lazyorder.core;

import android.content.Context;
import com.cisoft.lazyorder.AppConfig;
import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.finals.ApiConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.utils.SystemTool;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public abstract class BaseNetwork {
    protected Context mContext;
    protected KJHttp mKjHttp;
    protected HttpConfig mHttpConfig;
    protected String mModuleName;

    protected BaseNetwork(Context context, String moduleName) {
        mContext = context;
        mModuleName = moduleName;
        mHttpConfig = new HttpConfig();
        mHttpConfig.cachePath = AppConfig.HTTP_CACHE_PATH;
        mKjHttp = new KJHttp(mHttpConfig);
    }

    /**
     * 以get的请求方式且不使用缓存的方式来获取网络数据
     * @param methodName
     * @param params
     * @param successCallback
     * @param failureCallback
     * @param prepareCallback
     */
    protected void getRequest(String methodName, final HttpParams params,
                              final SuccessCallback successCallback,
                              final FailureCallback failureCallback,
                              final PrepareCallback prepareCallback) {
        getRequest(methodName, params, false, successCallback, failureCallback, prepareCallback);
    }

    /**
     * 以get的请求方式来获取网络数据
     * @param methodName
     * @param params
     * @param isUseCache
     * @param successCallback
     * @param failureCallback
     * @param prepareCallback
     */
    protected void getRequest(String methodName, final HttpParams params, boolean isUseCache,
                              final SuccessCallback successCallback,
                              final FailureCallback failureCallback,
                              final PrepareCallback prepareCallback) {
        // 判断网络状态
        if (!SystemTool.checkNet(mContext)) {
            if (failureCallback != null) {
                failureCallback.onFailure(ApiConstants.RES_STATE_NOT_NET,
                        getResponseStateInfo(ApiConstants.RES_STATE_NOT_NET));
            }
            return;
        }
        // 打包url
        final String url = packageAccessApiUrl(methodName, params);
        // 根据是否使用缓存来设置缓存时间
        mHttpConfig.cacheTime = (AppConfig.IS_DEBUG || !isUseCache) ? 0 : AppConfig.CACHE_EFFECTIVE_TIME;
        mKjHttp.get(url, new HttpCallBack() {

            @Override
            public void onPreStart() {
                if (prepareCallback != null) {
                    prepareCallback.onPreStart();
                }
            }

            @Override
            public void onSuccess(String result) {
                System.out.println("url:" + url);
                if (params != null)
                    System.out.println("params:" + params.toString());
                System.out.println("result:" + result);
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    int stateCode = jsonObj.getInt(ApiConstants.KEY_STATE);
                    if (stateCode == ApiConstants.RES_STATE_SUCCESS) {
                        if (successCallback != null) {
                            successCallback.onSuccess(result);
                        }
                    // 不正常的状态下才获取message
                    } else {
                        String errorMsg = jsonObj.getString(ApiConstants.KEY_MESSAGE);
                        if (failureCallback != null) {
                            failureCallback.onFailure(stateCode, errorMsg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    // 解析不了,就说明是服务器端的原因了
                    if (failureCallback != null) {
                        failureCallback
                                .onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                        getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (failureCallback != null) {
                    // 错误的URL(协议错误)
                    if (t instanceof MalformedURLException) {
                        failureCallback
                                .onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                        getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    // 404(Api地址错误)
                    }else if (t instanceof FileNotFoundException) {
                        failureCallback
                                .onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                        getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    // 连接超时(网络差)
                    } else if (t instanceof SocketException || t instanceof SocketTimeoutException) {
                        failureCallback
                                .onFailure(ApiConstants.RES_STATE_NET_POOR,
                                        getResponseStateInfo(ApiConstants.RES_STATE_NET_POOR));
                    } else {
                        failureCallback
                                .onFailure(ApiConstants.RES_STATE_UNKNOWN_EXCEPTION,
                                        getResponseStateInfo(ApiConstants.RES_STATE_UNKNOWN_EXCEPTION));
                    }
                }
            }
        });
    }

	/**
	 * 以post的请求方式且不使用缓存的方式来获取网络数据
     * @param methodName
     * @param params
     * @param successCallback
     * @param failureCallback
     * @param prepareCallback
     */
	protected void postRequest(String methodName, final HttpParams params,
                               final SuccessCallback successCallback,
                               final FailureCallback failureCallback,
                               final PrepareCallback prepareCallback) {
		postRequest(methodName, params, false, successCallback, failureCallback, prepareCallback);
	}

    /**
     * 以post的请求方式来获取网络数据
     * @param methodName
     * @param params
     * @param isUseCache
     * @param successCallback
     * @param failureCallback
     * @param prepareCallback
     */
    protected void postRequest(String methodName, final HttpParams params, boolean isUseCache,
                               final SuccessCallback successCallback,
                               final FailureCallback failureCallback,
                               final PrepareCallback prepareCallback) {
        // 判断网络状态
        if (!SystemTool.checkNet(mContext)) {
            if (failureCallback != null) {
                failureCallback.onFailure(ApiConstants.RES_STATE_NOT_NET,
                        getResponseStateInfo(ApiConstants.RES_STATE_NOT_NET));
            }
            return;
        }
        // 打包url
        final String url = packageAccessApiUrl(methodName, null);
        // 根据是否使用缓存来设置缓存时间
        mHttpConfig.cacheTime = (AppConfig.IS_DEBUG || !isUseCache) ? 0 : AppConfig.CACHE_EFFECTIVE_TIME;
        mKjHttp.post(url, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                if (prepareCallback != null) {
                    prepareCallback.onPreStart();
                }
            }

            @Override
            public void onSuccess(String result) {
                System.out.println("url:" + url);
                if (params != null)
                    System.out.println("params:" + params.toString());
                System.out.println("result:" + result);

                try {
                    JSONObject jsonObj = new JSONObject(result);
                    int stateCode = jsonObj.getInt(ApiConstants.KEY_STATE);
                    if (stateCode == ApiConstants.RES_STATE_SUCCESS) {
                        if (successCallback != null) {
                            successCallback.onSuccess(result);
                        }
                    // 不正常的状态下才获取message
                    } else {
                        String errorMsg = jsonObj.getString(ApiConstants.KEY_MESSAGE);
                        if (failureCallback != null) {
                            failureCallback.onFailure(stateCode, errorMsg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    // 解析不了,就说明是服务器端的原因了
                    if (failureCallback != null) {
                        failureCallback
                                .onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                        getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (failureCallback != null) {
                    // 错误的URL(协议错误)
                    if (t instanceof MalformedURLException) {
                        failureCallback
                                .onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                        getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    // 404(Api地址错误)
                    }else if (t instanceof FileNotFoundException) {
                        failureCallback
                                .onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                                        getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                    // 连接超时(网络差)
                    } else if (t instanceof SocketException || t instanceof SocketTimeoutException) {
                        failureCallback
                                .onFailure(ApiConstants.RES_STATE_NET_POOR,
                                        getResponseStateInfo(ApiConstants.RES_STATE_NET_POOR));
                    } else {
                        failureCallback
                                .onFailure(ApiConstants.RES_STATE_UNKNOWN_EXCEPTION,
                                        getResponseStateInfo(ApiConstants.RES_STATE_UNKNOWN_EXCEPTION));
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
    protected String packageAccessApiUrl(String methodName, HttpParams params) {
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConstants.SERVER_URL);
        sb.append(ApiConstants.URL_SEPERATOR);
        sb.append(mModuleName);
        sb.append(ApiConstants.URL_SEPERATOR);
        sb.append(methodName);
        if (params != null) {
            sb.append("?");
            sb.append(params.toString());
        }

        return sb.toString();
    }

    /**
     * 根据请求api响应的状态码来获取全局响应信息
     * @param stateCode
     * @return
     */
    protected String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RES_STATE_NOT_NET:
                stateInfo = mContext.getResources().getString(
                        R.string.toast_have_not_network);
                break;
            case ApiConstants.RES_STATE_NET_POOR:
                stateInfo = mContext.getResources().getString(R.string.toast_network_too_poor);
                break;
            case ApiConstants.RES_STATE_SERVICE_EXCEPTION:
                stateInfo = mContext.getResources().getString(
                        R.string.toast_service_have_exception);
                break;
            case ApiConstants.RES_STATE_UNKNOWN_EXCEPTION:
                stateInfo = mContext.getResources().getString(R.string.toast_unknown_exception);
                break;
        }

        return stateInfo;
    }

    public interface PrepareCallback {
        public void onPreStart();
    }

    public interface SuccessCallback {
        public void onSuccess(String result) throws JSONException;
    }

    public interface FailureCallback {
        public void onFailure(int stateCode, String errorMsg);
    }
}
