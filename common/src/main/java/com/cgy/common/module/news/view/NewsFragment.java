package com.cgy.common.module.news.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.cgy.common.R;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.base.BaseFragmentAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cgy
 * 2018/7/18  16:59
 */
public class NewsFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    private static final String TAG = "NewsFragment";

    public static final int ONE = 0;
    public static final int TWO = 1;
    public static final int THREE = 2;
    public static final int FOUR = 3;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private String[] titles = getResources().getStringArray(R.array.childTitles);
    private BaseFragment[] fragments;
    private BaseFragmentAdapter adapter;
    private int currentPosition = 0;

    public static NewsFragment getInstance() {
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setArguments(new Bundle());
        return newsFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        fragments = new BaseFragment[4];
        fragments[0] = NewsChildFragment.newInstance(ONE);
        fragments[1] = NewsChildFragment.newInstance(TWO);
        fragments[2] = NewsChildFragment.newInstance(THREE);
        fragments[3] = NewsChildFragment.newInstance(FOUR);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        adapter = new BaseFragmentAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.toolbar)
    public void onViewClicked() {
        ((NewsChildFragment)fragments[currentPosition]).slideToTop();
    }
}
