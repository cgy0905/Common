package com.cgy.common.module.video.view;

import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.TextureView;

import com.cgy.common.R;
import com.cgy.common.module.video.contract.VideoContract;
import com.cgy.common.module.video.model.VideoEntity;
import com.llf.basemodel.base.BaseActivity;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/20  17:23
 *
 * 类似于YouTuBe的activity
 */

@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class YouTuBeActivity extends BaseActivity implements TextureView.SurfaceTextureListener, VideoContract.View{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_youtube;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void returnData(List<VideoEntity.V9LG4CHORBean> data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }
}
