package ra.sumbayak.corpseunderthebed.datas.messages;

public abstract class Message implements java.io.Serializable {
    
    public static final String TYPE_NORMAL = "msg";
    public static final String TYPE_CHOICES = "choices";
    public static final String TYPE_NOTE = "note";
    public static final String TYPE_SHARE = "share";
    public static final String TYPE_INFO = "info";
    public static final String TYPE_COMMENT = "comment";
    
    private final String mRoom, mSender;
    
    Message (String room, String sender) {
        mRoom = room;
        mSender = sender;
    }
    
    public String room () {
        return mRoom;
    }
    
    public String sender () {
        return mSender;
    }
    
    abstract public String notificationMessage ();
    
    abstract public String type ();
}
