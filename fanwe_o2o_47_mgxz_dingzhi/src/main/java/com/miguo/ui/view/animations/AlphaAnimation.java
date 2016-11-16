package com.miguo.ui.view.animations;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;

/**
 * Created by didik on 2016/11/12.
 */

public class AlphaAnimation implements BaseAnimation {
    private final String TAG= getClass().getSimpleName();
    private float start=0f;
    private float end =1f;

    private AlphaAnimation() {
    }
    public AlphaAnimation(float start,float end) {
        if (start<0 || start>1 || end<0 || end >0){
            Log.d(TAG, "ScaleAnimation: is your params 0 ~ 1 ?");
        }else {
            this.start=start;
            this.end=end;
        }
    }

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", start, end)};
    }
}
