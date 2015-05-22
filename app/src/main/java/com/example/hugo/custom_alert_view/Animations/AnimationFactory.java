package com.example.hugo.custom_alert_view.Animations;

import android.util.Log;
import android.view.animation.Animation;

import com.example.hugo.custom_alert_view.Animations.AnimationClasses.InAnimations.IN_GrowingSlidingUp;
import com.example.hugo.custom_alert_view.Animations.AnimationClasses.InAnimations.None;

/**
 * Created by CJK on 22/05/2015.
 */
public class AnimationFactory {

    public static Animation getAnimation(String animationName) {

        Animation animation = null;
        switch (animationName) {
            case "IN_GrowingSlidingUp":
                //return new IN_GrowingSlidingUp();
                animation = new IN_GrowingSlidingUp();
                break;

            case "NONE":
                animation = new None();
                break;
        }


        return animation;
    }
}
