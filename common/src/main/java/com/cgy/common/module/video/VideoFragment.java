package com.cgy.common.module.video;

import com.llf.basemodel.base.BaseFragment;

/**
 * Created by cgy
 * 2018/7/18  17:02
 */
public class VideoFragment extends BaseFragment {

    public static VideoFragment getInstance() {
        VideoFragment videoFragment = new VideoFragment();
        return videoFragment;
    }


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyFetchData() {

    }
}
