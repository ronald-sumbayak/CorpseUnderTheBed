package ra.sumbayak.corpseunderthebed.datas.messages;

public class CommentMessage extends Message implements java.io.Serializable {
    
    private String mText;
    
    public CommentMessage (String room, String author, String text) {
        super (room, author);
        mText = text;
    }
    
    public String text () {
        return mText;
    }
    
    @Override
    public String notificationMessage () {
        return "comment message from " + sender () + " lul :v";
    }
    
    @Override
    public String type () {
        return TYPE_COMMENT;
    }
}
