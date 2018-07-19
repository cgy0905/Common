package com.cgy.common.module.mine.contract;

import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;

/**
 * Created by cgy
 * 2018/7/19  17:27
 */
public interface MineContract {

    interface View extends BaseView {
        void returnResult(String result);
    }

    interface Presenter extends BasePresenter {
        void checkUpdate(String url);
    }
}
