package ra.sumbayak.corpseunderthebed.datas;

import android.content.Context;

import java.io.Serializable;
import java.util.*;

import ra.sumbayak.corpseunderthebed.datas.msg.ChosenChoices;
import ra.sumbayak.corpseunderthebed.datas.msg.Message;

public abstract class GameData implements Serializable {
    
    protected List<Message> mInbox = new ArrayList<> ();
    protected List<String> mChatList = new ArrayList<> ();
    protected List<String> mDeadFriend = new ArrayList<> ();
    protected Map<String, RoomData> mRoomData = new HashMap<> ();
    protected Map<String, String> mSavedChoices = new HashMap<> ();
    protected Stack<ChosenChoices> mActiveChoices = new Stack<> ();
    
    protected MessageData mMessageData;
    protected int mDay = 1, mIndex = -1;
    protected String mUser = "User";
    protected transient OnMessageHandled mPostman;
    
    public abstract void handleMessage (Context context);
    public abstract void handleChoices (Context context, Integer selection);
    public abstract void markMessageAsRead (String room);
    public abstract boolean isOnChoices ();
    public abstract int getMessageInterval ();
    
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
    
    public Message getLastMessage () {
        return mMessageData.getMessageAt (mIndex);
    }
    
    public interface OnMessageHandled {
        
    }
}
