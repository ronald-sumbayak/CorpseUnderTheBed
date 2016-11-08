package ra.sumbayak.corpseunderthebed.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ra.sumbayak.corpseunderthebed.services.Postman;

/**
 * BroadcastReceiver that will receive timer from AlarmManager
 * for each messages.
 */
public class AlarmReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive (Context context, Intent intent) {
        deletePendingIntent (context);
        contactPostman (context);
    }
    
    private void contactPostman (Context context) {
        Intent intent;
        intent = new Intent (context, Postman.class);
        intent.setAction ("cutb.TIMER");
        context.startService (intent);
    }
    
    private void deletePendingIntent (Context context) {
        PendingIntent pendingIntent;
        pendingIntent = makePendingIntent (context);
        pendingIntent.cancel ();
    }
    
    private PendingIntent makePendingIntent (Context context) {
        Intent intent;
        intent = new Intent (context, AlarmReceiver.class);
        intent.setAction ("cutb.ALARM");
    
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast (context, 61197, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
