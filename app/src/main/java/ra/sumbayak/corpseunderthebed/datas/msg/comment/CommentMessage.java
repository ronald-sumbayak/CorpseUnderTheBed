package ra.sumbayak.corpseunderthebed.datas.msg.comment;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.msg.Message;

public class CommentMessage extends Message implements Serializable {
    
    public CommentMessage (String room) {
        super (room, "writer");
    }
    
    @Override
    public String getMessageType () {
        return TYPE_COMMENT;
    }
}
