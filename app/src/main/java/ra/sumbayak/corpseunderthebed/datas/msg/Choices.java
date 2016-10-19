package ra.sumbayak.corpseunderthebed.datas.msg;

import java.io.Serializable;

public class Choices implements Serializable {
    
    private int mReplies;
    private String mText;
    
    public Choices (Integer replies, String text) {
        this.mReplies = replies;
        this.mText = text;
    }
    
    public String getText () {
        return mText;
    }
    
    public int getReplies () {
        return mReplies;
    }
}
