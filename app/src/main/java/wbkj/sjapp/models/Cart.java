package wbkj.sjapp.models;

import java.io.Serializable;

/**
 * Created by jianghan on 16/8/1.
 */
public class Cart implements Serializable {

    public int scid;
    public int userid;
    public int num;
    public Product wdetails;

    @Override
    public String toString() {
        return "Cart{" +
                "wdetails=" + wdetails +
                ", num=" + num +
                ", userid=" + userid +
                ", scid=" + scid +
                '}';
    }
}
