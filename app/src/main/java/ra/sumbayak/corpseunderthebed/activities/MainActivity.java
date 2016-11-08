package ra.sumbayak.corpseunderthebed.activities;

import android.app.PendingIntent;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.animations.Fading;
import ra.sumbayak.corpseunderthebed.fragments.ChatListFragment;
import ra.sumbayak.corpseunderthebed.fragments.LaunchFragment;
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
        super.onCreate (savedInstanceState);
        setupInitialViews ();
        
        if (getIntent () != null && getIntent ().getAction ().equals ("cutb.STATUS_BAR")) {
            Intent intent;
            intent = new Intent (this, MainActivity.class);
            PendingIntent pendingIntent;
            pendingIntent = PendingIntent.getActivity (this, 777, intent, PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent == null) Log.d ("cutb_debug", "                           pending intent is null");
            else Log.d ("cutb_debug", "                         pending intent is not null");
            putMenuFragment ();
        }
        else
            setupLaunchActivities ();
    }
    
    private void putMenuFragment () {
        FragmentTransaction ft;
        ft = getSupportFragmentManager ().beginTransaction ();
        ft.add (R.id.fragment_view, new ChatListFragment (), "ChatList");
        ft.commit ();
    }
    
    @Override
    protected void onResume () {
        super.onResume ();
        Log.d ("cutb_debug", "game resumeddddddddsdsdsdsdsdsdsdssd");
        registerReceiver (mReceiver, mFilter);
        bindService (new Intent (this, Postman.class), mConnection, BIND_AUTO_CREATE);
    }
    
//    @Override
//    public void onAttachFragment (Fragment fragment) {
//        Log.d ("cutb_debug", "at MainActivity.#onAttachFragment");
//        super.onAttachFragment (fragment);
//        mFragmentLink = (FragmentLink) fragment;
//    }
    
    @Override
    protected void onPause () {
        super.onPause ();
        Log.d ("cutb_debug", "game paused/////////////////////////////////////");
        unbindService (mConnection);
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
        if (getSupportFragmentManager ().getBackStackEntryCount () > 0) {
            Fading fading;
            fading = new Fading (findViewById (R.id.fade_interpolator)) {
                @Override
                public void onFadeInEnd () {
                    getSupportFragmentManager ().popBackStack ();
                }
            };
            fading.run ();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp () {
        onBackPressed ();
        return true;
    }
    
    public void setFragmentLink (Fragment fragment) {
        mFragmentLink = (FragmentLink) fragment;
    }
    
//    private Postman mPostman;
    
    private ServiceConnection mConnection = new ServiceConnection () {
        @Override
        public void onServiceConnected (ComponentName componentName, IBinder iBinder) {
            Log.d ("cutb_debug", "service connecteeeeeeeeeeeeeeeeeeeeeeeeeeeeed");
//            mPostman = ((Postman.Binder) iBinder).getPostman ();
//            mPostman.reportInGame ();
        }
    
        @Override
        public void onServiceDisconnected (ComponentName componentName) {
            Log.d ("cutb_debug", "fffffffffffffuuuuuuuuuuuuuuuuuuuucccccccccccccccccccccccccckkkkkkkkkkkkkkkkkkk");
//            mPostman.reportLeaveGame ();
//            mPostman = null;
        }
    };
    
//    private class ServiceConnection implements android.content.ServiceConnection {
//    
//        @Override
//        public void onServiceConnected (ComponentName componentName, IBinder iBinder) {
//        
//        }
//    
//        @Override
//        public void onServiceDisconnected (ComponentName componentName) {
//            mPostman.reportLeaveGame ();
//            mPostman = null;
//        }
//    }
    
    public interface FragmentLink {
        void onBroadcastReceived ();
    }
}