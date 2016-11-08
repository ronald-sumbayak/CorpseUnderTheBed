package ra.sumbayak.corpseunderthebed.datas.messages;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.MessageTime;

public class NormalMessage extends Message implements Serializable {
    
    private final MessageTime mTime;
    private final String mText;
    
    public NormalMessage (String room, String sender, String time, String text) {
        super (room, sender);
        mTime = new MessageTime (time);
        mText = text;
    }
    
    public MessageTime time () {
        return mTime;
    }
    
    public String text () {
        return mText;
    }
    
    @Override
    public String notificationMessage () {
        return sender () + ": " + text ();
    }
    
    @Override
    public String type () {
        return TYPE_NORMAL;
    }
}
