package wbkj.sjapp.models;

import java.io.Serializable;

/**
 * Created by jianghan on 16/8/3.
 */
public class Address implements Serializable {

    private int udid;
    private String plotnickname;
    private String province;
    private String city;
    private String county;
    private String address;
    private String plot;
    private String household;   //户主
    private String tel;         //联系电话
    private String houserarea;  //面积
    private double waterfee;    //水费
    private double powerfee;    //电费
    private double propertyfee; //物业费

    private String expirationtime;  //到期时间
    private int isdefult;           //1默认

    public int getUdid() {
        return udid;
    }

    public void setUdid(int udid) {
        this.udid = udid;
    }

    public String getPlotnickname() {
        return plotnickname;
    }

    public void setPlotnickname(String plotnickname) {
        this.plotnickname = plotnickname;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHousehold() {
        return household;
    }

    public void setHousehold(String household) {
        this.household = household;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHouserarea() {
        return houserarea;
    }

    public void setHouserarea(String houserarea) {
        this.houserarea = houserarea;
    }

    public double getWaterfee() {
        return waterfee;
    }

    public void setWaterfee(double waterfee) {
        this.waterfee = waterfee;
    }

    public double getPowerfee() {
        return powerfee;
    }

    public void setPowerfee(double powerfee) {
        this.powerfee = powerfee;
    }

    public double getPropertyfee() {
        return propertyfee;
    }

    public void setPropertyfee(double propertyfee) {
        this.propertyfee = propertyfee;
    }


    public String getExpirationtime() {
        return expirationtime;
    }

    public void setExpirationtime(String expirationtime) {
        this.expirationtime = expirationtime;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public int getIsdefult() {
        return isdefult;
    }

    public void setIsdefult(int isdefult) {
        this.isdefult = isdefult;
    }
}
