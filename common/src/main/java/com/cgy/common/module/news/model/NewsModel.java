package com.cgy.common.module.news.model;

import com.cgy.common.module.news.contract.NewsContract;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/18  17:56
 */
public class NewsModel implements NewsContract.Model{


    @Override
    public void loadData(String url, int type, OnLoadFirstDataListener listener) {

    }

    public interface OnLoadFirstDataListener{
        void  onSuccess(List<NewsEntity> list);
        void  onFailure(String str,Exception e);
    }
}
