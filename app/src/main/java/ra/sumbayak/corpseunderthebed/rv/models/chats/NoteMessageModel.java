package ra.sumbayak.corpseunderthebed.rv.models.chats;

import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.NoteMessage;

public class NoteMessageModel extends NormalMessageModel {
    
    private String mAuthor;
    
    public NoteMessageModel (NoteMessage msg) {
        super (msg.getText (), msg.getSender (), msg.getTime ());
        mAuthor = msg.getPost ().getAuthor ();
    }
    
    public String getAuthor () {
        return mAuthor;
    }
}
