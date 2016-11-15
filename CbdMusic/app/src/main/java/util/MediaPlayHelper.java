package util;

import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by A on 2016/11/2.
 */
public class MediaPlayHelper implements  AudioManager.OnAudioFocusChangeListener{
    private MediaPlayer mediaPlayer;
    private Context context;
    public MediaPlayHelper(Context context) {
        this.context = context;
    }
    //创建MediaPlayer
    public void create(){
        mediaPlayer=new MediaPlayer();
    }
    //播放资源文件中的音乐就不需要调用create()方法了
    public void playMusicForRes(int soundId){
         mediaPlayer= MediaPlayer.create(context,soundId);
         start();
    }

    //播放手机本地音乐
    public  void playMusicForLocalStorage(long id) throws IOException {
        Uri contentUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(context, contentUri);
        start();
    }

    //播放手机本地音乐
    public  void playMusicForLocalStorage(Uri uri) throws IOException {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(context, uri);
        start();
    }

    public void playMusicForNetwork(String url) throws IOException {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
    }

    public void start(){
        mediaPlayer.start();
    }
    //释放mediaPlayer资源
    public void stop(){
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    public void pause(){
        if (mediaPlayer!=null){
            mediaPlayer.pause();
        }
    }

    public long getCurrentTime(){
        if (mediaPlayer!=null){
           return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public boolean isPlaying(){
        if (mediaPlayer!=null){
            return mediaPlayer.isPlaying();
        }
        return false;
    }
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener){
        mediaPlayer.setOnPreparedListener(onPreparedListener);
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener onErrorListener){
        mediaPlayer.setOnErrorListener(onErrorListener);
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener){
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }

    //音乐焦点改变，更好的用户体验
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            //获得音频焦点
            case AudioManager.AUDIOFOCUS_GAIN:
//                if (mMediaPlayer == null) initMediaPlayer();
//                else if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            //失去音频焦点，尽可能的清理资源
            case AudioManager.AUDIOFOCUS_LOSS:
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            //暂时失去焦点，可以不用清理资源
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            //暂时失去焦点，可以低音量播放音乐
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }
}
