package fragment;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import view.GuideVideoView;

/**
 * Created by A on 2016/10/24.
 */
public class BaseGuideFragment extends Fragment {
    GuideVideoView guideVideoView;
    public ViewPager viewPager;
    public int pauseValue=0;//暂停的视频位置
    public String duration="0";//视频长度

    public void videoPlay(){
    }

    public void setVideoPlayInitialization(Uri uri , final int page){
        if(guideVideoView!=null){
            guideVideoView.playVideo(uri);


            //获取视频长度
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(getActivity(),uri);
            duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);


            guideVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if(page!=4)
                    viewPager.setCurrentItem(page);
                }
            });
        }
    }
    public void videoReplay(){
        if(pauseValue<Integer.parseInt(duration)&&pauseValue>0){
            guideVideoView.seekTo(pauseValue);
            guideVideoView.start();
        }else{
            videoPlay();
        }
    }

    public void videoPause(){
        if (guideVideoView!=null){
            guideVideoView.pause();
            pauseValue=guideVideoView.getCurrentPosition();
        }
    }

    public void videoStop(){
        if(guideVideoView!=null){
            guideVideoView.stopPlayback();
        }
    }

    public void setViewPager(ViewPager viewPager){
        this.viewPager=viewPager;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(getUserVisibleHint()){
            videoReplay();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        videoPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoStop();
    }

}
