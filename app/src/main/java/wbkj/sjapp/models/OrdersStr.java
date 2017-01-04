package wbkj.sjapp.models;

/**
 * Created by jianghan on 16/8/17.
 */
public class OrdersStr {

    private String oid;
    private String wdid;
    private String tote;       //商品数量
    private String total;   //订单价格
    private String userid;
    private String odate;   //下单时间
    private String bdate;   //结算时间
    private String paytype;    //1支付宝,2微信
    private String state;      //1新建(未支付)    2已支付    3已完成    4已删除
    private String col1;       //服务地址id,
    private String col5;    //备注
    private String cid;        //
    private String integral;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getWdid() {
        return wdid;
    }

    public void setWdid(String wdid) {
        this.wdid = wdid;
    }

    public String getTote() {
        return tote;
    }

    public void setTote(String tote) {
        this.tote = tote;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
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

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}
