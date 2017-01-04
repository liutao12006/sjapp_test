package wbkj.sjapp.models;

/**
 * Created by jianghan on 16/7/23.
 */
public class ProType {

    private int scid;
    private int dmz;
    private String dmlb;
    private String dmlx;
    private String dmsm;        //类型名字

    public int getScid() {
        return scid;
    }

    public void setScid(int scid) {
        this.scid = scid;
    }

    public String getDmlb() {
        return dmlb;
    }

    public void setDmlb(String dmlb) {
        this.dmlb = dmlb;
    }

    public String getDmlx() {
        return dmlx;
    }

    public void setDmlx(String dmlx) {
        this.dmlx = dmlx;
    }

    public int getDmz() {
        return dmz;
    }

    public void setDmz(int dmz) {
        this.dmz = dmz;
    }

    public String getDmsm() {
        return dmsm;
    }

    public void setDmsm(String dmsm) {
        this.dmsm = dmsm;
    }
}
