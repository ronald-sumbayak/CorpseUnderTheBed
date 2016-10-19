package ra.sumbayak.corpseunderthebed.datas.msg;

import java.io.Serializable;

public class CommentMessage extends Message implements Serializable {
    
    public CommentMessage (String room) {
        super (room);
    }
    
    @Override
    public String getMessageType () {
        return TYPE_COMMENT;
    }
    
    @Override
    public String getSender () {
        return "writer";
    }
    
    @Override
    public String getText () {
        return null;
    }
    
    @Override
    public String getTime () {
        return null;
    }
}
