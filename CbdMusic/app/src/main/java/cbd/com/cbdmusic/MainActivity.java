package cbd.com.cbdmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.Gson;
import com.show.api.ShowApiRequest;

import java.util.List;

import adapter.BaseRecyclerAdapter;
import adapter.MyRecyclerAdapter;
import bean.LyricContent;
import bean.MusicBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import decorator.DividerItemDecoration;
import service.MusicService;
import util.LyricProgress;

public class MainActivity extends AppCompatActivity {
    private String TAG = "test";
    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerView;

    private MusicService mService;

    boolean mBound = false;

    Handler handler=new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            MusicBean musicBean=(MusicBean) msg.getData().getSerializable("data");
            setData(musicBean);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Intent intent=new Intent(MainActivity.this, MusicService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);

       ;LyricProgress lyricProgress = new LyricProgress();
        lyricProgress.readLyric(Environment.getExternalStorageDirectory().getPath()+"/test.trc");
        List<LyricContent> lyricContents = lyricProgress.getLyricList();
        new Thread() {
//            在新线程中发送网络请求
            public void run() {
                String str = new ShowApiRequest("http://route.showapi.com/213-4", "25718", "b17f5c44ade04352b13f2f169b121538")
                        .addTextPara("topid", "5")
//                        .addTextPara(" num","10")
                        .post();

                //通知主界面更新
                MusicBean musicBean=new Gson().fromJson(str, MusicBean.class);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",musicBean);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }.start();
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


    private void setData(MusicBean musicBean){
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        final MyRecyclerAdapter adapter = new MyRecyclerAdapter();
        adapter.addDatas(musicBean.getShowapi_res_body().getPagebean().getSonglist());
        adapter.setmHeaderView(View.inflate(MainActivity.this, R.layout.header, null));
        adapter.setmHeaderView(View.inflate(MainActivity.this, R.layout.header2, null));
        adapter.setmFooterView(View.inflate(MainActivity.this, R.layout.header, null));
        adapter.setmFooterView(View.inflate(MainActivity.this, R.layout.header2, null));
        adapter.setOnItemClickLitener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                Intent intent=new Intent(MainActivity.this,MusicActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",(MusicBean)data);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        adapter.setmOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                Toast.makeText(MainActivity.this, "长点击事件" + position, Toast.LENGTH_SHORT).show();
                adapter.notifyItemInserted(position);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBound){
            unbindService(mConnection);
            mBound=false;
        }
    }
}
