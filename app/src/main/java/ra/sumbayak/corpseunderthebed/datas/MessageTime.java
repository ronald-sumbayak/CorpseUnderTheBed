package ra.sumbayak.corpseunderthebed.datas;

import java.io.Serializable;

public class MessageTime implements Serializable {
    
    private String mTime;
    
    public MessageTime (String time) {
        mTime = time;
    }
    
    public String time () {
        return mTime;
    }
    
    public int hour () {
        return Integer.parseInt (mTime.split (":")[0]) + period ();
    }
    
    public int minute () {
        return Integer.parseInt (mTime.split (" |:")[1]);
    }
    
    public int period () {
        return mTime.split (" ")[1].equals ("AM") ? 0 : 12;
    }
}
