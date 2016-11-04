package ra.sumbayak.corpseunderthebed.datas;

import android.util.Log;

import java.io.Serializable;

public class MessageTime implements Serializable {
    
    private String mTime;
    
    public MessageTime (String time) {
        mTime = time;
    }
    
    public String getTimeAsString () {
        return mTime;
    }
    
    public int getTimeAsInteger () {
        return (getTimeHour () * 60) + getTimeMinute ();
    }
    
    public int getTimeHour () {
        Log.d ("cutb_debug", "mTime: " + mTime);
        return Integer.parseInt (mTime.split (":")[0]) + getTimePeriod ();
    }
    
    public int getTimeMinute () {
        Log.d ("cutb_debug", "bool: " + "ron :ld".split (" :")[1]);
        Log.d ("cutb_debug", "mTime: " + mTime);
        Log.d ("cutb_debug", "mTime[0]: " + mTime.split (" |:")[1]);
        return Integer.parseInt (mTime.split (" |:")[1]);
    }
    
    public int getTimePeriod () {
        return mTime.split (" ")[1].equals ("AM") ? 0 : 12;
    }
}
