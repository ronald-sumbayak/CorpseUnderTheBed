package ra.sumbayak.corpseunderthebed.applications;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

public class AppPortraitOrientationLocker extends Application {
    
    ActivityLifecycleCallbacks mCallbacks = new ActivityLifecycleCallbacks () {
        @Override
        public void onActivityCreated (Activity activity, Bundle bundle) {
            activity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    
        @Override
        public void onActivityStarted (Activity activity) {
        
        }
    
        @Override
        public void onActivityResumed (Activity activity) {
        
        }
    
        @Override
        public void onActivityPaused (Activity activity) {
        
        }
    
        @Override
        public void onActivityStopped (Activity activity) {
        
        }
    
        @Override
        public void onActivitySaveInstanceState (Activity activity, Bundle bundle) {
        
        }
    
        @Override
        public void onActivityDestroyed (Activity activity) {
        
        }
    };
    
    @Override
    public void onCreate () {
        super.onCreate ();
        registerActivityLifecycleCallbacks (mCallbacks);
    }
    
    public static void debug (String location) {
        Log.d ("cutb_debug", "----------------\n" + location + "\n" + "----------------");
    }
}
