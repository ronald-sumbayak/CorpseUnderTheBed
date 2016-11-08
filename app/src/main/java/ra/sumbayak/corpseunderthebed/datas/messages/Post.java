package ra.sumbayak.corpseunderthebed.datas.messages;

import java.io.Serializable;

public class Post implements Serializable {
    
    private final String mAuthor, mDate, mTime, mBody;
    
    public Post (String author, String date, String time, String body) {
        mAuthor = author;
        mDate = date;
        mBody = body;
        mTime = time;
    }
    
    public String author () {
        return mAuthor;
    }
    
    public String date () {
        return mDate;
    }
    
    public String time () {
        return mTime;
    }
    
    public String body () {
        return mBody;
    }
}
