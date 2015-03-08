package com.cisoft.lazyorder.core.address;

import android.content.Context;

import com.cisoft.lazyorder.bean.address.AddressInfo;
import com.cisoft.lazyorder.core.BaseNetwork;
import com.cisoft.lazyorder.finals.ApiConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

public class AddressNetwork extends BaseNetwork {

	public AddressNetwork(Context context) {
		super(context, ApiConstants.MODULE_ADDRESS);
	}

	/**
	 * 通过用户的id获取到该用户的送货地址列表
	 * @param userId
	 * @param loadFinishCallback
	 */
	public void loadAddrListByUId(int userId, final OnAddressListLoadFinish loadFinishCallback) {
		HttpParams params = new HttpParams();
		params.put(ApiConstants.KEY_ADDRESS_UID, String.valueOf(userId));
	
		getRequest(ApiConstants.METHOD_ADDRESS_FIND_ALL, params, new SuccessCallback() {
	      @Override
	      public void onSuccess(String result) {
	          List<AddressInfo> addresses = new ArrayList<AddressInfo>();
	          try {
	              JSONObject jsonObj = new JSONObject(result);
	              JSONArray addressArr = jsonObj.getJSONArray(ApiConstants.KEY_DATA);
	              JSONObject addressObj = null;
	              AddressInfo address = null;
	              for (int i = 0; i < addressArr.length(); i++) {
	            	  addressObj = addressArr.getJSONObject(i);
	            	  address = new AddressInfo(addressObj);
	            	  addresses.add(address);
	              }
	          } catch (JSONException e) {
	              e.printStackTrace();

                  if(loadFinishCallback != null){
                      loadFinishCallback.onFailure(ApiConstants.RES_STATE_SERVICE_EXCEPTION,
                              getResponseStateInfo(ApiConstants.RES_STATE_SERVICE_EXCEPTION));
                  }
	          }
	
	          if(loadFinishCallback != null){
	              loadFinishCallback.onSuccess(addresses);
	          }
	
	      }}, new FailureCallback() {
	
	      @Override
	      public void onFailure(int stateCode, String errorMsg) {
	          if(loadFinishCallback != null){
	              loadFinishCallback.onFailure(stateCode, errorMsg);
	          }
	      }
	  }, new PrepareCallback() {
		  @Override
		  public void onPreStart() {
			  if(loadFinishCallback != null){
				  loadFinishCallback.onPreStart();
			  }
		  }
	  });
	}
	
    /**
     * 新增送货地址
     *
     */
    public void insertAddressByUserId(int userId, AddressInfo addressObj, final OnInsertAddressFinish onInsertAddressFinish) {
    	HttpParams params = new HttpParams();
    	params.put(ApiConstants.KEY_ADDRESS_UID, String.valueOf(userId));
        params.put(ApiConstants.KEY_ADDRESS_NAME, addressObj.getName());
        params.put(ApiConstants.KEY_ADDRESS_PHONE, addressObj.getPhone());
        params.put(ApiConstants.KEY_ADDRESS_ADDRESS, addressObj.getAddress());

        getRequest(ApiConstants.METHOD_ADDRESS_INSERT, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
				try {
					JSONObject jsonObj = new JSONObject(result);
				    int addrId = jsonObj.getInt(ApiConstants.KEY_ADDRESS_ID);
				    
				    if(onInsertAddressFinish != null){
	                	onInsertAddressFinish.onSuccess(addrId);
	                }
				} catch (JSONException e) {
				    e.printStackTrace();
				}
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(onInsertAddressFinish != null){
                	onInsertAddressFinish.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
			
			@Override
			public void onPreStart() {
				onInsertAddressFinish.onPreStart();
			}
		});
    }
    
    /**
     * 修改送货地址
     * @param addressObj
     * @param onUpdateFinishCallback
     */
    public void updateAddressByAddrId(AddressInfo addressObj, final OnUpdateAddressFinish onUpdateFinishCallback) {
    	HttpParams params = new HttpParams();
    	params.put(ApiConstants.KEY_ADDRESS_ID, String.valueOf(addressObj.getId()));
        params.put(ApiConstants.KEY_ADDRESS_NAME, addressObj.getName());
        params.put(ApiConstants.KEY_ADDRESS_PHONE, addressObj.getPhone());
        params.put(ApiConstants.KEY_ADDRESS_ADDRESS, addressObj.getAddress());

        getRequest(ApiConstants.METHOD_ADDRESS_UPDATE, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(onUpdateFinishCallback != null){
                	onUpdateFinishCallback.onSuccess();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(onUpdateFinishCallback != null){
                	onUpdateFinishCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
			
			@Override
			public void onPreStart() {
				onUpdateFinishCallback.onPreStart();
			}
		});
    }
    
    /**
     * 删除送货地址
     * @param addrId
     * @param onDeleteAddressFinish
     */
    public void deleteAddressByAddrId(int userId, int addrId, final OnDeleteAddressFinish onDeleteAddressFinish) {
    	HttpParams params = new HttpParams();
    	params.put(ApiConstants.KEY_ADDRESS_ID, String.valueOf(addrId));
        params.put(ApiConstants.KEY_ADDRESS_UID, String.valueOf(userId));

        getRequest(ApiConstants.METHOD_ADDRESS_DELETE, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(onDeleteAddressFinish != null){
                	onDeleteAddressFinish.onSuccess();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(onDeleteAddressFinish != null){
                	onDeleteAddressFinish.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
			
			@Override
			public void onPreStart() {
				onDeleteAddressFinish.onPreStart();
			}
		});
    }
    
    /**
     * 设置默认地址
     *
     */
    public void setDefaultAddressByAddrId(int userId, int addrId, final OnSetDefaultAddressFinish onSetDefFinishCallback) {
    	HttpParams params = new HttpParams();
        params.put(ApiConstants.KEY_ADDRESS_ID, String.valueOf(addrId));
        params.put(ApiConstants.KEY_ADDRESS_UID, String.valueOf(userId));

        getRequest(ApiConstants.METHOD_ADDRESS_SET_DEFAULT, params, new SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if(onSetDefFinishCallback != null){
                	onSetDefFinishCallback.onSuccess();
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(int stateCode, String errorMsg) {
                if(onSetDefFinishCallback != null){
                	onSetDefFinishCallback.onFailure(stateCode, errorMsg);
                }
            }
        }, new PrepareCallback() {
			
			@Override
			public void onPreStart() {
				onSetDefFinishCallback.onPreStart();
			}
		});
    }
    
	
	public interface OnAddressListLoadFinish {
		
		public void onPreStart();
		
		public void onSuccess(List<AddressInfo> addresses);

		public void onFailure(int stateCode, String errorMsg);
	}
	
	
    public interface OnInsertAddressFinish {
    	
    	public void onPreStart();
    	
        public void onSuccess(int addrId);

        public void onFailure(int stateCode, String errorMsg);
    }
    
    public interface OnUpdateAddressFinish {
    	
    	public void onPreStart();
    	
        public void onSuccess();

        public void onFailure(int stateCode, String errorMsg);
    }
    
    public interface OnDeleteAddressFinish {
    	
    	public void onPreStart();
    	
        public void onSuccess();

        public void onFailure(int stateCode, String errorMsg);
    }
    
    public interface OnSetDefaultAddressFinish {
    	
    	public void onPreStart();
    	
        public void onSuccess();

        public void onFailure(int stateCode, String errorMsg);
    }

}
