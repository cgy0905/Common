package com.cgy.common.module.mine.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.cgy.common.R;
import com.llf.basemodel.utils.GuiUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cgy
 * 2018/7/20  10:18
 */
public class AttentionActivity extends AppCompatActivity {


    @Bind(R.id.container)
    RelativeLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        ButterKnife.bind(this);

        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    animateRevealShow();
                }
            }
        });
    }

    //动画展示
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow() {
        GuiUtils.animateRevealShow(this, container, 0, R.color.colorPrimary,
                new GuiUtils.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {

                    }

                    @Override
                    public void onRevealShow() {

                    }
                });
    }

    //动画消失
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealHide() {
        GuiUtils.animateRevealHide(this, container, 0, R.color.colorPrimary,
                new GuiUtils.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {
                        defaultBackPressed();
                    }

                    @Override
                    public void onRevealShow() {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            animateRevealHide();
    }

    //默认回退
    private void defaultBackPressed(){
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
