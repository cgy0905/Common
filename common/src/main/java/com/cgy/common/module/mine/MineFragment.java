package com.cgy.common.module.mine;

import com.cgy.common.R;
import com.llf.basemodel.base.BaseFragment;

/**
 * Created by cgy
 * 2018/7/18  17:01
 */
public class MineFragment extends BaseFragment {

    public static MineFragment getInstance() {
        MineFragment mineFragment = new MineFragment();
        return mineFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyFetchData() {

    }
}
