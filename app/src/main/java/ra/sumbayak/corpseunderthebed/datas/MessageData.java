package ra.sumbayak.corpseunderthebed.datas;

import java.io.Serializable;
import java.util.List;

import ra.sumbayak.corpseunderthebed.datas.messages.Message;

public class MessageData implements Serializable {
    
    private List<Message> mMessages;
    private String mDate;
    
    public MessageData (String date, List<Message> messages) {
        mMessages = messages;
        mDate = date;
    }
    
    public String date () {
        return mDate;
    }
    
    public int size () {
        return mMessages.size ();
    }
    
    public Message get (int index) {
        return mMessages.get (index);
    }
}
