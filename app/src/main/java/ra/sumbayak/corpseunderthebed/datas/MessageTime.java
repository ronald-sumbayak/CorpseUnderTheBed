package ra.sumbayak.corpseunderthebed.datas;

import java.io.Serializable;
import java.util.Calendar;

public class MessageTime implements Serializable {
    
    private String mTime;
    
    public MessageTime (String time) {
        mTime = time;
    }
    
    public Calendar getTimeAsCalendar () {
        Calendar calendar;
        calendar = Calendar.getInstance ();
        calendar.set (Calendar.HOUR, getTimeHour ());
        calendar.set (Calendar.MINUTE, getTimeMinute ());
        calendar.set (Calendar.AM_PM, getTimePeriod ());
        return calendar;
    }
    
    public String getTimeAsString () {
        return mTime;
    }
    
    public int getTimeAsInteger () {
        return (getTimeHour () * 60) + getTimeMinute ();
    }
    
    private int getTimeHour () {
        return Integer.parseInt (mTime.split (":")[0]) + getTimePeriod ();
    }
    
    private int getTimeMinute () {
        return Integer.parseInt (mTime.split (": ")[1]);
    }
    
    private int getTimePeriod () {
        return mTime.split (" ")[1].equals ("AM") ? 0 : 12;
    }
}
