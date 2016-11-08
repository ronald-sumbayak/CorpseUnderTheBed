package ra.sumbayak.corpseunderthebed.datas.messages;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.MessageTime;
import ra.sumbayak.corpseunderthebed.datas.messages.Message;

public class NormalMessage extends Message implements Serializable {
    
    private String mText;
    private MessageTime mTime;
    
    public NormalMessage (String room, String sender, String time, String text) {
        super (room, sender);
        mTime = new MessageTime (time);
        mText = text;
    }
    
    public MessageTime getTime () {
        return mTime;
    }
    
    public String getText () {
        return mText;
    }
    
    @Override
    public String getMessageType () {
        return TYPE_NORMAL;
    }
}
