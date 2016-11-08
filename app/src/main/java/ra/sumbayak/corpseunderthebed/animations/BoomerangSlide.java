package ra.sumbayak.corpseunderthebed.animations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class BoomerangSlide implements Runnable {
    
    private View mTarget;
    private float mHeight;
    private int mSlideDuration;
    private int mIdleDuration;
    private AnimatorSet mSet = new AnimatorSet ();
    private boolean mIsRunning = false;
    
    public BoomerangSlide (View target, int slideDuration, int idleDuration) {
        mTarget = target;
        mSlideDuration = slideDuration;
        mIdleDuration = idleDuration;
    }
    
    public boolean isRunning () {
        return mIsRunning;
    }
    
    @Override
    public void run () {
        mTarget.setVisibility (View.VISIBLE);
        mSet.playSequentially (slideDown (), slideUp ());
        mIsRunning = true;
        mSet.start ();
    }
    
    private Animator slideDown () {
        Animator slideDown;
        slideDown = ObjectAnimator.ofFloat (mTarget, View.TRANSLATION_Y, -mTarget.getHeight (), 0f);
        slideDown.setDuration (mSlideDuration);
        return slideDown;
    }
    
    private Animator slideUp () {
        Animator slideUp;
        slideUp = ObjectAnimator.ofFloat (mTarget, View.TRANSLATION_Y, 0f, -mTarget.getHeight ());
        slideUp.setDuration (mSlideDuration);
        slideUp.setStartDelay (mIdleDuration);
        slideUp.addListener (new Animator.AnimatorListener () {
            @Override
            public void onAnimationStart (Animator animation) {
                
            }
    
            @Override
            public void onAnimationEnd (Animator animation) {
                mTarget.setVisibility (View.GONE);
                mIsRunning = false;
            }
    
            @Override
            public void onAnimationCancel (Animator animation) {
        
            }
    
            @Override
            public void onAnimationRepeat (Animator animation) {
        
            }
        });
        return slideUp;
    }
    
    private Animator enlarge () {
        Animator enlarge;
        enlarge = ObjectAnimator.ofFloat (mTarget, View.SCALE_X, 0.5f, 1f);
        enlarge.setDuration (mIdleDuration/2);
        return enlarge;
    }
}
