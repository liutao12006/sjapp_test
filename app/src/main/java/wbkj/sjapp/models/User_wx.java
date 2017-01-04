package wbkj.sjapp.models;

import java.io.Serializable;

/**
 * Created by jianghan on 16/8/16.
 */
public class User_wx implements Serializable {

    private String openid;
    private String nickname;
    private int sex;
//    private String city;
//    private String province;
    private String headimgurl;
    private String unionid;

    public String getOpenid() {
        return openid;
    }

    public String getNickname() {
        return nickname;
    }

    public int getSex() {
        return sex;
    }

//    public String getCity() {
//        return city;
//    }
//
//    public String getProvince() {
//        return province;
//    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    @Override
//    ", city='" + city + '\'' +
//            ", province='" + province + '\'' +
    public String toString() {
        return "User_wx{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
