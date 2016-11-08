package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.messages.NormalMessage;

public class NormalMessageModel extends InfoMessageModel implements Serializable {
    
    private boolean mConsecutive, mRead = false;
    private String mSender, mTime;
    
    public NormalMessageModel (NormalMessage msg) {
        super (msg.text ());
        mSender = msg.sender ();
        mTime = msg.time ().time ();
    }
    
    public String sender () {
        return mSender;
    }
    
    public String time () {
        return mTime;
    }
    
    public boolean isConsecutive () {
        return mConsecutive;
    }
    
    public boolean isRead () {
        return mRead;
    }
    
    public void setConsecutive (boolean consecutive) {
        mConsecutive = consecutive;
    }
    
    public void setRead () {
        mRead = true;
    }
    
    @Override
    public int type () {
        return TYPE_NORMAL;
    }
}
