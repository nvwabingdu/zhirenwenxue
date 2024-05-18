package cn.dreamfruits.yaoguo.module.main.home.commentreplay;

/**
 * @Author qiwangi
 * @Date 2023/4/20
 * @TIME 10:50
 */
public class DiffTextBean {

    private int index;
    private String indexStr;
    private boolean isAdd;

    @Override
    public String toString() {
        return "==="+index +"" + indexStr + "" + isAdd;
    }

    public DiffTextBean(int index, String indexStr, boolean isAdd) {
        this.index = index;
        this.indexStr = indexStr;
        this.isAdd = isAdd;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIndexStr() {
        return indexStr;
    }

    public void setIndexStr(String indexStr) {
        this.indexStr = indexStr;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

}
