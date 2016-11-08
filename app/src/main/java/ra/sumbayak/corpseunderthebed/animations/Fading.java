package ra.sumbayak.corpseunderthebed.animations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public abstract class Fading implements Runnable {
    
    private View mTarget;
    
    protected Fading (View target) {
        mTarget = target;
    }
    
    @Override
    public void run () {
        AnimatorSet animatorSet;
        animatorSet = buildFadeAnimator ();
        animatorSet.start ();
    }
    
    private Animator makeFadeInAnimator () {
        Animator fadeIn;
        fadeIn = ObjectAnimator.ofFloat (mTarget, View.ALPHA, 0f, 1f);
        fadeIn.setDuration (750);
        fadeIn.addListener (new Animator.AnimatorListener () {
            @Override
            public void onAnimationStart (Animator animation) {
            
            }
        
            @Override
            public void onAnimationEnd (Animator animation) {
                onFadeInEnd ();
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
        fadeOut = ObjectAnimator.ofFloat (mTarget, View.ALPHA, 1f, 0f);
        fadeOut.setDuration (750);
        return fadeOut;
    }
    
    private AnimatorSet buildFadeAnimator () {
        AnimatorSet fadeAnimator;
        fadeAnimator = new AnimatorSet ();
        fadeAnimator.playSequentially (makeFadeInAnimator (), makeFadeOutAnimator ());
        return fadeAnimator;
    }
    
    abstract public void onFadeInEnd (); 
}
