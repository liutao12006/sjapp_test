package wbkj.sjapp.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class ShopDataBean implements Serializable {
    private  String ID;
    private List<Channel> channels;
    private  String name;

    public ShopDataBean(){}

    public ShopDataBean(List<Channel> channels, String ID, String name) {
        this.channels = channels;
        this.ID = ID;
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public void setName(String name) {
        this.name = name;
    }
}
