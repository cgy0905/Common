package com.cgy.common.module.find.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cgy.common.R;
import com.cgy.common.module.find.adapter.DiscoveryAdapter;
import com.cgy.common.module.find.contract.DiscoveryContract;
import com.cgy.common.module.find.model.JcodeEntity;
import com.cgy.common.module.find.presenter.DiscoveryPresenter;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.commonactivity.WebViewActivity;
import com.llf.basemodel.recycleview.DefaultItemDecoration;
import com.llf.basemodel.recycleview.EndLessOnScrollListener;
import com.llf.basemodel.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by cgy
 * 2018/7/18  17:03
 */
public class DiscoveryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, DiscoveryContract.View {

    private static final String TAG = "DiscoveryFragment";

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private DiscoveryAdapter adapter;

    private List<JcodeEntity> jcodes = new ArrayList<>();
    private DiscoveryPresenter presenter;


    private int pageIndex = 1;

    private static final String HOST = "http://www/jcodecraeer.com";

    public static DiscoveryFragment getInstance() {
        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        return discoveryFragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girl;
    }

    @Override
    protected void initView() {
        presenter = new DiscoveryPresenter(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DefaultItemDecoration(getActivity()));
        adapter = new DiscoveryAdapter(jcodes, getActivity());
        adapter.setOnItemClickListener(new DiscoveryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                presenter.addRecord(getActivity(), jcodes.get(position));
                WebViewActivity.lanuch(getActivity(), HOST + jcodes.get(position).getDetailUrl());
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.addFooterView(R.layout.layout_footer);
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore() {
                pageIndex++;
                if (jcodes.size() != 0)
                    adapter.setFooterVisible(View.VISIBLE);
                presenter.loadData("http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=1801&PageNo=" + pageIndex);

            }
        });
    }

    @OnClick(R.id.float_btn)
    public void onViewClicked() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    protected void lazyFetchData() {
        refreshLayout.setRefreshing(true);
        presenter.loadData("http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=1801&PageNo=" + pageIndex);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        presenter.loadData("http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=1801&PageNo=" + pageIndex);
    }

    @Override
    public void returnData(List<JcodeEntity> data) {
        if (pageIndex == 1) {
            adapter.replaceAll(data);
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
        LogUtil.d(TAG + msg);
        showErrorHint(msg);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        jcodes.clear();
        jcodes = null;
    }
}
