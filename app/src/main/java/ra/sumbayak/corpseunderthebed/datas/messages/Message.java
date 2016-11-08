package ra.sumbayak.corpseunderthebed.datas.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    
    public static final String TYPE_NORMAL = "msg";
    public static final String TYPE_CHOICES = "choices";
    public static final String TYPE_NOTE = "note";
    public static final String TYPE_SHARE = "share";
    public static final String TYPE_INFO = "info";
    public static final String TYPE_COMMENT = "comment";
    
    private String mRoom, mSender;
    
    public Message (String room, String sender) {
        mRoom = room;
        mSender = sender;
    }
    
    public String getRoom () {
        return mRoom;
    }
    
    public String getSender () {
        return mSender;
    }
    
    abstract public String getMessageType ();
}
