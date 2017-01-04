package wbkj.sjapp.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;
import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MainActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.activity.BindPhoneActivity;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.User;
import wbkj.sjapp.models.User_wx;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;

public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler {

	private IWXAPI api;

	private BaseActivity baseActivity;
	private MyApplication app;
	private SharedPreferencesUtil sp;

	/**
	 * 处理微信发出的向第三方应用请求app message
	 * <p>
	 * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
	 * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
	 * 做点其他的事情，包括根本不打开任何页面
	 */
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null) {
			Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
			startActivity(iLaunchMyself);
		}
	}

	/**
	 * 处理微信向第三方应用发起的消息
	 * <p>
	 * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
	 * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
	 * 回调。
	 * <p>
	 * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
	 */
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null
				&& (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (MyApplication) getApplication();
		sp = new SharedPreferencesUtil(this);
		baseActivity = new BaseActivity();
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

	@Override
	public void onResp(BaseResp resp) {
		//也是醉了,强转才能拿到     微信真坑
		SendAuth.Resp sendResp = (SendAuth.Resp) resp;
		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
			Log.e("wxapi222----", sendResp.code);
			getToken(sendResp.code);
		}
	}

	private void getToken(String code) {
		OkHttpClientManager.getAsyn(getUrl1(code), new OkHttpClientManager.ResultCallback<JsonElement>() {
			@Override
			public void onError(Request request, Exception e) {
				Toast.makeText(getApplication(), "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
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

	private void getInfo(String str1, String str2) {
		OkHttpClientManager.getAsyn(getUrl2(str1, str2), new OkHttpClientManager.ResultCallback<JsonElement>() {
			@Override
			public void onError(Request request, Exception e) {
				Toast.makeText(getApplication(), "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(JsonElement response) {
//				Log.e("info--------", response.toString());
				User_wx user = new Gson().fromJson(response, User_wx.class);
				Log.e("info--------", user.toString());
				checkAccount(user);
			}
		});
	}

	/**
	 * 验证账号是否已被注册
	 */
	private void checkAccount(final User_wx user) {
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
				new OkHttpClientManager.Param("thirdfrom", "1"),
				new OkHttpClientManager.Param("thirdaccount", user.getOpenid())
		};
		OkHttpClientManager.postAsyn(HttpUtil.loginUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
			@Override
			public void onError(Request request, Exception e) {
				baseActivity.closeProDialog();
				Log.e("err-----", e.getMessage());
				Toast.makeText(WXEntryActivity.this, "网络请求失败，请重新操作", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(JsonElement response) {
				baseActivity.closeProDialog();
				JsonModel json = new Gson().fromJson(response, JsonModel.class);
				if (json.result != 1) {
//					Log.e("------------",response.toString());
					Bundle bundle = new Bundle();
					bundle.putString("tag", "wx");
					bundle.putString("thirdaccount", user.getOpenid());
					bundle.putSerializable("user", user);

					//这里我也不知道为什么  这样写不行
//					baseActivity.skipActivity(WXEntryActivity.this, BindPhoneActivity.class, bundle);
					Intent intent=new Intent();
					intent.setClass(WXEntryActivity.this, BindPhoneActivity.class);

					intent.putExtra(Contants.ARGUMENTS_NAME, bundle);

					startActivity(intent);

				} else {
					Toast.makeText(WXEntryActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
					User user = json.data;
					user.setPassword("");
					sp.onLoginSuccess(user);
					baseActivity.skipActivity(WXEntryActivity.this, MainActivity.class, null);
				}
			}
		}, null);
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