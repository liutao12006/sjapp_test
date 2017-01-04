package wbkj.sjapp.models;

/**
 * Created by jianghan on 16/8/4.
 */
public class Notice {

    private int cdid;
    private String cicontent;
    private Data circular;

    public class Data{
        private String cititle;
        private String cidate;

        public String getCititle() {
            return cititle;
        }

        public String getCidate() {
            return cidate;
        }
    }

    public int getCdid() {
        return cdid;
    }

    public String getCicontent() {
        return cicontent;
    }

    public Data getCircular() {
        return circular;
    }
}
