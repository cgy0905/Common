package com.cgy.common.module.news.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cgy.common.R;
import com.cgy.common.module.news.adapter.NewsAdapter;
import com.cgy.common.module.news.contract.NewsContract;
import com.cgy.common.module.news.model.NewsEntity;
import com.cgy.common.module.news.presenter.NewsPresenter;
import com.cgy.common.net.Apis;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.image.BigImagePagerActivity;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.recycleview.DefaultItemDecoration;
import com.llf.basemodel.recycleview.EndLessOnScrollListener;
import com.llf.basemodel.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by cgy
 * 2018/7/18  17:41
 *
 * 新闻头条
 */
public class NewsChildFragment extends BaseFragment implements NewsContract.View, SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private NewsAdapter adapter;
    private NewsContract.Presenter presenter;
    private int pageIndex = 0;
    private int type = NewsFragment.ONE;
    private List<NewsEntity> newsData = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();

    public static NewsChildFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        NewsChildFragment fragment = new NewsChildFragment();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getArguments().getInt("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_child;
    }

    @Override
    protected void initView() {
        presenter = new NewsPresenter(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager  = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DefaultItemDecoration(getActivity()));
        adapter = new NewsAdapter(newsData, getActivity());
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder viewHolder) {
                if (newsData.get(position).getImgextra() == null) {
                    NewsEntity entity = newsData.get(position);
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("news", entity);
                    startActivity(intent);
                } else {
                    images.clear();
                    for (int i = 0; i < newsData.get(position).getImgextra().size(); i++) {
                        images.add(newsData.get(position).getImgextra().get(i).getImgsrc());
                    }
                    BigImagePagerActivity.startImagePagerActivity(getActivity(), images, 0);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.addFooterView(R.layout.layout_footer);
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore() {
                pageIndex += Apis.PAGE_SIZE;
                if (newsData.size() != 0)
                    adapter.setFooterVisible(View.VISIBLE);
                    presenter.loadData(type, pageIndex);

            }
        });
    }

    @Override
    protected void lazyFetchData() {
        refreshLayout.setRefreshing(true);
        presenter.loadData(type, pageIndex);
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        presenter.loadData(type, pageIndex);
    }

    @Override
    public void returnData(List<NewsEntity> data) {
        if (pageIndex == 0) {
            try {
                adapter.replaceAll(data);
            } catch (Exception e) {
                LogUtil.d("数据为null");
            }
            refreshLayout.setRefreshing(false);
        } else {
            adapter.addAll(data);
            adapter.setFooterVisible(View.GONE);
        }
    }

    @Override
    public void showLoading() {
        startProgressDialog();
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {
        showErrorHint(msg);
        refreshLayout.setRefreshing(false);
    }

    public void slideToTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        newsData.clear();
        newsData = null;
        images.clear();
        images = null;
    }
}
