package ra.sumbayak.corpseunderthebed.datas.messages;

import java.io.Serializable;

public class Post implements Serializable {
    
    private String mAuthor, mDate, mTime, mBody;
    
    public Post (String author, String date, String time, String body) {
        mAuthor = author;
        mDate = date;
        mBody = body;
        mTime = time;
    }
    
    public String getAuthor () {
        return mAuthor;
    }
    
    public String getDate () {
        return mDate;
    }
    
    public String getTime () {
        return mTime;
    }
    
    public String getBody () {
        return mBody;
    }
}
