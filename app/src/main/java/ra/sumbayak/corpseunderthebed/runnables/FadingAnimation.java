package ra.sumbayak.corpseunderthebed.runnables;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
import android.view.View;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;

public class FadingAnimation implements Runnable {
    
    private FadeInAnimationEnd mFadeInEnd;
    private View mFadeInterpolator;
    
    public FadingAnimation (MainActivity mainActivity) {
        mFadeInterpolator = mainActivity.findViewById (R.id.fade_interpolator);
    }
    
    @Override
    public void run () {
        AnimatorSet animatorSet;
        animatorSet = buildFadeAnimator ();
        animatorSet.start ();
    }
    
    private Animator makeFadeInAnimator () {
        Animator fadeIn;
        fadeIn = ObjectAnimator.ofFloat (mFadeInterpolator, View.ALPHA, 0f, 1f);
        fadeIn.setDuration (750);
        fadeIn.addListener (new Animator.AnimatorListener () {
            @Override
            public void onAnimationStart (Animator animation) {
            
            }
        
            @Override
            public void onAnimationEnd (Animator animation) {
                if (mFadeInEnd != null) {
                    mFadeInEnd.onFadeAnimationEnd ();
                }
            }
        
            @Override
            public void onAnimationCancel (Animator animation) {
            
            }
        
            @Override
            public void onAnimationRepeat (Animator animation) {
            
            }
        });
        return fadeIn;
    }
    
    private Animator makeFadeOutAnimator () {
        Animator fadeOut;
        fadeOut = ObjectAnimator.ofFloat (mFadeInterpolator, View.ALPHA, 1f, 0f);
        fadeOut.setDuration (750);
        return fadeOut;
    }
    
    private AnimatorSet buildFadeAnimator () {
        AnimatorSet fadeAnimator;
        fadeAnimator = new AnimatorSet ();
        fadeAnimator.playSequentially (makeFadeInAnimator (), makeFadeOutAnimator ());
        return fadeAnimator;
    }
    
    public void setFadeInEnd (@Nullable FadeInAnimationEnd fadeInEnd) {
        mFadeInEnd = fadeInEnd;
    }
    
    public interface FadeInAnimationEnd {
        void onFadeAnimationEnd ();
    }
}
