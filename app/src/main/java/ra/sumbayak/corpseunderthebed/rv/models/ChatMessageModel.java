package ra.sumbayak.corpseunderthebed.rv.models;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.msg.NormalMessage;

public class ChatMessageModel implements Serializable {
    
    private Boolean mConsecutive = false, mRead;
    private Integer mReadCount;
    private String mSender, mText, mTime;
    
    public ChatMessageModel (NormalMessage msg) {
        mSender = msg.getSender ();
        mText = msg.getText ();
        mTime = msg.getTime ();
        mRead = false;
        mReadCount = 0;
    }
    
    public ChatMessageModel (String text, String time, Integer readCount) {
        mSender = "user";
        mText = text;
        mTime = time;
        mRead = true;
        mReadCount = readCount;
    }
    
    public Boolean isConsecutive () { return mConsecutive; }
    public Boolean isRead () { return mRead; }
    public Integer getReadCount () { return mReadCount; }
    public String getSender () { return mSender; }
    public String getText () { return mText; }
    public String getTime () { return mTime; }
    
    public void setConsecutive (Boolean consecutive) {
        mConsecutive = consecutive;
    }
    
    public void setRead () {
        mRead = true;
    }
}
