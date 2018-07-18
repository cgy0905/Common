package com.cgy.common.module.news.presenter;

import com.cgy.common.module.news.contract.NewsDetailContract;
import com.cgy.common.module.news.model.NewsDetailEntity;
import com.cgy.common.module.news.view.NewsDetailActivity;
import com.cgy.common.net.Apis;
import com.cgy.common.tools.NewsJsonUtils;
import com.llf.basemodel.utils.LogUtil;
import com.llf.basemodel.utils.OkHttpUtils;

public class NewsDetailPresenter implements NewsDetailContract.Presenter {
    private NewsDetailContract.View newsDetailView;
    public NewsDetailPresenter(NewsDetailContract.View view) {
        this.newsDetailView = view;
    }

    @Override
    public void loadContent(final String s) {
        newsDetailView.showLoading();
        String detailUrl = getDetailUrl(s);

        OkHttpUtils.get(detailUrl, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtil.d("新闻详情" + response);
                newsDetailView.stopLoading();
                try {
                    NewsDetailEntity newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, s);
                    newsDetailView.showContent(newsDetailBean.getBody());
                } catch (NullPointerException e) {
                    LogUtil.d("空指针");
                }
            }

            @Override
            public void onFailure(Exception e) {
                newsDetailView.stopLoading();
            }
        });
    }

    private String getDetailUrl(String docId) {
        StringBuffer sb = new StringBuffer(Apis.NEW_DETAIL);
        sb.append(docId).append(Apis.END_DETAIL_URL);

        return sb.toString();
    }

    @Override
    public void start() {

    }
}
