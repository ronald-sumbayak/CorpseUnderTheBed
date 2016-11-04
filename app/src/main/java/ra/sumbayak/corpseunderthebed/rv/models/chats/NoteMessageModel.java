package ra.sumbayak.corpseunderthebed.rv.models.chats;

import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.PostMessage;

public class NoteMessageModel extends NormalMessageModel {
    
    private String mAuthor;
    private int mNoteIndex;
    
    public NoteMessageModel (PostMessage msg) {
        super (msg.getText (), msg.getSender (), msg.getTime ().getTimeAsString ());
        mAuthor = msg.getPost ().getAuthor ();
    }
    
    public void setNoteIndex (int index) {
        mNoteIndex = index;
    }
    
    public String getAuthor () {
        return mAuthor;
    }
    
    @Override
    public int getMessageType () {
        return MESSAGE_TYPE_NOTE;
    }
    
    public int getNoteIndex () {
        return mNoteIndex;
    }
}
