package com.cgy.common.module.mine.presenter;

import android.content.Context;

import com.cgy.common.data.JcodeDataSource;
import com.cgy.common.data.local.JcodeLocalDataSource;
import com.cgy.common.module.find.model.JcodeEntity;
import com.cgy.common.module.mine.contract.TrackContract;
import com.llf.basemodel.utils.LogUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cgy
 * 2018/7/20  10:49
 */
public class TrackPresenter implements TrackContract.Presenter{

    private TrackContract.View trackView;
    private JcodeLocalDataSource jcodeLocalDataSource;

    public TrackPresenter(TrackContract.View view) {
        this.trackView = view;
    }

    @Override
    public void loadData(Context context) {
        jcodeLocalDataSource = JcodeLocalDataSource.getInstance(context);
        Observable.create(new Observable.OnSubscribe<List<JcodeEntity>>() {
            @Override
            public void call(final Subscriber<? super List<JcodeEntity>> subscriber) {
                jcodeLocalDataSource.getJcodes(new JcodeDataSource.LoadJcodesCallback() {
                    @Override
                    public void onTasksLoaded(List<JcodeEntity> data) {
                        subscriber.onNext(data);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        subscriber.onError(new NullPointerException("数据为空"));
                    }
                });
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<JcodeEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(e.getMessage());
                        trackView.showErrorTip(e.getMessage());
                    }

                    @Override
                    public void onNext(List<JcodeEntity> jcodeEntities) {
                        trackView.returnData(jcodeEntities);
                    }
                });
    }

    @Override
    public void deleteData(Context context, final String title) {
        jcodeLocalDataSource = JcodeLocalDataSource.getInstance(context);
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(jcodeLocalDataSource.deleteJcode(title) == -1 ? false : true);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        trackView.showErrorTip(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean result) {
                        trackView.returnResult(result);
                    }
                });
    }

    @Override
    public void start() {

    }
}
