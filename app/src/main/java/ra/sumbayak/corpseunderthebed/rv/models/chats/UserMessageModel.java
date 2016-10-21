package ra.sumbayak.corpseunderthebed.rv.models.chats;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.msg.normal.NormalMessage;

public class UserMessageModel extends NormalMessageModel implements Serializable {
    
    private int mReadCount;
    
    public UserMessageModel (NormalMessage msg, int readCount) {
        super (msg.getText (), msg.getSender (), msg.getTime ());
        mReadCount = readCount;
        setRead ();
    }
    
    public int getReadCount () {
        return mReadCount;
    }
    
    @Override
    public int getMessageType () {
        return MESSAGE_TYPE_USER;
    }
}
