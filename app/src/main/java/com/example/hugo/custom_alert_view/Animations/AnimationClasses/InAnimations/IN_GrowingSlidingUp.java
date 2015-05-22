package com.example.hugo.custom_alert_view.Animations.AnimationClasses.InAnimations;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;


/**
 * Created by Sphinx117 on 22/05/2015.
 */
public class IN_GrowingSlidingUp extends ScaleAnimation {

    public IN_GrowingSlidingUp() {
        super(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 1.1f);
        this.setInterpolator(new AccelerateInterpolator(1.2f));
        this.setDuration(400);
    }

}
