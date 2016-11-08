package ra.sumbayak.corpseunderthebed.datas.messages;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.messages.Message;

public class CommentMessage extends Message implements Serializable {
    
    private String mText;
    
    public CommentMessage (String room, String writer, String text) {
        super (room, writer);
        mText = text;
    }
    
    @Override
    public String getMessageType () {
        return TYPE_COMMENT;
    }
    
    public String getText () {
        return mText;
    }
}
