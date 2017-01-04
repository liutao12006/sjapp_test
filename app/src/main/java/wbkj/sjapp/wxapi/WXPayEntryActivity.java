package wbkj.sjapp.wxapi;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import wbkj.sjapp.R;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Contants.APPID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@SuppressLint("LongLogTag")
	@Override
	public void onResp(BaseResp resp) {
		Log.e(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("提示");
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//			builder.show();
			if (resp.errCode == -2) {
				Toast.makeText(this, "取消支付", Toast.LENGTH_SHORT).show();
			} else if (resp.errCode == -1) {
				Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
				notifyShoping();
			}
			finish();
		}
	}



	/*
  通知商家的短信接口
   */
	public void notifyShoping(){

		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
		};
		OkHttpClientManager.postAsyn(HttpUtil.smsUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
			@Override
			public void onError(Request request, Exception e) {

			}

			@Override
			public void onResponse(JsonElement response) {

			}
		}, null);
	}



}