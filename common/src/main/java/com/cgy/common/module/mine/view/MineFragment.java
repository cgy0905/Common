package com.cgy.common.module.mine.view;

import android.view.View;
import android.widget.ImageView;

import com.cgy.common.R;
import com.cgy.common.constant.AppConfig;
import com.cgy.common.module.mine.contract.MineContract;
import com.cgy.common.module.mine.presenter.MinePresenter;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.utils.AppInfoUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by cgy
 * 2018/7/18  17:01
 */
public class MineFragment extends BaseFragment implements MineContract.View {

    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.iv_attention)
    ImageView ivAttention;
    @Bind(R.id.iv_track)
    ImageView ivTrack;
    @Bind(R.id.iv_share)
    ImageView ivShare;

    private static final int CHANNGE_AVATAR = 1;
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
                break;
            case R.id.ll_attention:
                break;
            case R.id.ll_track:
                break;
            case R.id.ll_share:
                break;
            case R.id.ll_night:
                break;
            case R.id.ll_service:
                break;
            case R.id.ll_update:
                break;
            case R.id.ll_replay:
                break;
            case R.id.iv_avatar:
                break;
            case R.id.rb_msg:
                break;
            default:
                break;
        }
    }

    @Override
    public void returnResult(String result) {

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }
}
