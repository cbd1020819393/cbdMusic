package util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.Log;

import java.util.ArrayList;

import bean.LyricsProgressBean;
import bean.LyricsProgressDetailsBean;
import bean.TrcLyricBean;
import bean.TycLyricLeanBean;
import view.KrcView;

/**
 * Created by A on 2016/11/11.
 */
public class LyricsProgressUtil {
    public LyricsProgressUtil(KrcView krcView) {
        this.krcView = krcView;
    }

    private KrcView krcView;
    private float f1 = 0.0f;
    private float f2 = 0.0f;
    private long total = 0;
    //下一行的起始时间
    private long nextStartTime;
    //当前行时间
    private long currentStartTime;
    //等待时间
    private long waitTime;

    private ArrayList<LyricsProgressDetailsBean> detailList=new ArrayList<>();

    public  LyricsProgressBean calculation(TrcLyricBean trcLyricBean){

        for(int i=0;i<trcLyricBean.getLeanArrayList().size();i++){
            //保存一句歌词的动画list
            ArrayList<Animator> list = new ArrayList<>();
            //初始化数据
            total=0;
            f1=0.0f;
            f2=0.0f;
            nextStartTime=0;
            currentStartTime=0;
            list.clear();
            //遍历当句的每个字
            TycLyricLeanBean tycLyricLeanBean=trcLyricBean.getLeanArrayList().get(i);
            for (int j = 0; j < tycLyricLeanBean.getWordArrayList().size(); j++) {
                //获得每个字的float值
                f2 = (j + 1) * 1f / tycLyricLeanBean.getWordArrayList().size();
                Log.i("test", "changeLine: " + f1 + ";" + f2);
                long time = Long.parseLong(tycLyricLeanBean.getWordArrayList().get(j).getWordTime());
                total += time;
                ObjectAnimator ob = ObjectAnimator.ofFloat(krcView, "float1", f1, f2).setDuration(time);
                f1 = f2;
                list.add(ob);
            }

            //不是最后一行
            if (i != trcLyricBean.getLeanArrayList().size() - 1) {
                //下一行的起始时间
                nextStartTime = Long.parseLong(trcLyricBean.getLeanArrayList().get(i + 1).getLineTime());
                //当前行时间
                currentStartTime = Long.parseLong(trcLyricBean.getLeanArrayList().get(i).getLineTime());
                //得出等待时间
                waitTime=nextStartTime-currentStartTime-total;
            }

            //放入detailList中
            LyricsProgressDetailsBean bean=new LyricsProgressDetailsBean();
            bean.setWaitTime(waitTime);
            bean.setAnimatorArrayList(list);
            detailList.add(bean);

            //是最后一行
            if(i==trcLyricBean.getLeanArrayList().size() - 1){
                LyricsProgressBean progressBean=new LyricsProgressBean();
                progressBean.setList(detailList);
                return progressBean;
            }
        }

        return null;
    }
}
