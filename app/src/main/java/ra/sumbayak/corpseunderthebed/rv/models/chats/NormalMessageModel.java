package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.msg.normal.NormalMessage;

public class NormalMessageModel extends InfoMessageModel implements Serializable {
    
    private boolean mConsecutive, mRead = false;
    private String mSender, mTime;
    
    public NormalMessageModel (NormalMessage msg) {
        this (
            msg.getText (), msg.getSender (), msg.getTime ().getTimeAsString ()
        );
    }
    
    NormalMessageModel (String text, String sender, String time) {
        super (text);
        mSender = sender;
        mTime = time;
    }
    
    public Boolean isConsecutive () {
        return mConsecutive;
    }
    
    public Boolean isRead () {
        return mRead;
    }
    
    public String getSender () {
        return mSender;
    }
    
    public String getTime () {
        return mTime;
    }
    
    public void setConsecutive (Boolean consecutive) {
        mConsecutive = consecutive;
    }
    
    public void setRead () {
        mRead = true;
    }
    
    @Override
    public int getMessageType () {
        return MESSAGE_TYPE_NORMAL;
    }
}
