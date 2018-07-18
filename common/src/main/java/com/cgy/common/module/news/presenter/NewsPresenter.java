package com.cgy.common.module.news.presenter;

import com.cgy.common.module.news.contract.NewsContract;
import com.cgy.common.module.news.model.NewsEntity;
import com.cgy.common.module.news.model.NewsModel;
import com.cgy.common.module.news.view.NewsFragment;
import com.cgy.common.net.Apis;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/18  17:59
 */
public class NewsPresenter implements NewsContract.Presenter, NewsModel.OnLoadFirstDataListener {

    private NewsContract.View newsView;
    private NewsContract.Model newsModel;

    public NewsPresenter(NewsContract.View view) {
        this.newsView = view;
        this.newsModel = new NewsModel();
    }

    @Override
    public void loadData(int type, int page) {
        String url = getUrl(type, page);
        newsModel.loadData(url, type, this);
    }

    @Override
    public void onSuccess(List<NewsEntity> list) {
        newsView.returnData(list);
    }

    @Override
    public void onFailure(String str, Exception e) {
        newsView.showErrorTip("您已进入没有网络二次元");
    }

    private String getUrl(int type, int page) {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case NewsFragment.ONE:
                sb.append(Apis.TOP_URL).append(Apis.TOP_ID);
                break;
            case NewsFragment.TWO:
                sb.append(Apis.COMMON_URL).append(Apis.NBA_ID);
                break;
            case NewsFragment.THREE:
                sb.append(Apis.COMMON_URL).append(Apis.CAR_ID);
                break;
            case NewsFragment.FOUR:
                sb.append(Apis.COMMON_URL).append(Apis.JOKE_ID);
                break;
            default:
                sb.append(Apis.TOP_URL).append(Apis.TOP_ID);
                break;
        }
        sb.append("/").append(page).append(Apis.END_URL);
        return sb.toString();
    }

    @Override
    public void start() {

    }
}
