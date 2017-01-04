package wbkj.sjapp.models;

/**
 * Created by jianghan on 16/9/12.
 */
public class Comment {

    private int cmid;
    private String cmdate;
    private int wid;
    private String cmcontent;
    private double selevel;     //服务评分
    private double lolevel;     //体验评分
    private double adlevel;     //商品评分
    private User vipuser;

    public int getCmid() {
        return cmid;
    }

    public void setCmid(int cmid) {
        this.cmid = cmid;
    }

    public String getCmdate() {
        return cmdate;
    }

    public void setCmdate(String cmdate) {
        this.cmdate = cmdate;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getCmcontent() {
        return cmcontent;
    }

    public void setCmcontent(String cmcontent) {
        this.cmcontent = cmcontent;
    }

    public double getSelevel() {
        return selevel;
    }

    public void setSelevel(double selevel) {
        this.selevel = selevel;
    }

    public double getLolevel() {
        return lolevel;
    }

    public void setLolevel(double lolevel) {
        this.lolevel = lolevel;
    }

    public double getAdlevel() {
        return adlevel;
    }

    public void setAdlevel(double adlevel) {
        this.adlevel = adlevel;
    }

    public User getVipuser() {
        return vipuser;
    }

    public void setVipuser(User vipuser) {
        this.vipuser = vipuser;
    }
}
