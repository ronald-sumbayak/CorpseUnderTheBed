package ra.sumbayak.corpseunderthebed.datas.msg;

import java.io.Serializable;

public class NoteMessage extends NormalMessage implements Serializable {
    
    private Integer mIndex;
    private Post mPost;
    
    public NoteMessage (String room, String sender, String time, Post post) {
        super (room, sender, time, null);
        mPost = post;
    }
    
    @Override
    public String getMessageType () {
        return TYPE_NOTE;
    }
}
