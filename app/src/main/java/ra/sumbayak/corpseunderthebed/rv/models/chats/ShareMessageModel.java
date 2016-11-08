package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.messages.ShareMessage;
import ra.sumbayak.corpseunderthebed.datas.messages.Post;

public class ShareMessageModel extends NoteMessageModel implements Serializable {
    
    private Post mPost;
    
    public ShareMessageModel (ShareMessage msg) {
        super (msg);
        mPost = msg.post ();
    }
    
    public Post getPost () {
        return mPost;
    }
    
    @Override
    public int type () {
        return TYPE_POST;
    }
}
