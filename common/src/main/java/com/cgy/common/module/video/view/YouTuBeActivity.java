package com.cgy.common.module.video.view;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;

import com.cgy.common.MyApp;
import com.cgy.common.R;
import com.cgy.common.WelcomeActivity;
import com.cgy.common.module.video.contract.VideoContract;
import com.cgy.common.module.video.model.VideoEntity;
import com.cgy.common.module.video.presenter.VideoPresenter;
import com.cgy.common.net.Apis;
import com.cgy.common.widget.YouTubeVideoView;
import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.commonwidget.CircleImageView;
import com.llf.basemodel.recycleview.BaseAdapter;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by cgy
 * 2018/7/20  17:23
 * <p>
 * 类似于YouTuBe的activity
 */

@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class YouTuBeActivity extends BaseActivity implements TextureView.SurfaceTextureListener, VideoContract.View, YouTubeVideoView.Callback, MediaPlayer.OnPreparedListener {

    private static final String TAG = "YouTuBeActivity";
    @Bind(R.id.video_view)
    TextureView videoView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.youtube_view)
    YouTubeVideoView youtubeView;

    private MediaPlayer mediaPlayer;
    private BaseAdapter adapter;
    private List<VideoEntity.V9LG4CHORBean> videos = new ArrayList<>();
    private VideoContract.Presenter presenter;
    private int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //是被强杀的
        if (MyApp.appStatus == -1) {
            startActivity(WelcomeActivity.class);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_youtube;
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void initView() {
        videoView.setSurfaceTextureListener(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.test_video);
        mediaPlayer.setOnPreparedListener(this);
        youtubeView.setCallback(this);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseAdapter<VideoEntity.V9LG4CHORBean>(this, R.layout.item_video, videos) {

            @Override
            public void convert(BaseViewHolder viewHolder, VideoEntity.V9LG4CHORBean item) {
                viewHolder.setText(R.id.tv_title, item.getTitle());
                viewHolder.setText(R.id.tv_source, item.getVideosource());
                ImageView imageView = viewHolder.getView(R.id.iv_cover);
                CircleImageView circleImageView = viewHolder.getView(R.id.cir_top_img);
                ImageLoaderUtils.loadingImg(YouTuBeActivity.this, imageView, item.getCover());
                ImageLoaderUtils.loadingImg(YouTuBeActivity.this, circleImageView, item.getTopicImg());
            }
        };

        adapter.setOnItemClickLitener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder holder) {

            }

            @Override
            public void onItemLongClick(int position) {

            }
        });
        recyclerView.setAdapter(adapter);

        presenter = new VideoPresenter(this);
        presenter.loadData(Apis.HOST + Apis.Video + Apis.VIDEO_HOT_ID + "/n/" + pageIndex * 10 + "-10.html");

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mediaPlayer.setSurface(new Surface(surface));
        mediaPlayer.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        finish();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    @Override
    public void showLoading() {
        startProgressDialog();
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {
        showErrorHint(msg);
    }

    @Override
    public void returnData(List<VideoEntity.V9LG4CHORBean> data) {
        videos.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.setLooping(true);
    }

    @Override
    public void onVideoViewHide() {
        mediaPlayer.pause();
    }

    @Override
    public void onVideoClick() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && youtubeView.getNowStateScale() == 1f) {
            youtubeView.goMin();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
