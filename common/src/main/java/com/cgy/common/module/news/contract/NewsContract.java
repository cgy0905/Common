package com.cgy.common.module.news.contract;

import com.cgy.common.module.news.model.NewsEntity;
import com.cgy.common.module.news.model.NewsModel;
import com.llf.basemodel.base.BaseModel;
import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/18  17:54
 *
 * 头条契约类
 */
public interface NewsContract {
    interface View extends BaseView {
        void returnData(List<NewsEntity> datas);
    }

    interface Presenter extends BasePresenter {
        void loadData(int type, int page);
    }

    interface Model extends BaseModel {

        void loadData(String url, int type, NewsModel.OnLoadFirstDataListener listener);
    }
}
