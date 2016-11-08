package ra.sumbayak.corpseunderthebed.animations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import static android.R.attr.duration;

public class BoomerangSlide implements Runnable {
    
    private View mTarget;
    private int mSlideDuration;
    private int mIdleDuration;
    
    public BoomerangSlide (View target, int slideDuration, int idleDuration) {
        mTarget = target;
        mSlideDuration = slideDuration;
        mIdleDuration = idleDuration;
    }
    
    @Override
    public void run () {
        AnimatorSet set;
        set = new AnimatorSet ();
        set.playSequentially (slideDown (), slideUp ());
    }
    
    private Animator slideDown () {
        
    }
    
    private Animator slideUp () {
        Animator slideUp;
        slideUp = ObjectAnimator.ofFloat (mTarget, View.TRANSLATION_Y, 0f, 1f);
        slideUp.setDuration (mSlideDuration);
        slideUp.setStartDelay (mIdleDuration);
        return slideUp;
    }
}
