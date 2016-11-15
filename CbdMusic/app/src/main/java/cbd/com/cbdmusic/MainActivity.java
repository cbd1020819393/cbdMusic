package cbd.com.cbdmusic;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.io.IOException;

import bean.LyricsProgressBean;
import bean.MusicBean;
import bean.TrcLyricBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import service.MusicService;
import util.LyricProgress;
import util.LyricsProgressUtil;
import util.MediaPlayHelper;
import view.KrcView;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private String TAG = "test";
//    @BindView(R.id.main_recyclerview)
//    RecyclerView recyclerView;

    @BindView(R.id.trc)
    KrcView krcView;

    private MusicService mService;

    TrcLyricBean trcLyricBean;

    boolean mBound = false;

    MediaPlayHelper mediaPlayHelper;
    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            MusicBean musicBean = (MusicBean) msg.getData().getSerializable("data");
//            setData(musicBean);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


//        Intent intent=new Intent(MainActivity.this, MusicService.class);
//        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        LyricProgress lyricProgress = new LyricProgress();
        trcLyricBean = lyricProgress.readTrc(Environment.getExternalStorageDirectory().getPath() + "/test.trc");
        krcView.setArrayList(trcLyricBean.getLeanArrayList());
//        Log.i("test",krcView.getmLrcHeight()+"");
//        Log.i("test",trcLyricBean.getTotal()+"");
//        krcView.animate().translationY(1f).setDuration(Integer.parseInt(trcLyricBean.getTotal())).start();
        //播放资源文件的音乐
        mediaPlayHelper = new MediaPlayHelper(this);
//        mediaPlayHelper.playMusicForRes(R.raw.sound);

        //mediaplay的创建
        mediaPlayHelper.create();
        //播放本地存储音乐
//      mediaPlayer.playMusicForLocalStorage();

        //播放网络音乐
        String url="http://m6.file.xiami.com/17/2017/11020/136064_4394992_h.mp3?auth_key=8efdfacd9944366e99806088cc88fc84-1479438000-0-null";
       // String url = "http://m6.file.xiami.com/298/1298/32403/384272_16850296_h.mp3?auth_key=521f8e45533e96733931ca22818d5b42-1479092400-0-null";
        try {
            mediaPlayHelper.playMusicForNetwork(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayHelper.setOnPreparedListener(this);
//        List<LyricContent> lyricContents = lyricProgress.getLyricList();
//        new Thread() {
////            在新线程中发送网络请求
//            public void run() {
//                String str = new ShowApiRequest("http://route.showapi.com/213-4", "25718", "b17f5c44ade04352b13f2f169b121538")
//                        .addTextPara("topid", "5")
////                        .addTextPara(" num","10")
//                        .post();
//
//                //通知主界面更新
//                MusicBean musicBean=new Gson().fromJson(str, MusicBean.class);
//                Message message=new Message();
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("data",musicBean);
//                message.setData(bundle);
//                handler.sendMessage(message);
//            }
//        }.start();
    }

    @Override
    public void onPrepared(final MediaPlayer mediaPlayer) {

        //计算出逐字播放的动画过程
        LyricsProgressUtil lyricsProgressUtil=new LyricsProgressUtil(krcView);
        LyricsProgressBean lyricsProgressBean= lyricsProgressUtil.calculation(trcLyricBean);
        //歌词从下往上的动画
        ObjectAnimator anim = ObjectAnimator.ofFloat(krcView, "translationY", 0, -(krcView.getmLrcHeight() * 36 / 45));
        anim.setDuration(Integer.parseInt(trcLyricBean.getTotal()));
        anim.start();
        krcView.setLyricsProgressBean(lyricsProgressBean);
        //音乐播放
        mediaPlayHelper.start();
        //逐字播放的动画
        krcView.changeLine();
    }
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
//            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


//    private void setData(MusicBean musicBean){
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
////        recyclerView.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
////        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
//        final MyRecyclerAdapter adapter = new MyRecyclerAdapter();
//        adapter.addDatas(musicBean.getShowapi_res_body().getPagebean().getSonglist());
//        adapter.setmHeaderView(View.inflate(MainActivity.this, R.layout.header, null));
//        adapter.setmHeaderView(View.inflate(MainActivity.this, R.layout.header2, null));
//        adapter.setmFooterView(View.inflate(MainActivity.this, R.layout.header, null));
//        adapter.setmFooterView(View.inflate(MainActivity.this, R.layout.header2, null));
//        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, Object data) {
//                Intent intent=new Intent(MainActivity.this,MusicActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("data",(MusicBean)data);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//
//        adapter.setmOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
//            @Override
//            public void onItemClick(View view, int position, Object data) {
//                Toast.makeText(MainActivity.this, "长点击事件" + position, Toast.LENGTH_SHORT).show();
//                adapter.notifyItemInserted(position);
//            }
//        });
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));
////        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBound){
            unbindService(mConnection);
            mBound=false;
        }
        //释放mediaPlayer资源
        mediaPlayHelper.stop();
    }
}
