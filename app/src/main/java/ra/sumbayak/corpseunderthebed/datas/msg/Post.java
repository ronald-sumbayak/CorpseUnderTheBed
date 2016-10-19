package ra.sumbayak.corpseunderthebed.datas.msg;

import java.io.Serializable;

public class Post implements Serializable {
    
    private String mAuthor, mDate, mTime, mText;
    
    public Post (String author, String date, String time, String text) {
        mAuthor = author;
        mDate = date;
        mText = text;
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
    
    public String getText () {
        return mText;
    }
}
