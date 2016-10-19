package ra.sumbayak.corpseunderthebed.datas;

import java.io.Serializable;
import java.util.List;

import ra.sumbayak.corpseunderthebed.datas.msg.Message;

public class MessageData implements Serializable {
    
    private List<Message> mMessages;
    private MessageDate mDate;
    
    public MessageData (String date, List<Message> messages) {
        mMessages = messages;
        mDate = new MessageDate (date);
    }
    
    public MessageDate getDate () {
        return mDate;
    }
    
    public Integer getSize () {
        return mMessages.size ();
    }
    
    public List<Message> getMessages () {
        return mMessages;
    }
    
    public Message getMessageAt (Integer index) {
        return mMessages.get (index);
    }
}
