package ra.sumbayak.corpseunderthebed.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import ra.sumbayak.corpseunderthebed.runnables.FadingAnimation;
import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.fragments.ChatFragment;
import ra.sumbayak.corpseunderthebed.fragments.LaunchFragment;
import ra.sumbayak.corpseunderthebed.handlers.FileIOHandler;
import ra.sumbayak.corpseunderthebed.services.Postman;

public class MainActivity extends AppCompatActivity{
    
    private FragmentLink mFragmentLink;
    private GameData mGameData;
    private BroadcastReceiver mReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent intent) {
            onBroadcastReceived ();
        }
    };
    private IntentFilter mFilter = new IntentFilter ("cutb.MESSAGE");
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        Log.d ("cutb_debug", "at MainActivity.#onCreate");
        super.onCreate (savedInstanceState);
        setupLaunchActivities ();
        setupInitialViews ();
    }
    
    @Override
    protected void onResume () {
        Log.d ("cutb_debug", "at MainActivity.#onResume");
        super.onResume ();
        FileIOHandler io;
        io = new FileIOHandler (this);
        mGameData = io.loadGameData ();
        registerReceiver (mReceiver, mFilter);
    }
    
    @Override
    public void onAttachFragment (Fragment fragment) {
        Log.d ("cutb_debug", "at MainActivity.#onAttachFragment");
        super.onAttachFragment (fragment);
        mFragmentLink = (FragmentLink) fragment;
    }
    
    @Override
    protected void onPause () {
        Log.d ("cutb_debug", "at MainActivity.#onPause");
        super.onPause ();
        unregisterReceiver (mReceiver);
    }
    
    private void setupLaunchActivities () {
        Log.d ("cutb_debug", "at MainActivity.#setupLaunchActivities");
        addFragment (android.R.id.content, new LaunchFragment (), "WelcomeScreen");
        contactPostman ();
    }
    
    private void setupInitialViews () {
        Log.d ("cutb_debug", "at MainActivity.#setupInitialViews");
        setContentView (R.layout.activity_main);
        setToolbar ();
    }
    
    private void setToolbar () {
        Log.d ("cutb_debug", "at MainActivity.#setToolbar");
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        toolbar.setTitleTextColor (Color.parseColor ("#ffffff"));
        setSupportActionBar (toolbar);
    }
    
    private void contactPostman () {
        Log.d ("cutb_debug", "at MainActivity.#contactPostman");
        Intent intent;
        intent = new Intent (MainActivity.this, Postman.class);
        intent.setAction ("cutb.LAUNCHER");
        startService (intent);
    }
    
    public void addFragment (@IdRes int containerViewId, Fragment fragment, String tag) {
        Log.d ("cutb_debug", "at MainActivity.#addFragment");
        Log.i ("cutb_debug", "   To      : " + findViewById (containerViewId));
        Log.i ("cutb_debug", "   Fragment: " + fragment.getClass ().getSimpleName ());
        Log.i ("cutb_debug", "   Tag     : " + tag);
        FragmentTransaction ft;
        ft = getSupportFragmentManager ().beginTransaction ();
        ft.add (containerViewId, fragment, tag);
        ft.commit ();
    }
    
    public void replaceFragment (@IdRes int containerViewId, Fragment fragment, String tag) {
        Log.d ("cutb_debug", "at MainActivity.#replaceFragment");
        Log.i ("cutb_debug", "   To      : " + findViewById (containerViewId));
        Log.i ("cutb_debug", "   Fragment: " + fragment.getClass ().getSimpleName ());
        Log.i ("cutb_debug", "   Tag     : " + tag);
        FragmentTransaction ft;
        ft = getSupportFragmentManager ().beginTransaction ();
        ft.addToBackStack (null);
        ft.replace (containerViewId, fragment, tag);
        ft.commit ();
    }
    
    public GameData getGameData () {
        return mGameData;
    }
    
    private void onBroadcastReceived () {
        Log.d ("cutb_debug", "at MainActivity.#onBroadcastReceived");
        FileIOHandler io;
        io = new FileIOHandler (this);
        mGameData = io.loadGameData ();
        mFragmentLink.onBroadcastReceived ();
    }
    
    @Override
    public void onBackPressed () {
        Log.d ("cutb_debug", "at MainActivity.#onBackPressed");
        if (mFragmentLink instanceof ChatFragment) {
            FadingAnimation fadingAnimation;
            fadingAnimation = new FadingAnimation (this);
            fadingAnimation.setFadeInEnd (new FadingAnimation.FadeInAnimationEnd () {
                @Override
                public void onFadeAnimationEnd () {
                    MainActivity.super.onBackPressed ();
                }
            });
            fadingAnimation.run ();
        }
    }
    
    public void setFragmentLink (Fragment fragment) {
        mFragmentLink = (FragmentLink) fragment;
    }
    
    public interface FragmentLink {
        void onBroadcastReceived ();
    }
}