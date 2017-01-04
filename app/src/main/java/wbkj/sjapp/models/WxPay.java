package wbkj.sjapp.models;

/**
 * Created by Zz on 2016/4/14 0014.
 */
public class WxPay {

    public String prepay_id;
    public String nonce_str;
    public String timeStamp;
    public String sign;

    @Override
    public String toString() {
        return "WxPay{" +
                ", prepayId='" + prepay_id + '\'' +
                ", nonceStr='" + nonce_str + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
