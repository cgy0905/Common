package com.cgy.common.module.news.contract;

import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;

public interface NewsDetailContract {

    interface View extends BaseView {
        void showContent(String s);
    }

    interface Presenter extends BasePresenter {
        void loadContent(String s);
    }
}
