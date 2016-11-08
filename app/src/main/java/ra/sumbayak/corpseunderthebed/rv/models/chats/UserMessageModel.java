package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.messages.NormalMessage;

public class UserMessageModel extends NormalMessageModel implements Serializable {
    
    private int mReadCount;
    
    public UserMessageModel (NormalMessage msg, int readCount) {
        super (msg);
        mReadCount = readCount;
        setRead ();
    }
    
    public int readCount () {
        return mReadCount;
    }
    
    @Override
    public int type () {
        return TYPE_USER;
    }
}
