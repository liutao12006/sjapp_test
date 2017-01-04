package wbkj.sjapp.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jianghan on 16/7/28.
 */
public class Message implements Serializable {

    private int tid;
    private String tdcontent;
    private String timg;
    private Msg title;
    private User user;
    private List<Comment> commentList;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String img6;
    private String img7;


    public class Msg implements Serializable {
        private String tname;
        private String tdate;

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getTdate() {
            return tdate;
        }

        public void setTdate(String tdate) {
            this.tdate = tdate;
        }
    }

    public class Comment implements Serializable {
        private int cmid;
        private String cmcontent;
        private String cmdate;
        private User vipuser;

        public int getCmid() {
            return cmid;
        }

        public void setCmid(int cmid) {
            this.cmid = cmid;
        }

        public String getCmcontent() {
            return cmcontent;
        }

        public void setCmcontent(String cmcontent) {
            this.cmcontent = cmcontent;
        }

        public String getCmdate() {
            return cmdate;
        }

        public void setCmdate(String cmdate) {
            this.cmdate = cmdate;
        }

        public User getVipuser() {
            return vipuser;
        }

        public void setVipuser(User vipuser) {
            this.vipuser = vipuser;
        }
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTdcontent() {
        return tdcontent;
    }

    public void setTdcontent(String tdcontent) {
        this.tdcontent = tdcontent;
    }

    public String getTimg() {
        return timg;
    }

    public void setTimg(String timg) {
        this.timg = timg;
    }

    public Msg getTitle() {
        return title;
    }

    public void setTitle(Msg title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    public String getImg6() {
        return img6;
    }

    public void setImg6(String img6) {
        this.img6 = img6;
    }

    public String getImg7() {
        return img7;
    }

    public void setImg7(String img7) {
        this.img7 = img7;
    }

    @Override
    public String toString() {
        return "Message{" +
                "tid=" + tid +
                ", tdcontent='" + tdcontent + '\'' +
                ", timg='" + timg + '\'' +
                ", title=" + title +
                ", user=" + user +
                ", commentList=" + commentList +
                '}';
    }
}
