package com.cgy.common.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.cgy.common.MyApp;
import com.cgy.common.R;
import com.cgy.common.WelcomeActivity;
import com.cgy.common.module.find.view.DiscoveryFragment;
import com.cgy.common.module.mine.MineFragment;
import com.cgy.common.module.news.view.NewsFragment;
import com.cgy.common.module.video.VideoFragment;
import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.base.BaseFragmentAdapter;
import com.llf.basemodel.dialog.UpdateDialog;
import com.llf.basemodel.utils.AppInfoUtil;
import com.llf.basemodel.utils.FileUtil;
import com.llf.basemodel.utils.LogUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, MainContract.View {

    @Bind(R.id.btn_journalism)
    Button btnNews;
    @Bind(R.id.btn_video)
    Button btnVideo;
    @Bind(R.id.btn_girl)
    Button btnGirl;
    @Bind(R.id.btn_mine)
    Button btnMine;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private static final String TAG = "MainActivity";

    private String[] titles;
    private BaseFragment[] fragments;
    int currentTabPosition = 0;
    public static final String CURRENT_TAB_POSITION = "HOME_CURRENT_TAB_POSITION";
    private MainContract.Presenter presenter;

    static {
        //vector支持selector
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //是被强杀的
        if (MyApp.appStatus == -1) {
            startActivity(WelcomeActivity.class);
        }

        if (savedInstanceState != null) {
            currentTabPosition = savedInstanceState.getInt(CURRENT_TAB_POSITION);
            viewPager.setCurrentItem(currentTabPosition);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/xiuqu.apk");
        if (file.exists() && file.length() > 0) {
            FileUtil.deleteFile(file);
            showToast("安装包已删除");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        presenter = new MainPresenter(this);
        titles = getResources().getStringArray(R.array.main_titles);
        fragments = new BaseFragment[titles.length];
        fragments[0] = NewsFragment.getInstance();
        fragments[1] = VideoFragment.getInstance();
        fragments[2] = DiscoveryFragment.getInstance();
        fragments[3] = MineFragment.getInstance();
        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        btnNews.setSelected(true);
        presenter.checkUpdate("http://api.fir.im/apps/latest/58f87d50959d6904280005a3?api_token=9f2408863ff25abccca986e5d4d9d6ba");
    }

    /**
     * 存储瞬间的UI状态
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //奔溃前保存位置 方法执行在onStop之前
        outState.putInt(CURRENT_TAB_POSITION, currentTabPosition);
        super.onSaveInstanceState(outState);
    }

    /**
     * 这个方法在onStart()之后
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //仅当activity为task时才生效
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //分发到fragment的onActivityResult用于解决qq分享接收不到回调
        BaseFragment fragment = fragments[3];
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 用于存储持久化数据
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (level == TRIM_MEMORY_UI_HIDDEN) {
            //释放资源
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        resetTab();
        switch (position) {
            case 0:
                btnNews.setSelected(true);
                break;
            case 1:
                btnVideo.setSelected(true);
                break;
            case 2:
                btnGirl.setSelected(true);
                break;
            case 3:
                btnMine.setSelected(true);
                break;
            default:
                //其他
                break;
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick({R.id.btn_journalism, R.id.btn_girl, R.id.btn_video, R.id.btn_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_journalism:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.btn_video:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.btn_girl:
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.btn_mine:
                viewPager.setCurrentItem(3, false);
                break;

            default:
                break;
        }
    }

    private void resetTab() {
        btnNews.setSelected(false);
        btnVideo.setSelected(false);
        btnGirl.setSelected(false);
        btnMine.setSelected(false);
    }

    @Override
    public void returnResult(String result) {
        showToast(result);
    }

    @Override
    public void returnUpdateResult(final ApplicationEntity entity) {
        try {
            if (AppInfoUtil.getVersionCode(MyApp.instance) < Integer.parseInt(entity.getVersion())) {
                String content = String.format("最新版本：%1$s\napp名字：%2$s\n\n更新内容\n%3$s", entity.getVersionShort(), entity.getName(), entity.getChangelog());
                UpdateDialog.show(MainActivity.this, content, new UpdateDialog.OnUpdate() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void ok() {
                        presenter.update(entity);
                    }
                });
            }
        } catch (Exception e) {
            LogUtil.d(TAG + "数字转化出错");
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
}
