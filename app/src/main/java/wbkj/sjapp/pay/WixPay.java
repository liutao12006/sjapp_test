package wbkj.sjapp.pay;

import android.content.Context;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

import wbkj.sjapp.models.WxPay;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.MD5;
import wbkj.sjapp.utils.WeiXinLogin;

public class WixPay {

    private IWXAPI api;

    private WxPay pay;

    private Context context;

    public WixPay(Context context) {
        this.context=context;
        api = WXAPIFactory.createWXAPI(context, Contants.APPID, false);
        api.registerApp(Contants.APPID);
    }

    public WixPay(Context context, WxPay pay) {
        this.pay = pay;
        this.context=context;

        api = WXAPIFactory.createWXAPI(context, Contants.APPID, false);
        api.registerApp(Contants.APPID);
    }

    /**
     * 微信支付
     */
    public void wxPay() {

        PayReq request = new PayReq();
        request.appId = Contants.APPID;       //开放平台appID
        request.partnerId = Contants.PARTNERID;           //支付商户号
        request.prepayId= pay.prepay_id;       //
        request.packageValue = "Sign=WXPay";       //固定值
        request.nonceStr= pay.nonce_str;       //
        request.timeStamp= pay.timeStamp;          //时间戳
//        request.sign= pay.sign;

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", request.appId));
        signParams.add(new BasicNameValuePair("noncestr", request.nonceStr));
        signParams.add(new BasicNameValuePair("package", request.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", request.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", request.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", request.timeStamp));

        request.sign = genAppSign(signParams);
        api.sendReq(request);
    }

    /**
     * 微信登陆
     */
    public void wxLogin() {

        api.registerApp(Contants.APPID);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";//应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
        req.state = "shangjieapp";//重定向后会带上state参数  这个参数不是必须的  所以应该是开发者可以填写a-zA-Z0-9的参数值都可以
//        req.scope = "snsapi_userinfo";
//        req.state = "wechat_sdk_demo";
        api.sendReq(req);
    }

    



    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append("qwertyuiop1234567890zxcvbnmlkjhg");

        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }

//    @Override
//    public void onReq(BaseReq baseReq) {
//
//    }
//
//    @Override
//    public void onResp(BaseResp resp) {
//
//        String code = ((SendAuth.Resp) resp).code;
//        Log.e("*************","%%%%%%"+code);
//        new WeiXinLogin(context).getToken(code);
//    }
}
