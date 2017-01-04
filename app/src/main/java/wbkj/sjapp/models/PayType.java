package wbkj.sjapp.models;

public class PayType {

    private String name;
    private String summary;
    private int iconResId;

    public String getName() {
        return name;
    }

    public PayType(String name, String summary, int iconResId) {
        super();
        this.name = name;
        this.summary = summary;
        this.iconResId = iconResId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }


}
