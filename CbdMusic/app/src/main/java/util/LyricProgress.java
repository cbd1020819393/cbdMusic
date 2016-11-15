package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bean.LyricContent;
import bean.TrcLyricBean;
import bean.TycLyricLeanBean;
import bean.TycLyricWordBean;

public class LyricProgress {
  
    private List<LyricContent> lyricList;
    private LyricContent myLyricContent;  
    private int currentTrcLine=1;//解析trc歌词的当前行
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

    public TrcLyricBean readTrc(String path){                        //从文件中读出歌词并解析的函数
        File f = new File(path);
        TrcLyricBean trcLyricBean=new TrcLyricBean();
        ArrayList<TycLyricLeanBean> trcLyricLeanList=new ArrayList<>();
        try{
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String s= "";

            while((s = br.readLine()) != null){
                //取值的正则
                String patternRegular ="(?<=\\:)\\S*(?=\\])";
                // 创建 Pattern对象
                Pattern pattern = Pattern.compile(patternRegular);
                Matcher matcher;
                switch (currentTrcLine){
                    case 1:
                        matcher = pattern.matcher(s);
                        while (matcher.find()){
                            trcLyricBean.setTi(matcher.group(0));
                        }
                        currentTrcLine++;
                        break;
                    case 2:
                        matcher = pattern.matcher(s);
                        while (matcher.find()){
                            trcLyricBean.setAr(matcher.group(0));
                        }
                        currentTrcLine++;
                        break;
                    case 3:
                        matcher = pattern.matcher(s);
                        while (matcher.find()){
                            trcLyricBean.setAl(matcher.group(0));
                        }
                        currentTrcLine++;
                        break;
                    case 4:
                        matcher = pattern.matcher(s);
                        while (matcher.find()){
                            trcLyricBean.setLy(matcher.group(0));
                        }
                        currentTrcLine++;
                        break;
                    case 5:
                        matcher = pattern.matcher(s);
                        while (matcher.find()){
                           trcLyricBean.setMu(matcher.group(0));
                        }
                        currentTrcLine++;
                        break;
                    case 6:
                        matcher = pattern.matcher(s);
                        while (matcher.find()){
                            trcLyricBean.setMa(matcher.group(0));
                        }
                        currentTrcLine++;
                        break;
                    case 7:
                        matcher = pattern.matcher(s);
                        while (matcher.find()){
                            trcLyricBean.setPu(matcher.group(0));
                        }
                        currentTrcLine++;
                        break;
                    case 8:
                        matcher = pattern.matcher(s);
                        while (matcher.find()){
                            trcLyricBean.setBy(matcher.group(0));
                        }
                        currentTrcLine++;
                        break;
                    case 9:
                        matcher = pattern.matcher(s);
                        while (matcher.find()){
                            trcLyricBean.setTotal(matcher.group(0));
                        }
                        currentTrcLine++;
                        break;
                    case 10:
                        matcher = pattern.matcher(s);
                        while (matcher.find()){
                            trcLyricBean.setOffset(matcher.group(0));
                        }
                        currentTrcLine++;
                        break;
                    default:
                        TycLyricLeanBean tycLyricLeanBean=new TycLyricLeanBean();
                        ArrayList<TycLyricWordBean>arrayList=new ArrayList<>();
                        // 按指定模式在字符串查找
                        //取时间的正则
                        String patternTime ="(?<=\\[)\\S*(?=\\])";
                        //取每个字时间的正则
                        String patternWordTime="(?<=<)\\d*(?=>)";
                        //取每个字内容的正则
                        String patternWord="(?<=>)\\S";
                        // 创建 Pattern对象
                        Pattern paternTime = Pattern.compile(patternTime);
                        Pattern paternWordTime = Pattern.compile(patternWordTime);
                        Pattern paternWord = Pattern.compile(patternWord);
                        // 现在创建 matcher 对象
                        Matcher matcherTime = paternTime.matcher(s);
                        Matcher matcherWordTime = paternWordTime.matcher(s);
                        Matcher matcherWord = paternWord.matcher(s);
                        //把每句的开始时间放入tycLyricLeanBean中
                        while(matcherTime.find())
                        {
                            tycLyricLeanBean.setLineTime(time2Str(matcherTime.group(0))+"");
                        }
                        //保存每个字的需要的时间
                        while(matcherWordTime.find())
                        {
                            TycLyricWordBean tycLyricWordBean=new TycLyricWordBean();
                            tycLyricWordBean.setWordTime(matcherWordTime.group(0));
                            arrayList.add(tycLyricWordBean);
                        }
                        //保存每个字
                        int position=0;
                        while(matcherWord.find())
                        {
                            arrayList.get(position).setWordString(matcherWord.group(0));
                            position++;
                        }
                        //把整句话放入句的bean中
                        StringBuilder stringBuilder=new StringBuilder();
                        for(int i=0;i<arrayList.size();i++){
                            stringBuilder.append(arrayList.get(i).getWordString());
                        }
                        tycLyricLeanBean.setLine(stringBuilder.toString());
                        //把字的list放入句中
                        tycLyricLeanBean.setWordArrayList(arrayList);
                        //把当前句子放入list中
                        trcLyricLeanList.add(tycLyricLeanBean);

                        break;
                }
            }
            //把句的list放入歌词体（trcLyricBean）中
            trcLyricBean.setLeanArrayList(trcLyricLeanList);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
//            stringBuilder.append("一丁点儿歌词都没找到,下载后再来找我把.......");
        }
        catch(IOException e){
            e.printStackTrace();
//            stringBuilder.append("没有读取到歌词.....");
        }
        return trcLyricBean;
    }

    public int time2Str(String timeStr){                 //将分:秒:毫秒转化为长整型的数  
        timeStr = timeStr.replace(":",".");  
        timeStr = timeStr.replace(".","@");  
  
        String timeData[] = timeStr.split("@");  
  
        int min = Integer.parseInt(timeData[0]);  
        int sec = Integer.parseInt(timeData[1]);  
        int millSec = Integer.parseInt(timeData[2]);  
  
        int currentTime = (min * 60 + sec) * 1000 + millSec;
        return currentTime;  
    }  
  
    public List<LyricContent> getLyricList(){  
        return this.lyricList;  
    }  
}  