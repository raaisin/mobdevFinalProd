package com.example.mobdevfinalprod.helperclasses;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationClass {
    private static Animation animation;
    public static Animation addSlideFromLeftAnimation() {
        animation = new TranslateAnimation(-1000f,0f,0f,0f);
        animation.setDuration(800);
        return animation;
    }
    public static Animation addSlideFromRightAnimation() {
        animation = new TranslateAnimation(1000f,0f,0f,0f);
        animation.setDuration(800);
        return animation;
    }
    public static Animation addSlideFromBottomAnimation() {
        animation = new TranslateAnimation(0f,0f,1000f,0f);
        animation.setDuration(800);
        return animation;
    }
    public static Animation addSlideFromTopAnimation() {
        animation = new TranslateAnimation(0f,0f,-1000f,0f);
        animation.setDuration(800);
        return animation;
    }
    public static Animation addFadeInAnimation() {
        animation = new AlphaAnimation(0,1);
        animation.setDuration(800);
        return animation;
    }
    public static Animation addFadeOutAnimation() {
        animation = new AlphaAnimation(1,0);
        animation.setDuration(800);
        return animation;
    }
    public static Animation fromCenterToTop(){
        animation = new TranslateAnimation(0f,0f,0f,-1000f);
        animation.setDuration(800);
        return animation;
    }

    public static Animation fromCenterToBottom(){
        animation = new TranslateAnimation(0f,0f,0f,1000f);
        animation.setDuration(800);
        return animation;
    }
}
