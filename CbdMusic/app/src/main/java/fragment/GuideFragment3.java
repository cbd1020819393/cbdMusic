package fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cbd.com.cbdmusic.R;
import view.GuideVideoView;


public class GuideFragment3 extends BaseGuideFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_guide_fragment3, null, false);
        guideVideoView= (GuideVideoView) view.findViewById(R.id.guide_fragment_videoview_3);
        return view;
    }
    public void videoPlay(){
        setVideoPlayInitialization(Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.splash_3),3);
    }
}
