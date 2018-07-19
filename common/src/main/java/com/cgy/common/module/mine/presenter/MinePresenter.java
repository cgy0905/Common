package com.cgy.common.module.mine.presenter;

import com.cgy.common.module.mine.contract.MineContract;

/**
 * Created by cgy
 * 2018/7/19  17:47
 */
public class MinePresenter implements MineContract.Presenter {

    private MineContract.View mineView;

    public MinePresenter(MineContract.View view) {
        this.mineView = view;
    }

    @Override
    public void checkUpdate(String url) {

    }

    @Override
    public void start() {

    }
}
