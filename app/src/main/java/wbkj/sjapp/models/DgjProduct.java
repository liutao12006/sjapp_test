package wbkj.sjapp.models;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/22.
 */

public class DgjProduct implements Serializable {
    private int wdid;
    private int wid;
    private double wdprice;
    private double oidprice;
    private String col1;        //简介
    private String col2;        //备注
    private String col3;        //服务时间
    private int col4;        //收费类型      1,/次 2,/小时 3,/积分 4,/元
    private String col7;

    private ProDital ware;

    public void setWdid(int wdid) {
        this.wdid = wdid;
    }

    public void setWdprice(double wdprice) {
        this.wdprice = wdprice;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public void setCol7(String col7) {
        this.col7 = col7;
    }

    public void setWare(ProDital ware) {
        this.ware = ware;
    }

    public void setCol4(int col4) {
        this.col4 = col4;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public void setOidprice(double oidprice) {
        this.oidprice = oidprice;
    }

    public int getWid() {
        return wid;
    }

    public double getWdprice() {
        return wdprice;
    }

    public double getOidprice() {
        return oidprice;
    }

    public String getCol1() {
        return col1;
    }

    public String getCol2() {
        return col2;
    }

    public String getCol3() {
        return col3;
    }

    public int getCol4() {
        return col4;
    }

    public String getCol7() {
        return col7;
    }

    public ProDital getWare() {
        return ware;
    }

    public class ProDital implements Serializable {
        private int wid;
        private String wname;
        private String img;
        private int laud;           //销量
        private String wauthor;
        private int shopid;
        private String imgstore;

        public String getImgstore() {
            return imgstore;
        }

        public int getShopid() {
            return shopid;
        }

        public String getWauthor() {
            return wauthor;
        }

        public int getLaud() {
            return laud;
        }

        public String getImg() {
            return img;
        }

        public String getWname() {
            return wname;
        }

        public int getWid() {
            return wid;
        }

        public void setWid(int wid) {
            this.wid = wid;
        }

        public void setWname(String wname) {
            this.wname = wname;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setLaud(int laud) {
            this.laud = laud;
        }

        public void setWauthor(String wauthor) {
            this.wauthor = wauthor;
        }

        public void setShopid(int shopid) {
            this.shopid = shopid;
        }

        public void setImgstore(String imgstore) {
            this.imgstore = imgstore;
        }
    }

}
