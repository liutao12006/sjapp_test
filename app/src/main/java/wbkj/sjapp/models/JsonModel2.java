package wbkj.sjapp.models;

import java.util.List;

/**
 * Created by jianghan on 16/7/23.
 */
public class JsonModel2<T> {

    public int result;
    public List<T> data;
    public String msg;
    public List<T> otherdata;
    public List<T> shopdata;
    public List<ProType> syscodes;

    @Override
    public String toString() {
        return "JsonModel2{" +
                "result=" + result +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
