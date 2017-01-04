package wbkj.sjapp.models;

import java.io.Serializable;

/**
 * Created by jianghan on 16/9/10.
 */
public class Shop implements Serializable {

    private int id;
    private String name;
    private String phone;
    private String address;
    private String locationX;
    private String locationY;
    private String date;
    private String state;
    private String type;
    private String introduce;
    private String img;
    private String distance;
    private double avg;
    private int commentsize;
    private String flag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public int getCommentsize() {
        return commentsize;
    }

    public void setCommentsize(int commentsize) {
        this.commentsize = commentsize;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", locationX='" + locationX + '\'' +
                ", locationY='" + locationY + '\'' +
                ", date='" + date + '\'' +
                ", introduce='" + introduce + '\'' +
                ", img='" + img + '\'' +
                ", avg=" + avg +
                ", commentsize=" + commentsize +
                '}';
    }
}
