package com.cgy.common.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by cgy
 * 2018/7/23  9:22
 * <p>
 * 视频滑动变小
 */
public class YouTubeVideoView extends LinearLayout {

    public interface Callback {
        void onVideoViewHide();

        void onVideoClick();
    }

    //单击以及消失时的回调
    private Callback callback;

    //可拖动的videoView 和下方详情View
    private View videoView;
    private View detailView;
    //video类的包装类 用于属性动画
    private VideoViewWrapper videoWrapper;

    //滑动区间 取值为videoView最小化时距离屏幕顶端的高度
    private float allScrollY;

    //1f为初始状态 0.5f或0.25f(横屏时)为最小状态
    private float nowStateScale;

    //最小的缩放比例
    private float MIN_RATIO = 0.5f;
    //播放器比例
    private static final float VIDEO_RATIO = 16f / 9f;

    //是否是第一次Measure，用于获取播放器初始宽高
    private boolean isFirstMeasure = true;

    //VideoView初始宽高
    private int originalWidth;
    private int originalHeight;

    //最小时距离屏幕右边以及下边的 DP值 初始化时会转化为PX
    private static final int MARGIN_DP = 12;
    private int marginPx;

    //是否可以横滑删除
    private boolean canHide;


    public YouTubeVideoView(Context context) {
        this(context, null);
    }

