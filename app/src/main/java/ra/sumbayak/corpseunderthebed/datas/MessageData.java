package ra.sumbayak.corpseunderthebed.datas;

import java.io.Serializable;
import java.util.List;

import ra.sumbayak.corpseunderthebed.datas.msg.Message;

public class MessageData implements Serializable {
    
    private List<Message> mMessages;
    private String mDate;
    
    public MessageData (String date, List<Message> messages) {
        mMessages = messages;
        mDate = date;
    }
    
    public String getDate () {
        return mDate;
    }
    
    public int size () {
        return mMessages.size ();
    }
    
    public Message getMessageAt (Integer index) {
        return mMessages.get (index);
    }
}
