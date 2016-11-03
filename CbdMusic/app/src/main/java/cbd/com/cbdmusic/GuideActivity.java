package cbd.com.cbdmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fragment.GuideFragment1;
import fragment.GuideFragment2;
import fragment.GuideFragment3;
import fragment.GuideFragment4;


public class GuideActivity extends AppCompatActivity {
    @BindView(R.id.guide_viewpager)
    ViewPager viewPager;

    @BindView(R.id.guide_button)
    Button button;
    private MyPagerAdapter pagerAdapter;
    private GuideFragment1 guideFragment1;
    private GuideFragment2 guideFragment2;
    private GuideFragment3 guideFragment3;
    private GuideFragment4 guideFragment4;
    private List<Fragment> fragmentList=new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        viewPager.setOffscreenPageLimit(4);

        if(guideFragment1==null){
            guideFragment1=new GuideFragment1();
        }

        if(guideFragment2==null){
            guideFragment2=new GuideFragment2();
        }

        if(guideFragment3==null){
            guideFragment3=new GuideFragment3();
        }

        if(guideFragment4==null){
            guideFragment4=new GuideFragment4();
        }

        fragmentList.add(guideFragment1);
        fragmentList.add(guideFragment2);
        fragmentList.add(guideFragment3);
        fragmentList.add(guideFragment4);

        guideFragment1.setViewPager(viewPager);
        guideFragment2.setViewPager(viewPager);
        guideFragment3.setViewPager(viewPager);
        guideFragment4.setViewPager(viewPager);

        pagerAdapter=new MyPagerAdapter(getSupportFragmentManager(),fragmentList);

        viewPager.setAdapter(pagerAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        guideFragment1.videoPlay();
                        guideFragment2.videoStop();
                        guideFragment3.videoStop();
                        guideFragment4.videoStop();
                        break;
                    case 1:
                        guideFragment2.videoPlay();
                        guideFragment1.videoStop();
                        guideFragment3.videoStop();
                        guideFragment4.videoStop();
                        break;
                    case 2:
                        guideFragment3.videoPlay();
                        guideFragment1.videoStop();
                        guideFragment2.videoStop();
                        guideFragment4.videoStop();
                        break;
                    case 3:
                        guideFragment4.videoPlay();
                        guideFragment1.videoStop();
                        guideFragment2.videoStop();
                        guideFragment3.videoStop();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> list;

        public MyPagerAdapter(FragmentManager fm,List<Fragment> list) {
            super(fm);
            this.list=list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
