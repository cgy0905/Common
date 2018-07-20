package com.cgy.common.module.mine.contract;

import android.content.Context;

import com.cgy.common.module.find.model.JcodeEntity;
import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/20  10:47
 */
public interface TrackContract {
    interface View extends BaseView {
        void returnData(List<JcodeEntity> data);
        void returnResult(boolean result);
    }

    interface Presenter extends BasePresenter {
        void loadData(Context context);
        void deleteData(Context context, String title);
    }
}
