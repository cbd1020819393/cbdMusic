package view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.VideoView;

/**
 * Created by A on 2016/10/21.
 */
public class GuideVideoView extends VideoView {
    public GuideVideoView(Context context) {
        super(context);
    }
    public GuideVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GuideVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(heightMeasureSpec));
    }

    public void playVideo(Uri uri){
        if(uri==null){
            throw new IllegalArgumentException("Uri can not be null");
        }

        setVideoURI(uri);

        start();
    }
}
