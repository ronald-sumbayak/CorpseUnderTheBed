package ra.sumbayak.corpseunderthebed.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.fragments.ChatMessageFragment;
import ra.sumbayak.corpseunderthebed.fragments.LaunchFragment;
import ra.sumbayak.corpseunderthebed.fragments.NoteFragment;
import ra.sumbayak.corpseunderthebed.runnables.FadingAnimation;
import ra.sumbayak.corpseunderthebed.services.Postman;

public class MainActivity extends AppCompatActivity{
    
    private FragmentLink mFragmentLink;
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
        registerReceiver (mReceiver, mFilter);
    }
    
//    @Override
//    public void onAttachFragment (Fragment fragment) {
//        Log.d ("cutb_debug", "at MainActivity.#onAttachFragment");
//        super.onAttachFragment (fragment);
//        mFragmentLink = (FragmentLink) fragment;
//    }
    
    @Override
    protected void onPause () {
        Log.d ("cutb_debug", "at MainActivity.#onPause");
        super.onPause ();
        unregisterReceiver (mReceiver);
    }
    
    private void setupLaunchActivities () {
        setLaunchFragment ();
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
        toolbar.setTitleTextColor (Color.parseColor ("#dddddd"));
        setSupportActionBar (toolbar);
    }
    
    private void setLaunchFragment () {
        FragmentTransaction ft;
        ft = getSupportFragmentManager ().beginTransaction ();
        ft.add (android.R.id.content, new LaunchFragment (), "WelcomeScreen");
        ft.commit ();
    }
    
    private void contactPostman () {
        Log.d ("cutb_debug", "at MainActivity.#contactPostman");
        Intent intent;
        intent = new Intent (this, Postman.class);
        intent.setAction ("cutb.LAUNCHER");
        startService (intent);
    }
    
    private void onBroadcastReceived () {
        Log.d ("cutb_debug", "at MainActivity.#onBroadcastReceived");
        mFragmentLink.onBroadcastReceived ();
    }
    
    @Override
    public void onBackPressed () {
        Log.d ("cutb_debug", "at MainActivity.#onBackPressed");
        if (mFragmentLink instanceof ChatMessageFragment || mFragmentLink instanceof NoteFragment) {
            FadingAnimation fadingAnimation;
            fadingAnimation = new FadingAnimation (this);
            fadingAnimation.setFadeInEnd (new FadingAnimation.FadeInAnimationEnd () {
                @Override
                public void onFadeAnimationEnd () {
                    MainActivity.super.onBackPressed ();
                    assert getSupportActionBar () != null;
                    getSupportActionBar ().setDisplayHomeAsUpEnabled (false);
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
    
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home: onBackPressed (); return true;
            default: return super.onOptionsItemSelected (item);
        }
    }
}