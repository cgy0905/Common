package com.cgy.common.module.video.contract;

import com.cgy.common.module.video.model.VideoEntity;
import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/20  14:12
 */
public interface VideoContract {

    interface View extends BaseView {
        void returnData(List<VideoEntity.V9LG4CHORBean> data);
    }

    interface Presenter extends BasePresenter {

        void loadData(String url);
    }
}
