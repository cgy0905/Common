package com.cgy.common.module.find.contract;

import android.content.Context;

import com.cgy.common.module.find.model.JcodeEntity;
import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/19  14:13
 */
public interface DiscoveryContract {

    interface View extends BaseView {
        void returnData(List<JcodeEntity> data);
    }

    interface Presenter extends BasePresenter {
        void loadData(String url);

        void addRecord(Context context, JcodeEntity entity);
    }
}
