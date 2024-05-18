package cn.dreamfruits.yaoguo.module.main.home.commentreplay;

/**
 * @Author qiwangi
 * @Date 2023/4/20
 * @TIME 10:31
 */
public class AtTextBean {
    private String singleStr;
    private int index;
    private int len;
    private Long id;
    private String name;

    @Override
    public String toString() {
        return "AtTextBean=>|" + singleStr +"|"+ index +"|"+ len +"|"+ id +"|"+ name;
    }

    public AtTextBean(String singleStr, int index, int len, Long id, String name) {
        this.singleStr = singleStr;
        this.index = index;
        this.len = len;
        this.id = id;
        this.name = name;
    }

    public String getSingleStr() {
        return singleStr;
    }

    public void setSingleStr(String singleStr) {
        this.singleStr = singleStr;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
