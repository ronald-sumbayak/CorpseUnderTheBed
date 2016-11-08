package ra.sumbayak.corpseunderthebed.animations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public abstract class FadeOut implements Runnable {
    
    private View mTarget;
    private int mDuration;
    
    protected FadeOut (View target, int duration) {
        mTarget = target;
        mDuration = duration;
    }
    
    @Override
    public void run () {
        AnimatorSet set;
        set = new AnimatorSet ();
        set.play (buildFadeIn ());
        set.start ();
    }
    
    private Animator buildFadeIn () {
        Animator fadeIn;
        fadeIn = ObjectAnimator.ofFloat (mTarget, View.ALPHA, 1f, 0f);
        fadeIn.setDuration (mDuration);
        fadeIn.addListener (new Animator.AnimatorListener () {
            @Override
            public void onAnimationStart (Animator animation) {
                onFadeOutStart ();
            }
    
            @Override
            public void onAnimationEnd (Animator animation) {
                onFadeOutEnd ();
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
    
    public abstract void onFadeOutStart ();
    
    public abstract void onFadeOutEnd ();
}
