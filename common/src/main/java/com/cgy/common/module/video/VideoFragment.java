package com.cgy.common.module.video;

import com.cgy.common.R;
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
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyFetchData() {

    }
}
