package wbkj.sjapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public  class Dmlx  implements Serializable {
    public List<Channel_groups> channel_groups;

    public Dmlx(){

    }

    public List getChannel_groups() {
        return channel_groups;
    }

    public void setChannel_groups(List channel_groups) {
        this.channel_groups = channel_groups;
    }
}
