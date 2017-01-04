package wbkj.sjapp.models;

import java.io.Serializable;

/**
 * Created by jianghan on 16/8/17.
 */
public class User_qq implements Serializable{

    private String nickname;
    private String figureurl_qq_2;
    private String gender;

    public String getNickname() {
        return nickname;
    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "User_qq{" +
                "nickname='" + nickname + '\'' +
                ", figureurl_qq_2='" + figureurl_qq_2 + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
