package wbkj.sjapp.models;

import java.io.Serializable;

/**
 * Created by jianghan on 16/7/22.
 */
public class User implements Serializable {

    private int vipid;
    private String username;
    private String password;
    private String nickname;
    private int sex;        //0男, 1女
    private int intergration;       //积分
    private String img;
    private String phone;
    private String thirdaccount;
    private int grade;      //等级
    private int thirdfrom;      //1微信   2qq     3平台


    public int getVipid() {
        return vipid;
    }

    public void setVipid(int vipid) {
        this.vipid = vipid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIntergration() {
        return intergration;
    }

    public void setIntergration(int intergration) {
        this.intergration = intergration;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getThirdaccount() {
        return thirdaccount;
    }

    public void setThirdaccount(String thirdaccount) {
        this.thirdaccount = thirdaccount;
    }

    public int getThirdfrom() {
        return thirdfrom;
    }

    public void setThirdfrom(int thirdfrom) {
        this.thirdfrom = thirdfrom;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
