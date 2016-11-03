package cbd.com.cbdmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bean.MusicBean;

public class MusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        //获取传递的数据
        if(getIntent()!=null&&getIntent().getExtras()!=null){
            Bundle bundle=getIntent().getExtras();
            MusicBean musicBean=(MusicBean) bundle.getSerializable("data");
        }
    }
}
