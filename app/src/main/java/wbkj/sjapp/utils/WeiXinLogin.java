package wbkj.sjapp.utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.squareup.okhttp.Request;


import wbkj.sjapp.models.User_wx;

public class WeiXinLogin {
	
	private Context context;
	
	public WeiXinLogin(Context context){
		this.context=context;
	}

	public void getToken(String code) {
		OkHttpClientManager.getAsyn(getUrl1(code), new OkHttpClientManager.ResultCallback<JsonElement>() {
			@Override
			public void onError(Request request, Exception e) {
				Toast.makeText(context, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(JsonElement response) {
				JsonObject object = response.getAsJsonObject();
				Log.e("token------", object.toString());
				String access_token = object.get("access_token").getAsString();
				String openid = object.get("openid").getAsString();
				String scope = object.get("scope").getAsString();
				String unionid = object.get("unionid").getAsString();
				getInfo(access_token, unionid);
			}
		});
	}
	
	/*
	 * 获取用户信息
	 */
	private void getInfo(String str1, String str2) {
		OkHttpClientManager.getAsyn(getUrl2(str1, str2), new OkHttpClientManager.ResultCallback<JsonElement>() {
			@Override
			public void onError(Request request, Exception e) {
				Toast.makeText(context, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(JsonElement response) {
				Log.e("info--------", response.toString());
				User_wx user = new Gson().fromJson(response, User_wx.class);
//				checkAccount(user);
			}
		});
	}


	/**
	 * 通过code获取access_token的url
	 */
	private String getUrl1(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
				+ "appid=" + Contants.APPID
				+ "&secret=" + Contants.APPSRCRET
				+ "&code=" + code
				+ "&grant_type=" + "authorization_code";
		return url;
	}

	/**
	 * 通过access获取用户信息url
	 */
	private String getUrl2(String str1, String str2) {
		String url = "https://api.weixin.qq.com/sns/userinfo?"
				+ "access_token=" + str1
				+ "&openid=" + str2;
		return url;
	}


}
