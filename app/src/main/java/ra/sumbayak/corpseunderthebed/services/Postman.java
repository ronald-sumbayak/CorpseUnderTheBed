package ra.sumbayak.corpseunderthebed.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.handlers.DataHandler;
import ra.sumbayak.corpseunderthebed.handlers.IOHandler;
import ra.sumbayak.corpseunderthebed.receivers.AlarmReceiver;

public class Postman extends Service implements GameData.OnDataHandled {
    
    private IOHandler mIO;
    
    @Override
    public void onCreate () {
        super.onCreate ();
        mIO = new IOHandler (this);
    }
    
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
        if (!mIO.isGameDataExist ()) {
            GameData data;
            data = new DataHandler (this);
            mIO.saveGameData (data);
        }
    }
    
    private void verifyAlarm () {
        PendingIntent pendingIntent;
        pendingIntent = makePendingIntent (0);
        
        if (pendingIntent == null) createNewAlarm (3);
        else notifyFrontEnd ();
    }
    
    /**
     * Create new alarm that will goes off at specified time (in seconds).
     * @param s    time in seconds in which the alarm will be go off.
     */
    @Override
    public void createNewAlarm (long s) {
        Log.d ("cutb_debug", "at Postman.#createNewAlarm");
        PendingIntent pendingIntent;
        pendingIntent = makePendingIntent (1);
        long ms;
        ms = getAlarmClock (s);
        triggerAlarm (pendingIntent, ms);
    }
    
    private PendingIntent makePendingIntent (int mode) {
        int[] flags = {
            PendingIntent.FLAG_NO_CREATE,
            PendingIntent.FLAG_UPDATE_CURRENT
        };
        
        Intent intent;
        intent = new Intent (this, AlarmReceiver.class);
        intent.setAction ("cutb.ALARM");
        
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast (this, 611, intent, flags[mode]);
        return pendingIntent;
    }
    
    private long getAlarmClock (long s) {
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
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) getSystemService (ALARM_SERVICE);
        alarmManager.set (AlarmManager.RTC_WAKEUP, ms, pendingIntent);
    }
    
    private void processMessage () {
        Log.d ("cutb_debug", "at Postman.#processMessage");
        mIO.mergeGameData ();
        notifyFrontEnd ();
        
        GameData data;
        data = mIO.loadGameData ();
        data.handleMessage (this);
    }
    
    private void processChoices (int selection) {
        Log.d ("cutb_debug", "at Postman.#processChoices");
        mIO.mergeGameData ();
        
        GameData data;
        data = mIO.loadGameData ();
        data.handleChoices (this, selection);
    }
    
    @Override
    public void notifyFrontEnd () {
        Log.d ("cutb_debug", "at Postman.#notifyFrontEnd");
        Intent intent;
        intent = new Intent ("cutb.MESSAGE");
        sendBroadcast (intent);
    }
    
    @Override
    public void pendingGameForNextDay (Calendar calendar) {
        long s;
        s = calendar.getTimeInMillis () / 1000;
        createNewAlarm (s);
    }
    
    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }
}