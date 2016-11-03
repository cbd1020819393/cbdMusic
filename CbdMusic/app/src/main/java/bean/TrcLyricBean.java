package bean;

import java.util.ArrayList;

/**
 * Created by A on 2016/11/3.
 */
public class TrcLyricBean {
    private String ti;//歌名
    private String ar;//歌手
    private String al;//专辑
    private String ly;//作词人
    private String mu;//作曲
    private String ma;//编曲
    private String pu;
    private String by;//官方，貌似都是[by:ttpod]，也可能也是作词人的意思
    private String total;//长度
    private String offset;//偏移
    private  ArrayList<TycLyricLeanBean> leanArrayList;
    public String getAl() {
        return al;
    }

    public void setAl(String al) {
        this.al = al;
    }

    public String getAr() {
        return ar;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public ArrayList<TycLyricLeanBean> getLeanArrayList() {
        return leanArrayList;
    }

    public void setLeanArrayList(ArrayList<TycLyricLeanBean> leanArrayList) {
        this.leanArrayList = leanArrayList;
    }

    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getMu() {
        return mu;
    }

    public void setMu(String mu) {
        this.mu = mu;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getPu() {
        return pu;
    }

    public void setPu(String pu) {
        this.pu = pu;
    }

    public String getTi() {
        return ti;
    }

    public void setTi(String ti) {
        this.ti = ti;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }






}
