package com.example.hugo.custom_alert_view;

/**
 * Created by hugo on 28/03/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class CustomAlertView extends RelativeLayout {

    final String ALPHA = "#80000000";
    final String STOP_ALPHA = "#00000000";

    private int nbrButtons = 0;
    private LinearLayout mLinearLayout;
    private TextView mTextView;
    private OnAlertViewClickListener mClickListener;
    private OnAlertViewAnimationListener mAnimListener;
    private ViewSpecial mSpecialView;
    private boolean mIsHandledDispatchKey = true;

    public CustomAlertView(Context context) {
        super(context);
    }

    public CustomAlertView(Context context, int nbrButtons, String mainText,
                           String[] tab, boolean autoAddToBackground) {
        super(context);
        if (tab.length == nbrButtons) {
            this.init(context, nbrButtons, mainText, tab, autoAddToBackground);
        } else {
            throw (new ExceptionInInitializerError(
                    "H.F : Le nombre de boutons ne correspond pas aux nombres de textBoutton !!"));
        }
    }

    private void init(Context context, int nbrButtons, String mainText,
                      String[] tab, boolean autoAddToBackground) {


        for (int i = 0; i < tab.length; i++) {
            System.out.println("à l'index " + i + "il y a :: " + tab[i]);
        }

        System.out.println("le nombre de buttons est :: " + nbrButtons);
        System.out.println("le texte parincipal est :: " + mainText);

        this.setVisibility(GONE);
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setBackgroundResource(R.drawable.alert_box);
        int paddings = this.DpToPixel(context, 10);
        mLinearLayout.setPadding(paddings, this.DpToPixel(context, 110),
                paddings, paddings);
        LayoutParams mLinearLayout_params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int PxMarginValue = this.DpToPixel(context, 16);
        mLinearLayout_params.setMargins(PxMarginValue, 0, PxMarginValue, 0);
        mLinearLayout_params.addRule(RelativeLayout.CENTER_IN_PARENT);

        if (nbrButtons > 3) {
            this.nbrButtons = 3;
        } else {
            this.nbrButtons = nbrButtons;
        }

        mTextView = new TextView(context);
        TableLayout.LayoutParams mTextView_params = new TableLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        mTextView_params.setMargins(this.DpToPixel(context, 20), 0,
                this.DpToPixel(context, 20), this.DpToPixel(context, 5));

        mTextView.setText(mainText);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.parseColor("#42B3CB"));
        mTextView.setTextSize(17.0f);
        mLinearLayout.addView(mTextView, mTextView_params);

        LinearLayout buttonsContainer = new LinearLayout(context);
        buttonsContainer.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams buttonsContainer_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        mLinearLayout.addView(buttonsContainer, buttonsContainer_params);

        //for (int iButton = 0; iButton < this.nbrButtons; iButton++) {
        for (int iButton = this.nbrButtons; iButton > 0; iButton--) {
            Button cButton = new Button(context);
            cButton.setTag(iButton - 1);

            cButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onButtonClicked((Integer) v.getTag());
                        Log.i("boutton cliqué",
                                " " + (Integer) v.getTag());
                    }
                }
            });

            LinearLayout.LayoutParams cButton_params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

            int topButtonMargin = DpToPixel(context, 10);
            cButton_params.setMargins(0, topButtonMargin, 0, 0);
            cButton.setBackgroundResource(R.drawable.orange_button);
            cButton.setText(tab[(Integer) cButton.getTag()]);
            cButton.setGravity(Gravity.CENTER);
            cButton.setTextColor(Color.WHITE);

            buttonsContainer.addView(cButton, cButton_params);

            //if (iButton < this.nbrButtons - 1) {
            if (iButton > 1) {
                View view = new View(context);
                view.setVisibility(View.INVISIBLE);

                LinearLayout.LayoutParams invisibleView_params = new LinearLayout.LayoutParams(
                        0, 0, 0.10f);

                view.setLayoutParams(invisibleView_params);

                buttonsContainer.addView(view);
            }
        }

        RelativeLayout.LayoutParams rl_params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        this.setLayoutParams(rl_params);

        mSpecialView = new ViewSpecial(context);
        LayoutParams special_params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        this.addView(mSpecialView, special_params);
        this.addView(mLinearLayout, mLinearLayout_params);

        if (autoAddToBackground) {
            try {
                ViewGroup vg = (ViewGroup) ((Activity) context).getWindow().getDecorView();
                vg.addView(this);

            } catch (ExceptionInInitializerError e) {
                String errtext = "impossible de caster le contexte en activity, Ne pas utiliser autoAdd -> le mettre à false";
                new ExceptionInInitializerError(errtext);
            }

        }

    }


    private int DpToPixel(Context context, int dpMarginValue) {
        float d = context.getResources().getDisplayMetrics().density;
        int sizeInPx = (int) ((dpMarginValue * d) + 0.5f);
        return sizeInPx;
    }


    public void setOnAlertViewClickListener(OnAlertViewClickListener OnAlertViewClickListener) {
        mClickListener = OnAlertViewClickListener;
    }

    public void setOnAlertViewAnimationListener(OnAlertViewAnimationListener lister) {
        mAnimListener = lister;
    }

    // montrer le pop-up
    public void show(String animationName) {
        this.setVisibility(VISIBLE);
        CustomAlertView.this.setBackgroundColor(Color.parseColor(ALPHA));

//        Animation animation = AnimationFactory.getAnimation(animationName);
//        this.mLinearLayout.startAnimation(animation);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                Glider.glide(Skill.SineEaseIn, 4000, ObjectAnimator.ofFloat(mLinearLayout, "translationX", -300, 300))
                );

        set.setDuration(1200);
        set.start();

    }

    // faire disparaitre le po-pup, Si isAnimated == true alors il disparaitra
    // avec une animation "crash down"

    public void dismiss(boolean isAnimated) {
        if (isAnimated) {

            AnimationSet animationSet = new AnimationSet(true);
            AccelerateInterpolator ai = new AccelerateInterpolator(1.2f);
            animationSet.setInterpolator(ai);
            animationSet.setDuration(200);

            RotateAnimation rotate = new RotateAnimation(0, -30,
                    Animation.RELATIVE_TO_PARENT, 0.5f,
                    Animation.RELATIVE_TO_PARENT, 0.5f);
            rotate.setDuration(50);
            rotate.setStartOffset(30);
            rotate.setFillAfter(true);
            rotate.setFillEnabled(true);
            animationSet.addAnimation(rotate);

            TranslateAnimation transXY = new TranslateAnimation(0, 0, 0, 200);
            transXY.setStartOffset(40);
            animationSet.addAnimation(transXY);

            // ScaleAnimation scale = new ScaleAnimation(1.0f, 0.30f, 1.0f,
            // 0.30f, Animation.RELATIVE_TO_SELF, 0.5f,
            // Animation.RELATIVE_TO_SELF, 1f);
            ScaleAnimation scale = new ScaleAnimation(1.0f, 0.4f, 1.0f, 0.4f,
                    Animation.RELATIVE_TO_PARENT, 0.6f,
                    Animation.RELATIVE_TO_PARENT, 1f);
            scale.setStartOffset(30);
            animationSet.addAnimation(scale);

            this.mLinearLayout.startAnimation(animationSet);

            animationSet.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationEnd(Animation anim) {
                    mLinearLayout.setVisibility(GONE);
                    CustomAlertView.this.setBackgroundColor(Color
                            .parseColor(STOP_ALPHA));
                    if (mAnimListener != null) {
                        mAnimListener.onAnimationEnd();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });

        } else {
            Log.i("pas d'annimation", "isNaimated = " + isAnimated);
            this.setVisibility(GONE);
        }

        ((ViewGroup) mSpecialView.getParent()).removeView(mSpecialView);

    }

    public void setMainText(String text) {
        mTextView.setText(text);
    }

    public void setDispatchKey(boolean isHandledDispatchKey) {
        mIsHandledDispatchKey = isHandledDispatchKey;
    }

    @SuppressWarnings("deprecation")
    public void release() {
        this.mLinearLayout.setBackgroundDrawable(null);
        this.removeAllViews();
        ((ViewGroup) this.getParent()).removeView(this);
    }

    public void setButtonText(String text, int index) {
        try {
            Button b = (Button) findViewWithTag(index);
            b.setText(text);
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("le boutton existe t'il vraiment ?");
        }

    }

    class ViewSpecial extends View {

        public ViewSpecial(Context context) {
            super(context);
            this.setFocusableInTouchMode(true);
            this.setFocusable(true);
            this.requestFocus();
            this.requestFocusFromTouch();
            System.out.println("suis je focusabe ????" + this.isFocusable());
            System.out.println("suis je focusé ????" + this.isFocused());
            System.out.println("suis je focusabeee ????" + this.isFocusableInTouchMode());
        }

        @Override
//		permet de gérer les évenement "tuch" si return true
//		ici on gère :: si un événement tuch est relevé on ne fait rien (== avoid tuch)
        public boolean dispatchTouchEvent(MotionEvent e) {
            return true;
        }

        @Override
//			écoute les Key pressées
//		  	Si mIsHandledDispatchKey = true alors on gère ici les actions
//		 	note :: utiliser setDispatchKey(boolean)
//			ici on gère le back Key
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                System.out.println("I'm a key pressed , moreover I'M BACK_KEY!!!");
                if (CustomAlertView.this.getVisibility() == VISIBLE && CustomAlertView.this != null) {
                    CustomAlertView.this.dismiss(true);
                }
            }
            return mIsHandledDispatchKey;
        }

    }

    /**
     * ******************** INTERFACE ***************************
     */
    public interface OnAlertViewClickListener {
        public void onButtonClicked(int index);
    }

    public interface OnAlertViewAnimationListener {
        public void onAnimationEnd();
    }

}
