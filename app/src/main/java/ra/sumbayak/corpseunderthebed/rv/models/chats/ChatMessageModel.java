package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

public abstract class ChatMessageModel implements Serializable {
    
    public static final int MESSAGE_TYPE_INFO = 0;
    public static final int MESSAGE_TYPE_NORMAL = 1;
    public static final int MESSAGE_TYPE_USER = 2;
    public static final int MESSAGE_TYPE_NOTE = 3;
    public static final int MESSAGE_TYPE_POST = 4;
    
    private String mText;
    
    protected ChatMessageModel (String text) {
        mText = text;
    }
    
    public abstract int getMessageType ();
    
    public String getText () {
        return mText;
    }
}
