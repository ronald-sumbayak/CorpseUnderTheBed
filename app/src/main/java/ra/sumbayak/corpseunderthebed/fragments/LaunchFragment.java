package ra.sumbayak.corpseunderthebed.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;

public class LaunchFragment extends Fragment implements MainActivity.FragmentLink {
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d ("cutb_debug", "at LaunchFragment.#onCreateView");
        View view;
        view = inflater.inflate (R.layout.launch_screen, container, false);
        return view;
    }
    
    @Override
    public void onBroadcastReceived () {
        Log.d ("cutb_debug", "at LaunchFragment.#onBroadcastReceived");
        removeWelcomeScreen ();
    }
    
    private void removeWelcomeScreen () {
        Log.d ("cutb_debug", "at LaunchFragment.#removeWelcomeScreen");
        Animator fadeIn;
        fadeIn = makeFadeInAnimator ();
        
        AnimatorSet animatorSet;
        animatorSet = buildAnimator ();
        animatorSet.play (fadeIn);
        animatorSet.start ();
    }
    
    /**
     * Make a new FadeIn {@link Animator} that fade in from alpha 1.0 to 0.0.
     * 
     * @return a FadeIn {@link Animator}.
     */
    private Animator makeFadeInAnimator () {
        Log.d ("cutb_debug", "at LaunchFragment.#makeFadeInAnimator");
        Animator fadeIn;
        fadeIn = ObjectAnimator.ofFloat (getView (), View.ALPHA, 1f, 0f);
        fadeIn.setDuration (1000);
        return fadeIn;
    }
    
    /**
     * Build a new {@link AnimatorSet} with a listener which add a {@link MenuFragment} and
     * remove this {@link LaunchFragment} attached to it.
     * 
     * @return the created {@link AnimatorSet}.
     */
    private AnimatorSet buildAnimator () {
        Log.d ("cutb_debug", "at LaunchFragment.#buildAnimator");
        AnimatorSet animatorSet;
        animatorSet = new AnimatorSet ();
        animatorSet.addListener (new Animator.AnimatorListener () {
            @Override
            public void onAnimationStart (Animator animation) {
                addMenuFragment ();
            }
    
            @Override
            public void onAnimationEnd (Animator animation) {
                removeLaunchFragment ();
            }
    
            @Override
            public void onAnimationCancel (Animator animation) {
        
            }
    
            @Override
            public void onAnimationRepeat (Animator animation) {
        
            }
        });
        return animatorSet;
    }
    
    private void addMenuFragment () {
        Log.d ("cutb_debug", "at LaunchFragment.#addMenuFragment");
        MenuFragment menuFragment;
        menuFragment = new MenuFragment ();
        
        MainActivity mainActivity;
        mainActivity = (MainActivity) getActivity ();
        mainActivity.addFragment (R.id.fragment_view, menuFragment, "ChatList");
    }
    
    private void removeLaunchFragment () {
        Log.d ("cutb_debug", "at LaunchFragment.#removeLaunchFragment");
        FragmentTransaction ft;
        ft = getActivity ().getSupportFragmentManager ().beginTransaction ();
        ft.remove (this);
        ft.commit ();
    }
    
    @Override
    public void onAttach (Context context) {
        super.onAttach (context);
        setFragmentLink ();
    }
    
    private void setFragmentLink () {
        ((MainActivity) getActivity ()).setFragmentLink (this);
    }
}