package ra.sumbayak.corpseunderthebed.datas.msg.normal.note;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.msg.normal.NormalMessage;

public class PostMessage extends NormalMessage implements Serializable {
    
    private boolean mSaveAsNote;
    private Post mPost;
    
    public PostMessage (String room, String sender, String time, Post post, boolean saveAsNote) {
        super (room, sender, time, post.getBody ());
        
        mPost = post;
        mSaveAsNote = saveAsNote;
    }
    
    public Post getPost () {
        return mPost;
    }
    
    public boolean isSaveAsNote () {
        return mSaveAsNote;
    }
    
    @Override
    public String getMessageType () {
        return TYPE_NOTE;
    }
}
