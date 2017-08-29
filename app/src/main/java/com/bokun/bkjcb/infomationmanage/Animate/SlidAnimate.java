package com.bokun.bkjcb.infomationmanage.Animate;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.chad.library.adapter.base.animation.BaseAnimation;

/**
 * Created by DengShuai on 2017/8/28.
 */

public class SlidAnimate implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth() / 2, 0)};
    }
}
