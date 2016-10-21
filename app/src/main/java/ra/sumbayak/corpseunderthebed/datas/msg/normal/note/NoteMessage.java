package ra.sumbayak.corpseunderthebed.datas.msg.normal.note;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.msg.normal.NormalMessage;

public class NoteMessage extends NormalMessage implements Serializable {
    
    private boolean mSaveAsNote;
    private Post mPost;
    
    public NoteMessage (String room, String sender, String time, Post post, boolean saveAsNote) {
        super (room, sender, time, null);
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
