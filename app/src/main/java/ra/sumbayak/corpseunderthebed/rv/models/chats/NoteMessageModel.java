package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.messages.NoteMessage;

public class NoteMessageModel extends NormalMessageModel implements Serializable {
    
    private String mAuthor, mBody;
    private int mNoteIndex;
    
    public NoteMessageModel (NoteMessage msg) {
        super (msg);
        mAuthor = msg.author ();
        mBody = msg.body ();
    }
    
    public String author () {
        return mAuthor;
    }
    
    public String body () {
        return mBody;
    }
    
    public void setNoteIndex (int index) {
        mNoteIndex = index;
    }
    
    public int noteIndex () {
        return mNoteIndex;
    }
    
    @Override
    public String text () {
        return sender () + " created a group note.";
    }
    
    @Override
    public int type () {
        return TYPE_NOTE;
    }
}
