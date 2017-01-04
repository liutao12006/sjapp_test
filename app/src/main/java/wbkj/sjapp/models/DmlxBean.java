package wbkj.sjapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class DmlxBean implements Serializable {
    private Dmlx mDmlx = new Dmlx();

    public DmlxBean(){}

    public DmlxBean(Dmlx mDmlx) {
        this.mDmlx = mDmlx;
    }

    public Dmlx getmDmlx() {
        return mDmlx;
    }

    public void setmDmlx(Dmlx mDmlx) {
        this.mDmlx = mDmlx;
    }
}
