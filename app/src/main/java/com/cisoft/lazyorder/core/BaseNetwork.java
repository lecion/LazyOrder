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
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.SystemTool;
import java.io.IOException;
import java.net.MalformedURLException;

public abstract class BaseNetwork {
    protected Context mContext;
    protected KJHttp mKjHttp;
    protected HttpConfig mHttpConfig;
    protected String mModuleName;

    protected BaseNetwork(Context context, String moduleName) {
        mContext = context;
        mModuleName = moduleName;
        mHttpConfig = new HttpConfig();
//        mHttpConfig.cachePath = AppConfig.HTTP_CACHE_PATH;
//        mHttpConfig.cacheTime = AppConfig.IS_DEBUG ? 0 : AppConfig.CACHE_EFFECTIVE_TIME;
        mKjHttp = new KJHttp(mHttpConfig);
    }

    // /**
    // * 以get的方式异步请求Api的网络数据并保存数据到缓存
    // *
    // * @param methodName
    // * Api接口的方法名，如"findAll.json"
    // * @param params
    // * Api接口的参数对
    // * @param successCallback
    // * 成功后的回调
    // * @param failureCallback
    // * 失败后的回调
    // */
    // protected void getRequest(String methodName, final HttpParams params,
    // final SuccessCallback successCallback,
    // final FailureCallback failureCallback) {
    // this.getRequest(methodName, params, true, successCallback,
    // failureCallback);
    // }

    /**
     * 是否保存成功的数据到缓存里
     *
     * @param methodName
     * @param params
     *            是否保存数据为缓存
     * @param successCallback
     * @param failureCallback
     */
    protected void getRequest(String methodName, final HttpParams params,
                              final SuccessCallback successCallback,
                              final FailureCallback failureCallback,
                              final PrepareCallback prepareCallback) {

        // 判断网络状态
        if (!SystemTool.checkNet(mContext)) {
            if (failureCallback != null) {
                failureCallback.onFailure(ApiConstants.RESPONSE_STATE_NOT_NET);
            }
            return;
        }

        final String url = packageAccessApiUrl(methodName, params);

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
                    if (stateCode == ApiConstants.RESPONSE_STATE_SUCCESS) {
//						// 获取数据正常,就保存到缓存里 */
//						if (isSaveCache && !AppConfig.IS_DEBUG)
//							httpCacher.add(url, result);

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

                    // 解析不了,就说明是服务器端的原因了
                    if (failureCallback != null) {
                        failureCallback
                                .onFailure(ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (failureCallback != null) {
                    if (t instanceof MalformedURLException) {
                        ViewInject.toast("不是标准的URL");
                    } else if (t instanceof IOException) {
                        failureCallback
                                .onFailure(ApiConstants.RESPONSE_STATE_NET_POOR);
                    } else {
                        failureCallback
                                .onFailure(ApiConstants.RESPONSE_STATE_FAILURE);
                    }
                }
            }
        });
    }

//	/**
//	 * 以post的方式异步请求Api的网络数据并将获取到的数据保存为缓存
//	 * 
//	 * @param methodName
//	 *            Api接口的方法名，如"findAll.json"
//	 * @param params
//	 *            Api接口的参数对
//	 * @param successCallback
//	 *            成功后的回调
//	 * @param failureCallback
//	 *            失败后的回调
//	 */
//	protected void postRequest(String methodName, final KJStringParams params,
//			final SuccessCallback successCallback,
//			final FailureCallback failureCallback) {
//		this.asyncUrlPost(methodName, params, true, successCallback,
//				failureCallback);
//	}

    /**
     *
     * @param methodName
     * @param params
     *            是否获取到数据后保存为缓存
     * @param successCallback
     * @param failureCallback
     */
    protected void postRequest(String methodName, final HttpParams params, final SuccessCallback successCallback,
                               final FailureCallback failureCallback, final PrepareCallback prepareCallback) {

        // 判断网络状态
        if (!SystemTool.checkNet(mContext)) {
            if (failureCallback != null) {
                failureCallback.onFailure(ApiConstants.RESPONSE_STATE_NOT_NET);
            }
            return;
        }

        final String url = packageAccessApiUrl(methodName, null);

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
                    if (stateCode == ApiConstants.RESPONSE_STATE_SUCCESS) {
//						// 获取数据正常,就保存到缓存里 */
//						if (isSaveCache && !AppConfig.IS_DEBUG)
//							httpCacher.add(url, result);

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

                    // 解析不了,就说明是服务器端的原因了
                    if (failureCallback != null) {
                        failureCallback
                                .onFailure(ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (failureCallback != null) {
                    if (t instanceof MalformedURLException) {
                        ViewInject.longToast("不是标准的URL");
                    } else if (t instanceof IOException) {
                        failureCallback
                                .onFailure(ApiConstants.RESPONSE_STATE_NET_POOR);
                    } else {
                        failureCallback
                                .onFailure(ApiConstants.RESPONSE_STATE_FAILURE);
                    }
                }
            }
        });
    }

    /**
     * 通过方法名包装访问api的url（不带参数的url）
     *
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
     * 根据请求api响应的状态码来获取对应的信息
     *
     * @param stateCode
     * @return
     */
    public String getResponseStateInfo(int stateCode) {
        String stateInfo = "";
        switch (stateCode) {
            case ApiConstants.RESPONSE_STATE_NOT_NET:
                stateInfo = mContext.getResources().getString(
                        R.string.no_net_service);
                break;
            case ApiConstants.RESPONSE_STATE_NET_POOR:
                stateInfo = mContext.getResources().getString(R.string.net_too_poor);
                break;
            case ApiConstants.RESPONSE_STATE_SERVICE_EXCEPTION:
                stateInfo = mContext.getResources().getString(
                        R.string.service_have_error_exception);
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
        public void onFailure(int stateCode);
    }
}
