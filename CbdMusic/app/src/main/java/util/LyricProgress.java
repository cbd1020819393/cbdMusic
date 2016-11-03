package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import bean.LyricContent;
import bean.TrcLyricBean;
import bean.TycLyricLeanBean;

public class LyricProgress {
  
    private List<LyricContent> lyricList;
    private LyricContent myLyricContent;  
    private int trcLine=1;
    public LyricProgress(){  
        myLyricContent = new LyricContent();  
        lyricList = new ArrayList<LyricContent>();
    }  
  
    public String readLyric(String path){                        //从文件中读出歌词并解析的函数  
        StringBuilder stringBuilder = new StringBuilder();  
//        path = path.replace("song","lyric");                     //这个是针对天天动听的目录结构下手的,,,不知道有没有什么适合所有文件结构的方法呢..
        File f = new File(path);
  
        try{  
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String s= "";  
  
            while((s = br.readLine()) != null){  
                s = s.replace("[","");  
                s = s.replace("]","@");                                 //每一句话的分隔符  
  
                s = s.replaceAll("<[0-9]{3,5}>","");                    //去掉每个字的时间标签,这里用到了正则表达式  

  
                String spiltLrcData[] = s.split("@");  
  
                if(spiltLrcData.length > 1){  
  
                    myLyricContent.setLyricString(spiltLrcData[1]);     //将每句话创建一个类的实例,歌词和对应时间赋值  
                    int lycTime = time2Str(spiltLrcData[0]);  
                    myLyricContent.setLyricTime(lycTime);  
                    lyricList.add(myLyricContent);  
  
                    myLyricContent = new LyricContent();  
                }  
            }  
        }  
        catch(FileNotFoundException e){
            e.printStackTrace();  
            stringBuilder.append("一丁点儿歌词都没找到,下载后再来找我把.......");  
        }  
        catch(IOException e){
            e.printStackTrace();  
            stringBuilder.append("没有读取到歌词.....");  
        }  
        return stringBuilder.toString();  
    }

    public String readTrc(String path){                        //从文件中读出歌词并解析的函数
        StringBuilder stringBuilder = new StringBuilder();
        File f = new File(path);
        TrcLyricBean trcLyricBean=new TrcLyricBean();
        try{
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String s= "";

            while((s = br.readLine()) != null){
                s = s.replace("[","");
                s = s.replace("]","@");//每一句话的分隔符
                switch (trcLine){
                    case 1:
                        trcLyricBean.setTi(s);
                        break;
                    case 2:
                        trcLyricBean.setAr(s);
                        break;
                    case 3:
                        trcLyricBean.setAl(s);
                        break;
                    case 4:
                        trcLyricBean.setLy(s);
                        break;
                    case 5:
                        trcLyricBean.setMu(s);
                        break;
                    case 6:
                        trcLyricBean.setMa(s);
                        break;
                    case 7:
                        trcLyricBean.setPu(s);
                        break;
                    case 8:
                        trcLyricBean.setBy(s);
                        break;
                    case 9:
                        trcLyricBean.setTotal(s);
                        break;
                    case 10:
                        trcLyricBean.setOffset(s);
                        break;
                    default:
                        TycLyricLeanBean tycLyricLeanBean=new TycLyricLeanBean();

                        String spiltLrcData[] = s.split("@");

                        if(spiltLrcData.length > 1){

                            myLyricContent.setLyricString(spiltLrcData[1]);     //将每句话创建一个类的实例,歌词和对应时间赋值
                            int lycTime = time2Str(spiltLrcData[0]);
                            myLyricContent.setLyricTime(lycTime);
                            lyricList.add(myLyricContent);
                            trcLine++;
                            myLyricContent = new LyricContent();
                        }
                        break;
                }
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            stringBuilder.append("一丁点儿歌词都没找到,下载后再来找我把.......");
        }
        catch(IOException e){
            e.printStackTrace();
            stringBuilder.append("没有读取到歌词.....");
        }
        return stringBuilder.toString();
    }

    public int time2Str(String timeStr){                 //将分:秒:毫秒转化为长整型的数  
        timeStr = timeStr.replace(":",".");  
        timeStr = timeStr.replace(".","@");  
  
        String timeData[] = timeStr.split("@");  
  
        int min = Integer.parseInt(timeData[0]);  
        int sec = Integer.parseInt(timeData[1]);  
        int millSec = Integer.parseInt(timeData[2]);  
  
        int currentTime = (min * 60 + sec) * 1000 + millSec * 10;  
        return currentTime;  
    }  
  
    public List<LyricContent> getLyricList(){  
        return this.lyricList;  
    }  
}  