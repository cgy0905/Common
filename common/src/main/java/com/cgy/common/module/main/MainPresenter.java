package com.cgy.common.module.main;

import com.cgy.common.MyApp;
import com.llf.basemodel.utils.DownloadUtil;
import com.llf.basemodel.utils.JsonUtils;
import com.llf.basemodel.utils.LogUtil;
import com.llf.basemodel.utils.OkHttpUtils;

/**
 * Created by cgy
 * 2018/7/18  16:57
 */
class MainPresenter implements MainContract.Presenter {

    private MainContract.View mainView;

    public MainPresenter(MainContract.View view) {
        this.mainView = view;
    }

    @Override
    public void checkUpdate(String url) {
        OkHttpUtils.get(url, new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response) {
                LogUtil.d("应用更新:" + response);
                try {
                    ApplicationEntity entity = JsonUtils.deserialize(response, ApplicationEntity.class);
                    mainView.returnUpdateResult(entity);
                } catch (Exception e) {
                    mainView.returnResult("System Object");
                }

            }

            @Override
            public void onFailure(Exception e) {
                mainView.returnResult(e.getMessage());
            }
        });
    }

    @Override
    public void update(ApplicationEntity entity) {
        DownloadUtil.downloadApk(MyApp.instance, entity.getInstall_url(), entity.getName(), entity.getChangelog(), "xiuqu.apk");
    }

    @Override
    public void start() {

    }
}
