package ra.sumbayak.corpseunderthebed.datas.msg.normal.choices;

import java.io.Serializable;

public class Choices implements Serializable {
    
    private int mReplies;
    private String mLabel;
    
    public Choices (int replies, String label) {
        mReplies = replies;
        mLabel = label;
    }
    
    public String getLabel () {
        return mLabel;
    }
    
    public int getReplies () {
        return mReplies;
    }
}
