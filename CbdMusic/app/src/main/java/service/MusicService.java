package service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.io.IOException;

import cbd.com.cbdmusic.MainActivity;
import cbd.com.cbdmusic.R;
import util.MediaPlayHelper;
import util.NotificationHelper;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    private WifiManager.WifiLock wifiLock;

    MediaPlayHelper mediaPlayHelper;
    long thisId;
    @Override

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //发送自定义的Notification
        sendNotification();
        //播放资源文件的音乐
        mediaPlayHelper=new MediaPlayHelper(this);
//        mediaPlayHelper.playMusicForRes(R.raw.sound);

        //mediaplay的创建
        mediaPlayHelper.create();
        //播放本地存储音乐
//      mediaPlayer.playMusicForLocalStorage();

        //播放网络音乐
        String url = "http://m6.file.xiami.com/298/1298/32403/384272_16850296_h.mp3?auth_key=521f8e45533e96733931ca22818d5b42-1479092400-0-null";
        try {
            mediaPlayHelper.playMusicForNetwork(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayHelper.setOnPreparedListener(this);
        mediaPlayHelper.setOnErrorListener(this);
        mediaPlayHelper.setOnCompletionListener(this);

        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

        wifiLock.acquire();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放mediaPlayer资源
        mediaPlayHelper.stop();
        //释放wifiLock资源
        if(wifiLock!=null){
            wifiLock.release();
            wifiLock=null;
        }
        //停止前台进程
        stopForeground(true);

    }

    private void sendNotification(){
        NotificationHelper notificationHelper = new NotificationHelper(this);
        Intent intent = new Intent(MusicService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MusicService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteView.setOnClickPendingIntent(R.id.imageView1, pendingIntent);
        remoteView.setOnClickPendingIntent(R.id.imageView2, pendingIntent);
        remoteView.setOnClickPendingIntent(R.id.imageView3, pendingIntent);
        startForeground(2,notificationHelper.returnCustomNotification(null, remoteView, R.mipmap.ic_launcher, false));
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayHelper.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }



    public class LocalBinder extends Binder {
        public MusicService getService() {
            // 返回本service的实例到客户端，于是客户端可以调用本service的公开方法
            return MusicService.this;
        }
    }
}
