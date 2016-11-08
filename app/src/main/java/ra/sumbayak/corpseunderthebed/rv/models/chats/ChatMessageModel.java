package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

public abstract class ChatMessageModel implements Serializable {
    
    public static final int TYPE_INFO = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_USER = 2;
    public static final int TYPE_NOTE = 3;
    public static final int TYPE_POST = 4;
    
    private String mText;
    
    protected ChatMessageModel (String text) {
        mText = text;
    }
    
    public abstract int type ();
    
    public String text () {
        return mText;
    }
}
