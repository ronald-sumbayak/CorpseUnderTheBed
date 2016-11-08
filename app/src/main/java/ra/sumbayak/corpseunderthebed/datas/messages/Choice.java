package ra.sumbayak.corpseunderthebed.datas.messages;

public class Choice implements java.io.Serializable {
    
    private int mReplies;
    private String mLabel;
    
    public Choice (int replies, String label) {
        mReplies = replies;
        mLabel = label;
    }
    
    public String label () {
        return mLabel;
    }
    
    public int replies () {
        return mReplies;
    }
}
