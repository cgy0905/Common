package com.cgy.common.module.news.presenter;

import com.cgy.common.module.news.contract.NewsContract;
import com.cgy.common.module.news.model.NewsEntity;
import com.cgy.common.module.news.model.NewsModel;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/18  17:59
 */
public class NewsPresenter implements NewsContract.Presenter, NewsModel.OnLoadFirstDataListener{

    private NewsContract.View newsView;
    private NewsContract.Model newsModel;

    public NewsPresenter(NewsContract.View view) {
        this.newsView = view;
        this.newsModel = new NewsModel();
    }

    @Override
    public void loadData(int type, int page) {

    }

    @Override
    public void onSuccess(List<NewsEntity> list) {

    }

    @Override
    public void onFailure(String str, Exception e) {

    }

    @Override
    public void start() {

    }
}
