package wbkj.sjapp.models;

import java.io.Serializable;

/**
 * Created by jianghan on 16/8/10.
 */
public class Orders implements Serializable {

    private String oid;
    private int wdid;
    private int tote;       //商品数量
    private double total;   //订单价格
    private int userid;
    private String odate;   //下单时间
    private String bdate;   //结算时间
    private int paytype;    //1支付宝,2微信
    private int state;      //1新建(未支付)    2已支付    3已完成    4已删除     5已评论
    private int col1;       //服务地址id,
    private String col5;    //备注
    private int cid;        //

    private Product wdetails;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getWdid() {
        return wdid;
    }

    public void setWdid(int wdid) {
        this.wdid = wdid;
    }

    public int getTote() {
        return tote;
    }

    public void setTote(int tote) {
        this.tote = tote;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getOdate() {
        return odate;
    }

    public void setOdate(String odate) {
        this.odate = odate;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCol1() {
        return col1;
    }

    public void setCol1(int col1) {
        this.col1 = col1;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public Product getWdetails() {
        return wdetails;
    }

    public void setWdetails(Product wdetails) {
        this.wdetails = wdetails;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "oid=" + oid +
                ", wdid=" + wdid +
                ", tote=" + tote +
                ", total=" + total +
                ", userid=" + userid +
                ", odate='" + odate + '\'' +
                ", bdate='" + bdate + '\'' +
                ", paytype=" + paytype +
                ", state=" + state +
                ", col1=" + col1 +
                ", col5='" + col5 + '\'' +
                '}';
    }
}
