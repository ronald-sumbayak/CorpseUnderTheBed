package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.messages.Post;
import ra.sumbayak.corpseunderthebed.datas.messages.ShareMessage;

public class ShareMessageModel extends NormalMessageModel implements Serializable {
    
    private Post mPost;
    
    public ShareMessageModel (ShareMessage msg) {
        super (msg);
        mPost = msg.post ();
    }
    
    public Post post () {
        return mPost;
    }
    
    @Override
    public String text () {
        return sender () + " shared " + mPost.author () + "'s post.";
    }
    
    @Override
    public int type () {
        return TYPE_POST;
    }
}
