package com.cgy.common.module.girl;

import com.llf.basemodel.base.BaseFragment;

/**
 * Created by cgy
 * 2018/7/18  17:03
 */
public class GirlFragment extends BaseFragment {

    public static GirlFragment getInstance() {
        GirlFragment girlFragment = new GirlFragment();
        return girlFragment;
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
