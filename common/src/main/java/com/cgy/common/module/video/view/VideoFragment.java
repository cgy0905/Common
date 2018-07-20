package com.cgy.common.module.video.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cgy.common.R;
import com.cgy.common.module.video.contract.VideoContract;
import com.cgy.common.module.video.model.VideoEntity;
import com.cgy.common.module.video.presenter.VideoPresenter;
import com.cgy.common.net.Apis;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.commonwidget.CircleImageView;
import com.llf.basemodel.recycleview.BaseAdapter;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.recycleview.EndLessOnScrollListener;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.basemodel.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by cgy
 * 2018/7/18  17:02
 *
 * 视频
 */
public class VideoFragment extends BaseFragment implements VideoContract.View, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "VideoFragment";
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private BaseAdapter adapter;
    private List<VideoEntity.V9LG4CHORBean> videos = new ArrayList<>();
    private int pageIndex = 0;
    private VideoContract.Presenter presenter;


    public static VideoFragment getInstance() {
        VideoFragment videoFragment = new VideoFragment();
        return videoFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        presenter = new VideoPresenter(this);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(this);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseAdapter<VideoEntity.V9LG4CHORBean>(getActivity(), R.layout.item_video, videos) {

            @Override
            public void convert(BaseViewHolder viewHolder, VideoEntity.V9LG4CHORBean item) {
                viewHolder.setText(R.id.tv_title, item.getTitle());
                viewHolder.setText(R.id.tv_source, item.getVideosource());
                ImageView imageView = viewHolder.getView(R.id.iv_cover);
                CircleImageView circleImageView = viewHolder.getView(R.id.cir_top_img);
                ImageLoaderUtils.loadingImg(getActivity(), imageView, item.getCover());
                ImageLoaderUtils.loadingImg(getActivity(), circleImageView, item.getTopicImg());
            }
        };
        adapter.setOnItemClickLitener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder holder) {
                if (videos.size() > 0) {
                    VideoDetailActivity.launch(getActivity(), videos.get(position).getMp4_url(), videos.get(position).getTitle(), pageIndex);
                }
            }

            @Override
            public void onItemLongClick(int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        adapter.addFooterView(R.layout.layout_footer);
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore() {
                pageIndex++;
                if (videos.size() != 0)
                    adapter.setFooterVisible(View.VISIBLE);
                presenter.loadData(Apis.HOST + Apis.Video + Apis.VIDEO_HOT_ID + "/n/" + pageIndex * 10 + "-10.html");
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
        presenter.loadData(Apis.HOST + Apis.Video + Apis.VIDEO_HOT_ID + "/n/" + pageIndex + "-10.html");
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        videos.clear();
        presenter.loadData(Apis.HOST + Apis.Video + Apis.VIDEO_HOT_ID + "/n/" + pageIndex + "-10.html");
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
        showErrorTip(msg);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void returnData(List<VideoEntity.V9LG4CHORBean> data) {
        if (pageIndex == 0) {
            adapter.replaceAll(data);
            refreshLayout.setRefreshing(false);
        } else {
            adapter.addAll(data);
            adapter.setFooterVisible(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videos.clear();
        videos = null;
    }
}
