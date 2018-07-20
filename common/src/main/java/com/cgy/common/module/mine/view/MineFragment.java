package com.cgy.common.module.mine.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;

import com.cgy.common.R;
import com.cgy.common.constant.AppConfig;
import com.cgy.common.module.mine.contract.MineContract;
import com.cgy.common.module.mine.presenter.MinePresenter;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.dialog.ShareDialog;
import com.llf.basemodel.utils.AppInfoUtil;
import com.llf.basemodel.utils.DateUtil;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.basemodel.utils.LogUtil;
import com.llf.photopicker.ImgSelConfig;
import com.llf.photopicker.PickPhotoActivity;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.tencent.mm.sdk.platformtools.Util.bmpToByteArray;

/**
 * Created by cgy
 * 2018/7/18  17:01
 */
public class MineFragment extends BaseFragment implements MineContract.View, IUiListener, ShareDialog.OneShare {

    private static final String TAG = "MineFragment";

    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.iv_attention)
    ImageView ivAttention;
    @Bind(R.id.iv_track)
    ImageView ivTrack;
    @Bind(R.id.iv_share)
    ImageView ivShare;

    private static final int CHANGE_AVATAR = 1;
    private MineContract.Presenter presenter;
    private Tencent tencent;
    private IWXAPI iwxapi;

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
        presenter = new MinePresenter(this);

        /**
         * 推荐位 根据服务器传入的图标名字 图标可动态配置
         */
        ivAttention.setImageResource(getResources().getIdentifier("ic_mine_attention", "drawable", AppInfoUtil.getPackageName(getActivity())));
        ivTrack.setImageResource(getResources().getIdentifier("ic_mine_track", "drawable", AppInfoUtil.getPackageName(getActivity())));
        ivShare.setImageResource(getResources().getIdentifier("ic_mine_share", "drawable", AppInfoUtil.getPackageName(getActivity())));
        tencent = Tencent.createInstance(AppConfig.APP_ID_QQ, getActivity());
        iwxapi = WXAPIFactory.createWXAPI(getActivity(), AppConfig.APP_ID_WEIXIN, false);
        iwxapi.registerApp(AppConfig.APP_ID_WEIXIN);
    }

    @Override
    protected void lazyFetchData() {

    }


    @OnClick({R.id.rb_setting, R.id.ll_attention, R.id.ll_track, R.id.ll_share, R.id.ll_night, R.id.ll_service, R.id.ll_update, R.id.ll_replay, R.id.iv_avatar, R.id.rb_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.ll_attention:
                startActivity(AttentionActivity.class);
                break;
            case R.id.ll_track:
                startActivity(TrackActivity.class);
                break;
            case R.id.ll_share:
                ShareDialog.show(getActivity(), this);
                break;
            case R.id.ll_night:
                int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (mode == Configuration.UI_MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (mode == Configuration.UI_MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                getActivity().recreate();
                break;
            case R.id.ll_service:
                showToast("客服中心");
                break;
            case R.id.ll_update:
                presenter.checkUpdate("http://api.fir.im/apps/latest/58f87d50959d6904280005a3?api_token=9f2408863ff25abccca986e5d4d9d6ba");
                break;
            case R.id.ll_replay:
                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR, DateUtil.getCurrentHour());
                intent.putExtra(AlarmClock.EXTRA_MINUTES, 0);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "设置计划");
                intent.putExtra(AlarmClock.EXTRA_VIBRATE, true);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.iv_avatar:
                PickPhotoActivity.startActivity(this, new ImgSelConfig.Builder().multiSelect(false).build(), CHANGE_AVATAR);
                break;
            case R.id.rb_msg:
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_AVATAR && resultCode == Activity.RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(PickPhotoActivity.INTENT_RESULT);
            if (result.size() != 0) {
                ImageLoaderUtils.displayCircle(getActivity(), ivAvatar, result.get(0));

                final File file = new File(result.get(0));
                Luban.get(getActivity())
                        .load(file)                     //传入要压缩的图片
                        .putGear(Luban.THIRD_GEAR)      //设定压缩档次,默认三档
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                LogUtil.d("压缩之前的图片大小" + file.length());
                            }

                            @Override
                            public void onSuccess(File file) {
                                LogUtil.d("压缩之后的图片大小" + file.length());
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.d("压缩出错了" + e.getMessage());
                            }
                        }).launch();    //启动压缩
            }
        }

        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    public void returnResult(String result) {
        LogUtil.d(TAG + result);
    }

    @Override
    public void onComplete(Object o) {
        showToast("QQ分享成功");
    }

    @Override
    public void onError(UiError uiError) {
        showToast("QQ分享出错" + uiError.errorMessage);
    }

    @Override
    public void onCancel() {
        showToast("QQ分享取消");
    }

    @Override
    public void weixinShare() {
        if (!AppInfoUtil.isWeixinAvilible(getActivity())) {
            showToast("请先安装微信");
            return;
        }

        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = "http://fir.im/6s7z";
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = "出大事了";
        msg.description = "这里有个好强大的app";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }


    @Override
    public void qqShare() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "出大事了");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "这里有个好强大的app");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "https://fir.im/6s7z");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://avatar.casn.net/B/0/1/1_new_one_object.jpg");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "秀趣");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        tencent.shareToQQ(getActivity(), params, this);

    }

    private String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
