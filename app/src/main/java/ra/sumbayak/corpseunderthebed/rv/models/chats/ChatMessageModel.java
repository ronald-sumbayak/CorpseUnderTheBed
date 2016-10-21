package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

public abstract class ChatMessageModel implements Serializable {
    
    public static int MESSAGE_TYPE_INFO = 0;
    public static int MESSAGE_TYPE_NORMAL = 1;
    public static int MESSAGE_TYPE_USER = 2;
    public static int MESSAGE_TYPE_NOTE = 3;
    public static int MESSAGE_TYPE_POST = 4;
    
    public abstract int getMessageType ();
}
