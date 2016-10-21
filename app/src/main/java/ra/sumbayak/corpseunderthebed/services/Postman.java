package ra.sumbayak.corpseunderthebed.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.handlers.DataHandler;
import ra.sumbayak.corpseunderthebed.handlers.FileIOHandler;
import ra.sumbayak.corpseunderthebed.receivers.AlarmReceiver;

public class Postman extends Service implements GameData.OnMessageHandled {
    
    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        Log.d ("cutb_debug", "at Postman.#onStartCommand");
        switch (intent.getAction ()) {
            case "cutb.LAUNCHER":
            case "cutb.BOOT": checkGameComponent (); break;
            case "cutb.TIMER": processMessage (); break;
            case "cutb.CHOICES": processChoices (intent.getIntExtra ("selection", -1)); break;
            default: break;
        }
        
        return START_REDELIVER_INTENT;
    }
    
    /**
     * check GameData to check first launch and make sure the alarm is set
     */
    private void checkGameComponent () {
        Log.d ("cutb_debug", "at Postman.#checkGameComponent");
        verifyGameData ();
        verifyAlarm ();
    }
    
    private void verifyGameData () {
        Log.d ("cutb_debug", "at Postman.#verifyGameData");
        FileIOHandler io;
        io = new FileIOHandler (this);
        
        if (!io.isGameDataExist ()) {
            GameData data;
            data = new DataHandler (this);
            io.saveGameData (data);
        }
    }
    
    private void verifyAlarm () {
        Log.d ("cutb_debug", "at Postman.#verifyGameData");
        PendingIntent pendingIntent;
        pendingIntent = makePendingIntent (PendingIntent.FLAG_NO_CREATE);
        
        if (pendingIntent == null) {
            createNewAlarm (3);
        }
    }
    
    private void createNewAlarm (int s) {
        Log.d ("cutb_debug", "at Postman.#createNewAlarm");
        PendingIntent pendingIntent;
        pendingIntent = makePendingIntent (PendingIntent.FLAG_UPDATE_CURRENT);
        long ms;
        ms = getAlarmClock (s);
        triggerAlarm (pendingIntent, ms);
    }
    
    private PendingIntent makePendingIntent (int flags) {
        Log.d ("cutb_debug", "at Postman.#makePendingIntent");
        Intent intent;
        intent = new Intent (this, AlarmReceiver.class);
        intent.setAction ("cutb.ALARM");
        
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast (this, 611, intent, flags);
        return pendingIntent;
    }
    
    private long getAlarmClock (int s) {
        Log.d ("cutb_debug", "at Postman.#getAlarmClock");
        long ms;
        ms = System.currentTimeMillis () + (s * 1000);
        return ms;
    }
    
    /**
     * trigger alarm manager with the specified pending intent at given time.
     * 
     * @param pendingIntent    PendingIntent to be used by the System. 
     * @param ms               time in which the alarm will be triggered (in milliseconds).
     */
    private void triggerAlarm (PendingIntent pendingIntent, long ms) {
        Log.d ("cutb_debug", "at Postman.#triggerAlarm");
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) getSystemService (ALARM_SERVICE);
        alarmManager.set (AlarmManager.RTC_WAKEUP, ms, pendingIntent);
    }
    
    private void notifyFrontEnd () {
        Log.d ("cutb_debug", "at Postman.#notifyFrontEnd");
        Intent intent;
        intent = new Intent ("cutb.MESSAGE");
        sendBroadcast (intent);
    }
    
    private void processMessage () {
        Log.d ("cutb_debug", "at Postman.#processMessage");
        FileIOHandler io;
        io = new FileIOHandler (this);
        
        GameData data;
        data = io.loadGameData ();
        data.handleMessage (this);
        
        // post-handleMessage
        io.saveGameData (data);
        if (!data.isOnChoices ()) createNewAlarm (data.getMessageInterval ());
        notifyFrontEnd ();
    }
    
    private void processChoices (int selection) {
        Log.d ("cutb_debug", "at Postman.#processChoices");
        FileIOHandler io;
        io = new FileIOHandler (this);
        
        GameData data;
        data = io.loadGameData ();
        data.handleChoices (this, selection);
        
        // post-handleChoices
        io.saveGameData (data);
        createNewAlarm (data.getMessageInterval ());
        notifyFrontEnd ();
    }
    
    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        Log.d ("cutb_debug", "at Postman.#onBind");
        return null;
    }
}
