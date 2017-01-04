package wbkj.sjapp.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/2.
 */

public class Channel implements Serializable {

    private String coll;
    private String dmlb;
    private String dmlx;
    private String dmsm;
    private String dmz;
    private String img;
    private String imgstore;
    private String state;
    private double oidprice;
    private double wdprice;
    private int wauthor;
    private int wid;
    private String wname;

    public Channel() {
    }

    public Channel(String dmlb, String dmlx, String dmsm, String img, String dmz, String imgstore, int wauthor, String state, int wid, String wname) {
        this.dmlb = dmlb;
        this.dmlx = dmlx;
        this.dmsm = dmsm;
        this.img = img;
        this.dmz = dmz;
        this.imgstore = imgstore;
        this.wauthor = wauthor;
        this.state = state;
        this.wid = wid;
        this.wname = wname;
    }

    public Channel(String dmlb, String coll, String dmsm, String dmlx, String dmz, String img, String state, String imgstore, double oidprice, double wdprice, int wauthor, int wid, String wname) {
        this.dmlb = dmlb;
        this.coll = coll;
        this.dmsm = dmsm;
        this.dmlx = dmlx;
        this.dmz = dmz;
        this.img = img;
        this.state = state;
        this.imgstore = imgstore;
        this.oidprice = oidprice;
        this.wdprice = wdprice;
        this.wauthor = wauthor;
        this.wid = wid;
        this.wname = wname;
    }

    public void setColl(String coll) {
        this.coll = coll;
    }

    public void setOidprice(double oidprice) {
        this.oidprice = oidprice;
    }

    public void setWdprice(double wdprice) {
        this.wdprice = wdprice;
    }

    public double getOidprice() {
        return oidprice;
    }

    public double getWdprice() {
        return wdprice;
    }

    public String getColl() {
        return coll;
    }

    public String getDmlb() {
        return dmlb;
    }

    public String getDmlx() {
        return dmlx;
    }

    public String getDmsm() {
        return dmsm;
    }

    public String getDmz() {
        return dmz;
    }

    public String getImg() {
        return img;
    }

    public String getImgstore() {
        return imgstore;
    }

    public String getState() {
        return state;
    }

    public int getWauthor() {
        return wauthor;
    }

    public int getWid() {
        return wid;
    }

    public String getWname() {
        return wname;
    }

    public void setDmlb(String dmlb) {
        this.dmlb = dmlb;
    }

    public void setDmlx(String dmlx) {
        this.dmlx = dmlx;
    }

    public void setDmsm(String dmsm) {
        this.dmsm = dmsm;
    }

    public void setDmz(String dmz) {
        this.dmz = dmz;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setImgstore(String imgstore) {
        this.imgstore = imgstore;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setWauthor(int wauthor) {
        this.wauthor = wauthor;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }
}
