package ra.sumbayak.corpseunderthebed.datas.messages;

import java.io.Serializable;

public class ShareMessage extends NormalMessage implements Serializable {
    
    private boolean mSaveAsNote;
    private Post mPost;
    
    public ShareMessage (String room, String sender, String time, Post post, boolean saveAsNote) {
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
