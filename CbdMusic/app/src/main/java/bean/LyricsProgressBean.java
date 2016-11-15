package bean;

import java.util.ArrayList;

/**
 * Created by A on 2016/11/11.
 */
public class LyricsProgressBean {
    public ArrayList<LyricsProgressDetailsBean> getList() {
        return list;
    }

    public void setList(ArrayList<LyricsProgressDetailsBean> list) {
        this.list = list;
    }

    private ArrayList<LyricsProgressDetailsBean> list;
}
