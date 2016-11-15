package view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import bean.LyricsProgressBean;
import bean.TycLyricLeanBean;
import cbd.com.cbdmusic.R;

/**
 * Created by ss on 2015/8/7.
 */
public class KrcView extends View {

    private ArrayList<TycLyricLeanBean> arrayList = new ArrayList<>();
    private int mViewWidth; // view的宽度
    private int mLrcHeight; // lrc界面的高度
    private int mRows;      // 多少行
    private int mCurrentLine = 0; // 当前行
    private float mProgress;
    private float mTextSize; // 字体
    private float mDividerHeight; // 行间距
    private Paint mNormalPaint; // 常规的字体
    private Paint mCurrentPaint; // 当前歌词的大小
    private Bitmap mBackground = null;
    private float float1 = 0.0f;//渲染百分比
    private float float2 = 0.01f;
    private boolean isChanging = false;//视图是否正在更新，用处不大
    private int normalTextColor;//歌词颜色
    private int currentTextColor;//当前歌词颜色
    private long total = 0;
    //下一行的起始时间
    private long nextStartTime;
    //当前行时间
    private long currentStartTime;

    public LyricsProgressBean getLyricsProgressBean() {
        return lyricsProgressBean;
    }

    public void setLyricsProgressBean(LyricsProgressBean lyricsProgressBean) {
        this.lyricsProgressBean = lyricsProgressBean;
    }

    private LyricsProgressBean lyricsProgressBean;
    public KrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public KrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    public ArrayList<TycLyricLeanBean> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<TycLyricLeanBean> arrayList) {
        this.arrayList = arrayList;
        mLrcHeight = (int) (mTextSize + mDividerHeight) * (9+arrayList.size()) + 5;
        invalidate();
    }


    public float getmProgress() {
        return mProgress;
    }

    public void setmProgress(float mProgress) {
        this.mProgress = mProgress;
    }

    public float getFloat1() {
        return float1;
    }

    public void setFloat1(float float1) {
        this.float1 = float1;
        invalidate();
    }

    // 初始化操作
    private void initViews(AttributeSet attrs) {
        // <begin>
        // 解析自定义属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.Lrc);
        mTextSize = ta.getDimension(R.styleable.Lrc_textSize, 50.0f);
        mRows = ta.getInteger(R.styleable.Lrc_rows, 5);
        mDividerHeight = ta.getDimension(R.styleable.Lrc_dividerHeight, 0.0f);

        normalTextColor = ta.getColor(R.styleable.Lrc_normalTextColor,
                Color.BLACK);
        currentTextColor = ta.getColor(R.styleable.Lrc_currentTextColor,
                Color.YELLOW);
        mProgress = ta.getFloat(R.styleable.Lrc_mProgress, 0);
        ta.recycle();
        // </end>
        // 计算krc面板的高度
        mLrcHeight = (int) (mTextSize + mDividerHeight) * 45 + 5;
        mNormalPaint = new Paint();
        mCurrentPaint = new Paint();
        // 初始化paint
        mNormalPaint.setTextSize(mTextSize);
        mNormalPaint.setColor(normalTextColor);
        mCurrentPaint.setTextSize(mTextSize);
        //mCurrentPaint.setColor(currentTextColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 获取view宽度
        mViewWidth = getMeasuredWidth();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 重新设置view的高度
        int measuredHeight = MeasureSpec.makeMeasureSpec(mLrcHeight, MeasureSpec.AT_MOST);
        setMeasuredDimension(widthMeasureSpec, measuredHeight);
        //setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }


    //关键代码
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (arrayList.isEmpty()) {
            return;
        }

        canvas.save();
        // 圈出可视区域
        canvas.clipRect(0, 0, mViewWidth, mLrcHeight);

        if (null != mBackground) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(mBackground, mViewWidth, mLrcHeight, true),
                    new android.graphics.Matrix(), null);
        }

//        // 将画布上移
//        canvas.translate(0, -((mCurrentLine - (mRows+1)/2) * (mTextSize + mDividerHeight)));
        canvas.translate(0,9 * (mTextSize + mDividerHeight));
        // 画当前行上面的
        for (int i = mCurrentLine - 1; i >= 0; i--) {
            String lrc = arrayList.get(i).getLine();
            float x = (mViewWidth - mNormalPaint.measureText(lrc)) / 2;
            canvas.drawText(lrc, x, (mTextSize + mDividerHeight) * i, mNormalPaint);
        }

        String currentLrc = arrayList.get(mCurrentLine).getLine();
        float currentX = (mViewWidth - mCurrentPaint.measureText(currentLrc)) / 2;

        // 获得字符串的"长度"
        float len = mCurrentPaint.getTextSize() * currentLrc.length();
        // 参数color数组表示参与渐变的集合
        // 参数float数组表示对应颜色渐变的位置
        int[] a = new int[]{currentTextColor, normalTextColor};
        float[] f = new float[]{float1, float2};
        Shader shader = new LinearGradient(currentX,
                (mTextSize + mDividerHeight) * mCurrentLine,
                len + currentX,
                (mTextSize + mDividerHeight) * mCurrentLine,
                a, f, Shader.TileMode.CLAMP);
        mCurrentPaint.setShader(shader);
        // 画当前行
        canvas.drawText(currentLrc, currentX, (mTextSize + mDividerHeight) * mCurrentLine, mCurrentPaint);

        // 画当前行下面的
        for (int i = mCurrentLine + 1; i < arrayList.size(); i++) {
            String lrc = arrayList.get(i).getLine();
            float x = (mViewWidth - mNormalPaint.measureText(lrc)) / 2;
            canvas.drawText(lrc, x, (mTextSize + mDividerHeight) * i, mNormalPaint);
        }

        canvas.restore();
    }


    //
    // 背景图片
    public void setBackground(Bitmap bmp) {
        mBackground = bmp;
    }


    // 外部提供方法
    // 传入当前播放时间
    public void changeLine() {

        AnimatorSet set = new AnimatorSet();
        ArrayList<Animator> list = lyricsProgressBean.getList().get(mCurrentLine).getAnimatorArrayList();
        set.playSequentially(list);

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                super.onAnimationEnd(animation);
                if (mCurrentLine != arrayList.size() - 1) {//不是最后一行
                    //歌词等待时间做偏移，不知道什么原因，完全用计算出来的等待时间，
                    // 歌词总是对不上歌曲的播放，只能采取这种傻瓜的方式
                    long time = lyricsProgressBean.getList().get(mCurrentLine).getWaitTime();
                    if(lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()>20000){
                        time=lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()-1000;
                    }if(20000>lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()&&lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()>10000){
                        time=lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()-300;
                    }else if(10000>lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()&&lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()>1000){
                        time=lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()-200;
                    }else if(1000>lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()&&lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()>500){
                        time=time-200;
                    }else if(500>lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()&&lyricsProgressBean.getList().get(mCurrentLine).getWaitTime()>300){
                        time=time-50;
                    }


                    ObjectAnimator ob = ObjectAnimator.ofFloat(this, "cbd",1).setDuration(time);
                    Log.i("totaltime", "currentLine："+mCurrentLine+"；"+lyricsProgressBean.getList().get(mCurrentLine).getWaitTime() + "");
                    ob.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mCurrentLine++;
                            changeLine();
                        }
                    });
                    ob.start();
                }
            }
        });
        set.start();
    }


}
