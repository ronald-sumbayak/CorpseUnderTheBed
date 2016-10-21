package ra.sumbayak.corpseunderthebed.rv.models;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.Post;

public class NoteModel implements Serializable {
    
    private String mAuthor, mDate, mTime, mBody;
    
    public NoteModel (Post post) {
        mAuthor = post.getAuthor ();
        mDate = post.getDate ();
        mTime = post.getTime ();
        mBody = post.getBody ();
    }
    
    public String getAuthor () { return mAuthor; }
    public String getDate () { return mDate; }
    public String getTime () { return mTime; }
    public String getBody () { return mBody; }
}
