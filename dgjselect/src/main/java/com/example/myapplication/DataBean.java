package com.example.myapplication;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/24.
 */

public class DataBean implements Serializable {

//    public String oid;
//    public String state;
    public String col2;
//    public String col3;

//    public String odate;
//    public String total;
    public Ware ware;

    public class Ware implements Serializable {
        public String wname;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "col2='" + col2 + '\'' +
                ", ware=" + ware +
                '}';
    }
}
