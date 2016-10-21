package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

public class InfoMessageModel extends ChatMessageModel implements Serializable {
    
    protected String mText;
    
    public InfoMessageModel (String text) {
        mText = text;
    }
    
    public String getText () {
        return mText;
    }
    
    @Override
    public int getMessageType () {
        return MESSAGE_TYPE_INFO;
    }
}
