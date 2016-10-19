package ra.sumbayak.corpseunderthebed.datas;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.*;

import ra.sumbayak.corpseunderthebed.datas.msg.ChosenChoices;
import ra.sumbayak.corpseunderthebed.datas.msg.Message;

public abstract class GameData implements Serializable {
    
    protected List<String> mChatList = new ArrayList<> ();
    protected List<String> mDeadFriend = new ArrayList<> ();
    protected List<Message> mChosenMessage = new ArrayList<> ();
    protected MessageData mMessageData;
    protected Map<String, RoomData> mRoomData = new HashMap<> ();
    protected Stack<ChosenChoices> mChosenChoices = new Stack<> ();
    
    protected Integer mDay = 1, mIndex = -1;
    protected transient OnMessageHandled mPostman;
    
    public abstract void handleMessage (Context context);
    public abstract void handleChoices (Context context, Integer selection);
    public abstract void markMessageAsRead (String room);
    
    public Boolean isOnChoices () {
        for (int x = 0; x < mChatList.size (); x++) {
            Log.d ("cutb_debug", "room: " + mChatList.get (x) + ", " + mRoomData.get (mChatList.get (x)).isOnChoices ());
            if (mRoomData.get (mChatList.get (x)).isOnChoices ()) {
                return true;
            }
        }
        return false;
    }
    
    public List<String> getChatList () {
        return mChatList;
    }
    
    public Map<String, RoomData> getRoomData () {
        return mRoomData;
    }
    
    public RoomData getRoomData (String room) {
        return mRoomData.get (room);
    }
    
    public MessageData getMessageData () {
        return mMessageData;
    }
    
    public int getIndex () {
        return mIndex;
    }
    
    public int getInterval () {
        return 3;
    }
    
    public interface OnMessageHandled {
        
    }
}
