package ra.sumbayak.corpseunderthebed.datas.messages;

public class NoteMessage extends NormalMessage implements java.io.Serializable {
    
    private final String mAuthor, mBody;
    
    public NoteMessage (String room, String sender, String time, String author, String body) {
        super (room, sender, time, null);
        mAuthor = author;
        mBody = body;
    }
    
    public String author () {
        return mAuthor;
    }
    
    public String body () {
        return mBody;
    }
    
    @Override
    public String notificationMessage () {
        return sender () + " created a note.";
    }
    
    @Override
    public String type () {
        return TYPE_NOTE;
    }
}
