package ra.sumbayak.corpseunderthebed.datas.messages;

public class ShareMessage extends NormalMessage implements java.io.Serializable {
    
    private final Post mPost;
    
    public ShareMessage (String room, String sender, String time, Post post) {
        super (room, sender, time, null);
        mPost = post;
    }
    
    public Post post () {
        return mPost;
    }
    
    @Override
    public String notificationMessage () {
        return sender () + " shared " + mPost.author () + "'s post.";
    }
    
    @Override
    public String type () {
        return TYPE_SHARE;
    }
}
