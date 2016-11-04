package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.PostMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.Post;

public class PostMessageModel extends NoteMessageModel implements Serializable {
    
    private Post mPost;
    
    public PostMessageModel (PostMessage msg) {
        super (msg);
        mPost = msg.getPost ();
    }
    
    public Post getPost () {
        return mPost;
    }
    
    @Override
    public int getMessageType () {
        return MESSAGE_TYPE_POST;
    }
}
