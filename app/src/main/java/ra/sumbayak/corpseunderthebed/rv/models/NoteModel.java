package ra.sumbayak.corpseunderthebed.rv.models;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.messages.NoteMessage;

public class NoteModel implements Serializable {
    
    private String mAuthor, mDate, mTime, mBody;
    
    public NoteModel (NoteMessage msg, String date) {
        mAuthor = msg.author ();
        mTime = msg.time ().time ();
        mBody = msg.body ();
        mDate = date;
    }
    
    public String getAuthor () { return mAuthor; }
    public String getDate () { return mDate; }
    public String getTime () { return mTime; }
    public String getBody () { return mBody; }
}
