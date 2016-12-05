package com.miguo.ui.view.animations;

import android.animation.Animator;
import android.view.View;

/**
 * Created by didik on 2016/11/12.
 */

public interface BaseAnimation {
    Animator[] getAnimators(View view);
}
