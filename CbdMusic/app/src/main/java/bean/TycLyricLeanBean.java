package bean;

import java.util.ArrayList;

public class TycLyricLeanBean {
    private String lineTime;//当句开始时间
    private String line;//当句歌词
    private ArrayList<TycLyricWordBean> wordArrayList;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public ArrayList<TycLyricWordBean> getWordArrayList() {
        return wordArrayList;
    }

    public void setWordArrayList(ArrayList<TycLyricWordBean> wordArrayList) {
        this.wordArrayList = wordArrayList;
    }

    public String getLineTime() {
        return lineTime;
    }

    public void setLineTime(String lineTime) {
        this.lineTime = lineTime;
    }


}