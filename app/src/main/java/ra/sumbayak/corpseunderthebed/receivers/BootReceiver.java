package ra.sumbayak.corpseunderthebed.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ra.sumbayak.corpseunderthebed.services.Postman;

public class BootReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive (Context context, Intent intent) {
        Log.d ("cutb_debug", "at BootReceiver.#onReceive");
        wakePostman (context);
    }
    
    private void wakePostman (Context context) {
        Log.d ("cutb_debug", "at BootReceiver.#wakePostman");
        Intent intent;
        intent = new Intent (context, Postman.class);
        intent.setAction ("cutb.BOOT");
        context.startService (intent);
    }
}
