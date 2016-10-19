package ra.sumbayak.corpseunderthebed.datas.msg;

import java.io.Serializable;

public abstract class Message implements Serializable {
    
    public final static String TYPE_NORMAL = "msg";
    public final static String TYPE_CHOICES = "choices";
    public final static String TYPE_NOTE = "note";
    public final static String TYPE_INFO = "info";
    public final static String TYPE_COMMENT = "comment";
    
    private String mRoom;
    
    public Message (String room) {
        this.mRoom = room;
    }
    
    public String getRoom () {
        return mRoom;
    }
    
    abstract public String getMessageType ();
    abstract public String getSender ();
    abstract public String getText ();
    abstract public String getTime ();
}
