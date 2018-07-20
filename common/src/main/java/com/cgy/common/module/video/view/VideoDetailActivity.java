package com.cgy.common.module.video.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cgy.common.MyApp;
import com.cgy.common.R;
import com.cgy.common.WelcomeActivity;
import com.cgy.common.constant.AppConfig;
import com.cgy.common.module.video.contract.VideoContract;
import com.cgy.common.module.video.model.VideoEntity;
import com.cgy.common.module.video.presenter.VideoPresenter;
import com.cgy.common.net.Apis;
import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.commonwidget.CircleImageView;
import com.llf.basemodel.dialog.ShareDialog;
import com.llf.basemodel.recycleview.BaseAdapter;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.AppInfoUtil;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.basemodel.utils.LogUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXVideoObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import llf.videomodel.VideoPlayer;

import static com.tencent.mm.sdk.platformtools.Util.bmpToByteArray;

/**
 * Created by cgy
 * 2018/7/20  14:34
 * 视频播放
 */
public class VideoDetailActivity extends BaseActivity implements VideoContract.View, ShareDialog.OneShare {

    private static final String TAG = "VideoDetailActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.video_player)
    VideoPlayer videoPlayer;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    private VideoContract.Presenter presenter;

    private BaseAdapter adapter;
    private List<VideoEntity.V9LG4CHORBean> videos = new ArrayList<>();
    private int pageIndex = 0;
    private IWXAPI iwxapi;
    private String url, title;

    public static void launch(Context context, String url, String title, int pageIndex) {
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("pageIndex", pageIndex);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //是被强杀的
        if (MyApp.appStatus == -1) {
            startActivity(WelcomeActivity.class);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initView() {
        final Intent intent = getIntent();
        pageIndex = intent.getIntExtra("pageIndex", 0);
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        videoPlayer.playVideo(url, title);

        iwxapi = WXAPIFactory.createWXAPI(this, AppConfig.APP_ID_WEIXIN, false);
        iwxapi.registerApp(AppConfig.APP_ID_WEIXIN);
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.inflateMenu(R.menu.menu_video_detail);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        showToast("搜索");
                        break;
                    case R.id.love:
                        showToast("喜欢");
                        break;
                    case R.id.share:
                        ShareDialog.show(VideoDetailActivity.this, VideoDetailActivity.this);
                        break;
                    case R.id.report:
                        showToast("举报成功");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

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
                ImageLoaderUtils.loadingImg(VideoDetailActivity.this, imageView, item.getCover());
                ImageLoaderUtils.loadingImg(VideoDetailActivity.this, circleImageView, item.getTopicImg());
            }
        };

        adapter.setOnItemClickLitener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder holder) {
                videoPlayer.playVideo(videos.get(position).getMp4_url(), videos.get(position).getTitle());
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    LogUtil.d(getClass().getSimpleName() + "onMenuOpened...unable to set icons for overflow menu");
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (videoPlayer.isFullScreen()) {
            videoPlayer.setProtrait();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoPlayer != null) {
            videoPlayer.destroyVideo();
            videoPlayer = null;
        }
        videos.clear();
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
    public void weixinShare() {
        if (!AppInfoUtil.isWeixinAvilible(this)) {
            showToast("请先安装微信");
            return;
        }
        WXVideoObject wxVideoObject = new WXVideoObject();
        wxVideoObject.videoUrl = url;
        WXMediaMessage msg = new WXMediaMessage(wxVideoObject);
        msg.title = "最好的视频";
        msg.description = title;
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    @Override
    public void qqShare() {
        showToast("敬请期待");
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
