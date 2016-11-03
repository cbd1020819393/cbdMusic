package bean;

public class LyricContent {
    private String lyricString;            //歌词的内容  
    private int lyricTime;                 //歌词当前的时间  
  
    public String getLyricString(){  
        return this.lyricString;  
    }  
  
    public void setLyricString(String str){  
        this.lyricString = str;  
    }  
  
    public int getLyricTime(){  
        return this.lyricTime;  
    }  
  
    public void setLyricTime(int time){  
        this.lyricTime = time;  
    }  
}  