package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

public class InfoMessageModel extends ChatMessageModel implements Serializable {
    
    public InfoMessageModel (String text) {
        super (text);
    }
    
    @Override
    public int getMessageType () {
        return MESSAGE_TYPE_INFO;
    }
}
