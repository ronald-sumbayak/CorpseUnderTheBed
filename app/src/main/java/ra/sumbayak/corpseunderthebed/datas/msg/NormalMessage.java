package ra.sumbayak.corpseunderthebed.datas.msg;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class NormalMessage extends Message implements Serializable {
    
    private String mSender, mTime, mText;
    
    public NormalMessage (String room, String sender, String time, String text) {
        super (room);
        mSender = sender;
        mTime = time;
        mText = text;
    }
    
    @Override
    public String getMessageType () {
        return TYPE_NORMAL;
    }
    
    @Override
    public String getSender () {
        return mSender;
    }
    
    @Override
    public String getText () {
        return mText;
    }
    
    @Override
    public String getTime () {
        return mTime;
    }
    
    public Integer getTimeAsInteger () {
        return (getTimeHour () * 60) + getTimeMinute ();
    }
    
    @NonNull
    private Integer getTimeHour () {
        return Integer.parseInt (mTime.split (":")[0]) + getTimePeriod ();
    }
    
    @NonNull
    private Integer getTimeMinute () {
        return Integer.parseInt (mTime.split (": ")[1]);
    }
    
    @NonNull
    private Integer getTimePeriod () {
        return mTime.split (" ")[1].equals ("AM") ? 0 : 12;
    }
}
