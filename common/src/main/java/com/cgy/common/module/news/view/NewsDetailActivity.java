package com.cgy.common.module.news.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.cgy.common.MyApp;
import com.cgy.common.R;
import com.cgy.common.WelcomeActivity;
import com.cgy.common.module.news.contract.NewsDetailContract;
import com.cgy.common.module.news.model.NewsEntity;
import com.cgy.common.module.news.presenter.NewsDetailPresenter;
import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.basemodel.utils.LogUtil;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.Bind;

/**
 * Created by cgy
 * 2018/7/18  18:15
 * <p>
 * 新闻详情
 */
public class NewsDetailActivity extends BaseActivity implements NewsDetailContract.View {

    private static final String TAG = "NewsDetailActivity";
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.htNewsContent)
    HtmlTextView htNewsContent;

    private NewsEntity entity;//详情数据

    private NewsDetailContract.Presenter presenter;

    public static void Launch(Activity context, BaseViewHolder holder, NewsEntity entity) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("news", entity);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                holder.getView(R.id.iv_news), context.getString(R.string.transition_news_img));
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

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
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initView() {
        presenter = new NewsDetailPresenter(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
        entity = (NewsEntity) getIntent().getSerializableExtra("news");
        collapsingToolbar.setTitle(entity.getTitle());
        ImageLoaderUtils.loadingImg(getApplicationContext(), ivImage, entity.getImgsrc());
        presenter.loadContent(entity.getDocid());
    }

    /**
     * 在singleTop模式下会回调
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void showContent(String s) {
        try {
            htNewsContent.setHtmlFromString(s, new HtmlTextView.LocalImageGetter());
        } catch (Exception e) {
            LogUtil.d("数据为空");
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
