package ra.sumbayak.corpseunderthebed.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.animations.FadeOut;

public class LaunchFragment extends Fragment implements MainActivity.FragmentLink {
    
    private MainActivity mContext;
    
    @Override
    public void onAttach (Context context) {
        super.onAttach (context);
        mContext = (MainActivity) context;
        setFragmentLink ();
    }
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate (R.layout.launch_screen, container, false);
        return view;
    }
    
    @Override
    public void onBroadcastReceived () {
        removeWelcomeScreen ();
    }
    
    private void removeWelcomeScreen () {
        FadeOut fadeOut;
        fadeOut = new FadeOut (getView (), 1000) {
            @Override
            public void onFadeOutStart () {
                putMenuFragment ();
            }
    
            @Override
            public void onFadeOutEnd () {
                removeLaunchFragment ();
            }
        };
        fadeOut.run ();
    }
    
    private void putMenuFragment () {
        FragmentTransaction ft;
        ft = mContext.getSupportFragmentManager ().beginTransaction ();
        ft.add (R.id.fragment_view, new ChatListFragment (), "ChatList");
        ft.commit ();
    }
    
    private void removeLaunchFragment () {
        FragmentTransaction ft;
        ft = mContext.getSupportFragmentManager ().beginTransaction ();
        ft.remove (this);
        ft.commit ();
    }
    
    private void setFragmentLink () {
        mContext.setFragmentLink (this);
    }
}