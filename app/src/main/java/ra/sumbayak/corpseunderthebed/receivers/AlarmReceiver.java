package ra.sumbayak.corpseunderthebed.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ra.sumbayak.corpseunderthebed.services.Postman;

/**
 * BroadcastReceiver that will receive timer from AlarmManager
 * for each messages.
 */
public class AlarmReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive (Context context, Intent intent) {
        Log.d ("cutb_debug", "at AlarmReceiver.#onReceive");
        deletePendingIntent (context);
        contactPostman (context);
    }
    
    private void contactPostman (Context context) {
        Log.d ("cutb_debug", "at AlarmReceiver.#contactPostman");
        Intent intent;
        intent = new Intent (context, Postman.class);
        intent.setAction ("cutb.TIMER");
        context.startService (intent);
    }
    
    private void deletePendingIntent (Context context) {
        Log.d ("cutb_debug", "at AlarmReceiver.#deletePendingIntent");
        PendingIntent pendingIntent;
        pendingIntent = makePendingIntent (context);
        pendingIntent.cancel ();
    }
    
    private PendingIntent makePendingIntent (Context context) {
        Log.d ("cutb_debug", "at AlarmReceiver.#makePendingIntent");
        Intent intent;
        intent = new Intent (context, AlarmReceiver.class);
        intent.setAction ("cutb.ALARM");
    
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast (context, 611, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
