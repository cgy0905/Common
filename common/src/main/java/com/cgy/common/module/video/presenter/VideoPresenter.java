package com.cgy.common.module.video.presenter;

import com.cgy.common.module.video.contract.VideoContract;
import com.cgy.common.module.video.model.VideoEntity;
import com.llf.basemodel.utils.JsonUtils;
import com.llf.basemodel.utils.LogUtil;
import com.llf.basemodel.utils.OkHttpUtils;

/**
 * Created by cgy
 * 2018/7/20  14:19
 *
 * 浏览器能访问接口   客户端禁止访问是请求头的问题 需要加请求头
 */
public class VideoPresenter implements VideoContract.Presenter {

    private VideoContract.View videoView;

    public VideoPresenter (VideoContract.View view) {
        this.videoView = view;
    }

    @Override
    public void loadData(String url) {
        OkHttpUtils.get(url, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtil.d("视频列表" + response);
                try {
                    VideoEntity entity = JsonUtils.deserialize(response, VideoEntity.class);
                } catch (Exception e) {
                    LogUtil.d("Gson解析出错");
                }
            }

            @Override
            public void onFailure(Exception e) {
                videoView.showErrorTip(e.getMessage());
            }
        });
    }

    @Override
    public void start() {

    }
}
