package com.cgy.common.module.mine.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.cgy.common.MyApp;
import com.cgy.common.R;
import com.cgy.common.WelcomeActivity;
import com.cgy.common.module.find.adapter.DiscoveryAdapter;
import com.cgy.common.module.find.model.JcodeEntity;
import com.cgy.common.module.mine.contract.TrackContract;
import com.cgy.common.module.mine.presenter.TrackPresenter;
import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.commonactivity.WebViewActivity;
import com.llf.basemodel.commonwidget.EmptyLayout;
import com.llf.basemodel.recycleview.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by cgy
 * 2018/7/20  10:36
 */
public class TrackActivity extends BaseActivity implements TrackContract.View, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.empty_layout)
    EmptyLayout emptyLayout;


    private DiscoveryAdapter adapter;
    private List<JcodeEntity> jcodes = new ArrayList<>();
    private ItemTouchHelper itemTouchHelper;
    private TrackPresenter presenter;
    private int position;
    private static final String HOST = "http://www.jcodecraeer.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //是被强杀的
        if (MyApp.appStatus == -1) {
            startActivity(WelcomeActivity.class);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_track;
    }

    @Override
    protected void initView() {
        presenter = new TrackPresenter(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                jcodes.add(toPosition, jcodes.remove(fromPosition));
                adapter.notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                position = viewHolder.getAdapterPosition();
                presenter.deleteData(TrackActivity.this, jcodes.get(position).getTitle());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DefaultItemDecoration(TrackActivity.this));
        adapter = new DiscoveryAdapter(jcodes, this);
        adapter.setOnItemClickListener(new DiscoveryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WebViewActivity.lanuch(TrackActivity.this, HOST + jcodes.get(position).getDetailUrl());
            }
        });
        recyclerView.setAdapter(adapter);
        refreshLayout.setRefreshing(true);
        presenter.loadData(this);
    }

    @OnClick(R.id.float_btn)
    public void onViewClicked() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        jcodes.clear();
        presenter.loadData(this);
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

    @Override
    public void returnData(List<JcodeEntity> data) {
        if (data.size() == 0) {
            emptyLayout.showEmpty();
        }

        refreshLayout.setRefreshing(false);
        adapter.replaceAll(data);
    }

    @Override
    public void returnResult(boolean result) {
        if (result) {
            jcodes.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        jcodes.clear();
        jcodes = null;
    }
}
