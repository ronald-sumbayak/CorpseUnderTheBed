package ra.sumbayak.corpseunderthebed.datas;

import android.content.Context;

import java.io.Serializable;
import java.util.*;

import ra.sumbayak.corpseunderthebed.datas.msg.ChosenChoices;
import ra.sumbayak.corpseunderthebed.datas.msg.Message;
import ra.sumbayak.corpseunderthebed.handlers.IOHandler;

public abstract class GameData implements Serializable {
    
    protected List<Message> mInbox = new ArrayList<> ();
    protected List<String> mChatList = new ArrayList<> ();
    protected List<String> mDeadFriend = new ArrayList<> ();
    protected Map<String, RoomData> mRoomData = new HashMap<> ();
    protected Map<String, String> mSavedChoices = new HashMap<> ();
    protected Stack<ChosenChoices> mActiveChoices = new Stack<> ();
    
    protected MessageData mMessageData;
    //protected int mDay = 0, mIndex = -1;
    protected int mDay = 2, mIndex = 80;
    protected String mUser = "Luna";
    protected transient Context mContext;
    protected transient OnDataHandled mPostman;
    
    public abstract void handleMessage (Context context);
    public abstract void handleChoices (Context context, int selection);
    public abstract void markMessageAsRead (String room);
    public abstract int getMessageInterval ();
    
    /* needed by ChatListRecyclerView. */
    public List<String> getChatList () {
        return mChatList;
    }
    
    protected void startNewSession (Context context) {
        mContext = context;
        mPostman = (OnDataHandled) context;
    }
    
    protected void saveTempGameData () {
        IOHandler io;
        io = new IOHandler (mContext);
        io.saveTempGameData (this);
    }
    
    protected void saveGameData () {
        IOHandler io;
        io = new IOHandler (mContext);
        io.saveGameData (this);
    }
    
    public int getIndex () {
        return mIndex;
    }
    
    public RoomData getRoomData (String room) {
        return mRoomData.get (room);
    }
    
    public Message getCurrentMessage () {
        return mMessageData.getMessageAt (mIndex);
    }
    
    public boolean isOnChoices () {
        for (Map.Entry<String, RoomData> roomData : mRoomData.entrySet ()) {
            if (roomData.getValue ().isOnChoices ()) {
                return true;
            }
        }
        
        return false;
    }
    
    public String getUser () {
        return mUser;
    }
    
    public interface OnDataHandled {
        // TODO construct this!
        void createNewAlarm (long interval);
        void notifyFrontEnd ();
        void pendingGameForNextDay (Calendar calendar);
    }
}