    public YouTubeVideoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouTubeVideoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 该方法在布局文件的结构解析完毕之后执行，此时就知道了自己有几个子View，而且该方法
     * 在onMeasure执行之前，所以在该方法中还获取不到VIew的宽高
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2)
            throw new RuntimeException("YouTubeVideoView only need 2 child views");

        videoView = getChildAt(0);
        detailView = getChildAt(1);

        init();
    }

    private void init() {
        videoView.setOnTouchListener(new VideoTouchListener());
        //初始化包装类
        videoWrapper = new VideoViewWrapper();
        //dp to px
        marginPx = MARGIN_DP * (getContext().getResources().getDisplayMetrics().densityDpi / 160);

        //当前缩放比例
        nowStateScale = 1f;

        //如果是横屏则最小化比例为0.25f
        if (videoView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            MIN_RATIO = 0.25f;

        originalWidth = videoView.getContext().getResources().getDisplayMetrics().widthPixels;
        originalHeight = (int) (originalWidth / VIDEO_RATIO);

        ViewGroup.LayoutParams params = videoView.getLayoutParams();
        params.width = originalWidth;
        params.height = originalHeight;
        videoView.setLayoutParams(params);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isFirstMeasure) {
            //滑动区间：取值为videoView最小化时距离屏幕顶端的高度 也就是最小化时的marginTop
            allScrollY = this.getMeasuredHeight() - MIN_RATIO * originalHeight - marginPx;
            isFirstMeasure = false;
        }
    }

    private class VideoTouchListener implements OnTouchListener {
        //保存上一个滑动事件手指的坐标
        private int lastY;
        private int lastX;
        //刚触摸时手指的坐标
        private int downY;
        private int downX;

        private int dy;//和上一次滑动的差值 设置为全局变量是因为 UP里也要使用

        private boolean isClick;
        private int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        private VelocityTracker tracker;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int y = (int) event.getRawX();
            int x = (int) event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isClick = true;
                    tracker = VelocityTracker.obtain();
                    downX = (int) event.getRawX();
                    downY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    tracker.addMovement(event);
                    dy = y - lastY;//和上一次滑动的差值
                    int dx = x - lastX;
                    int newMarY = videoWrapper.getMarginTop() + dy; //新的marginTop值
                    int newMarX = videoWrapper.getMarginRight() - dx;//新的marginRight值
                    int dDownY = y - downY;
                    int dDownX = x - downX;//从点击点开始产生的差值

                    //如果滑动达到一定距离
                    if (Math.abs(dDownX) > touchSlop || Math.abs(dDownY) > touchSlop) {
                        isClick = false;
                        //如果X > Y 且能滑动关闭 则动态设置水平偏移量
                        if (Math.abs(dDownX) > Math.abs(dDownY) && canHide) {
                            videoWrapper.setMarginRight(newMarX);
                        } else
                            updateVideoView(newMarY);//否则通过新的marginTop的值更新大小
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:

                    if (isClick) {
                        if (nowStateScale == 1f && callback != null) {
                            //单击事件回调
                            callback.onVideoClick();
                        } else {
                            goMax();
                        }
                        break;
                    }

                    tracker.computeCurrentVelocity(100);
                    float yVelocity = Math.abs(tracker.getYVelocity());
                    tracker.clear();
                    tracker.recycle();

                    if (canHide) {
                        //速度大于一定值或者滑动的距离超过了最小化时的宽度 则进行隐藏 否则保持最小状态
                        if (yVelocity > touchSlop || Math.abs(videoWrapper.getMarginRight()) > MIN_RATIO * originalWidth)
                            dismissView();
                        else
                            goMin();
                    } else
                        confirmState(yVelocity, dy);//确定状态
                    break;
            }
            lastY = y;
            lastX = x;
            return true;
        }
    }

    private void updateVideoView(int m) {
        //如果当前状态时最小化 先把我们的布局宽高设置为MATCH_PARENT
        if (nowStateScale == MIN_RATIO) {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = -1;
            params.height = -1;
            setLayoutParams(params);
        }

        canHide = false;

        //marginTop的值最大为allScrollY 最小为0
        if (m > allScrollY)
            m = (int) allScrollY;
        if (m < 0)
            m = 0;

        //视频View的高度的百分比100% - 0%
        float marginPercent = (allScrollY - m) / allScrollY;
        //视频View对应的大小的百分比 100% - 50%或25%
        float videoPercent = MIN_RATIO + (1f - MIN_RATIO) * marginPercent;

        //设置宽高
        videoWrapper.setWidth(originalWidth * videoPercent);
        videoWrapper.setHeight(originalHeight * videoPercent);

        detailView.setAlpha(marginPercent);//设置下方详情View透明度

        int mr = (int) ((1f - marginPercent) * marginPx);//VideoView右边和详情View上方的margin
        videoWrapper.setZ(mr / 2);//这个是Z轴的值 悬浮效果

        videoWrapper.setMarginTop(m);
        videoWrapper.setMarginRight(mr);
        videoWrapper.setDetailMargin(mr);

    }

    private void dismissView() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(videoView, "alpha", 1f, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(INVISIBLE);
            }
        });
        animator.setDuration(300).start();

        if (callback != null)
            callback.onVideoViewHide();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void confirmState(float v, int dy) {    //dy用于判断是否反方向滑动了

        //如果手指抬起时宽度达到一定值 或者 速度达到一定值 则改变状态
        if (nowStateScale == 1f) {
            if (videoView.getWidth() <= originalWidth * 0.75f || (v > 15 && dy > 0)) {
                goMin();
            } else
                goMax();
        } else {
            if (videoView.getWidth() >= originalWidth * 0.75f || (v > 15 && dy < 0)) {
                goMax();
            } else
                goMin();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void goMax() {
        if (nowStateScale == MIN_RATIO) {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = -1;
            params.height = -1;
            setLayoutParams(params);
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(videoWrapper, "width", videoWrapper.getWidth(), originalWidth),
                ObjectAnimator.ofFloat(videoWrapper, "height", videoWrapper.getHeight(), originalHeight),
                ObjectAnimator.ofInt(videoWrapper, "marginTop", videoWrapper.getMarginTop(), 0),
                ObjectAnimator.ofInt(videoWrapper, "marginRight", videoWrapper.getMarginRight(), 0),
                ObjectAnimator.ofInt(videoWrapper, "detailMargin", videoWrapper.getDetailMargin(), 0),
                ObjectAnimator.ofFloat(videoWrapper, "z", videoWrapper.getZ(), 0),
                ObjectAnimator.ofFloat(detailView, "alpha", detailView.getAlpha(), 1f)
        );
        set.setDuration(200).start();
        nowStateScale = 1.0f;
        canHide = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void goMin() {
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(videoWrapper, "width", videoWrapper.getWidth(), originalWidth * MIN_RATIO),
                ObjectAnimator.ofFloat(videoWrapper, "height", videoWrapper.getHeight(), originalHeight * MIN_RATIO),
                ObjectAnimator.ofInt(videoWrapper, "marginTop", videoWrapper.getMarginTop(), (int) allScrollY),
                ObjectAnimator.ofInt(videoWrapper, "marginRight", videoWrapper.getMarginRight(), marginPx),
                ObjectAnimator.ofInt(videoWrapper, "detailMargin", videoWrapper.getDetailMargin(), marginPx),
                ObjectAnimator.ofFloat(videoWrapper, "z", videoWrapper.getZ(), marginPx / 2),
                ObjectAnimator.ofFloat(detailView, "alpha", detailView.getAlpha(), 0)
        );
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                canHide = true;

                ViewGroup.LayoutParams params = getLayoutParams();
                params.width = -2;
                params.height = -2;
                setLayoutParams(params);

                nowStateScale = MIN_RATIO;
            }
        });
        set.setDuration(200).start();
    }

    //获取当前状态
    public float getNowStateScale() {
        return nowStateScale;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void show() {
        setVisibility(VISIBLE);
        goMax();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private class VideoViewWrapper {
        private LayoutParams params;
        private LayoutParams detailParams;


        VideoViewWrapper() {
            params = (LayoutParams) videoView.getLayoutParams();
            detailParams = (LayoutParams) detailView.getLayoutParams();
            params.gravity = Gravity.END;
        }

        int getWidth() {
            return params.width < 0 ? originalWidth : params.width;
        }

        int getHeight() {
            return params.height < 0 ? originalHeight : params.height;
        }

        void setWidth(float width) {
            if (width == originalWidth) {
                params.width = -1;
                params.setMargins(0, 0, 0, 0);
            } else
                params.width = (int) width;

            videoView.setLayoutParams(params);
        }

        void setHeight(float height) {
            params.height = (int) height;
            videoView.setLayoutParams(params);
        }

        void setMarginTop(int m) {
            params.topMargin = m;
            videoView.setLayoutParams(params);
        }

        int getMarginTop() {
            return params.topMargin;
        }

        void setMarginRight(int mr) {
            params.rightMargin = mr;
            videoView.setLayoutParams(params);
        }

        int getMarginRight() {
            return params.rightMargin;
        }

        void setZ(float z) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                videoView.setTranslationZ(z);

        }

        float getZ() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                return videoView.getTranslationZ();
            else
                return 0;
        }

        void setDetailMargin(int t) {
            detailParams.topMargin = t;
            detailView.setLayoutParams(detailParams);
        }

        int getDetailMargin() {
            return detailParams.topMargin;
        }
    }
}
