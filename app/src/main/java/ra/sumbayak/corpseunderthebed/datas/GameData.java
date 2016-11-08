package ra.sumbayak.corpseunderthebed.datas;

import android.app.Notification;
import android.content.Context;

import java.io.Serializable;
import java.util.*;

import ra.sumbayak.corpseunderthebed.datas.messages.ChosenChoices;
import ra.sumbayak.corpseunderthebed.datas.messages.Message;
import ra.sumbayak.corpseunderthebed.handlers.IOHandler;

public abstract class GameData implements Serializable {
    
    protected Stack<Message> mInbox = new Stack<> ();
    protected List<String> mChatList = new ArrayList<> ();
    protected List<String> mDeadFriends = new ArrayList<> ();
    protected Map<String, RoomData> mRoomData = new HashMap<> ();
    protected Map<String, String> mSavedChoices = new HashMap<> ();
    protected Stack<ChosenChoices> mActiveChoices = new Stack<> ();
    
    // protected int mDay = 0, mIndex = -1;
    protected int mDay = 0, mIndex = -1;
    protected String mUser = "Luna";
    protected MessageData mMessageData;
    
    protected transient Context mContext;
    protected transient OnDataHandled mPostman;
    
    public abstract void handleMessage (Context context);
    public abstract void handleChoices (Context context, int selection);
    public abstract String getInGameNotification ();
    public abstract Notification.Builder getStatusBarNotification (Context context);
    public abstract void markMessageAsRead (String room);
    public abstract int getMessageInterval ();
    
    /* needed by ChatListRecyclerView. */
    public List<String> chatList () {
        return mChatList;
    }
    
    public RoomData roomData (String room) {
        return mRoomData.get (room);
    }
    
    public Stack<Message> inbox () {
        return mInbox;
    }
    
    public Message currentMessage () {
        return mMessageData.get (mIndex);
    }
    
    public boolean isOnChoices () {
        for (Map.Entry<String, RoomData> roomData : mRoomData.entrySet ()) {
            if (roomData.getValue ().isOnChoices ()) {
                return true;
            }
        }
        
        return false;
    }
    
    protected void startNewSession (Context context) {
        mContext = context;
        mPostman = (OnDataHandled) context;
    }
    
    protected void saveTempData () {
        IOHandler io;
        io = new IOHandler (mContext);
        io.saveTempGameData (this);
    }
    
    protected void saveGameData () {
        IOHandler io;
        io = new IOHandler (mContext);
        io.saveGameData (this);
    }
    
    public interface OnDataHandled {
        // TODO construct this!
        void createNewAlarm (long interval);
        void notifyFrontEnd ();
        void pendingGameForNextDay (Calendar calendar);
    }
}
