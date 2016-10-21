package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.RoomData;

public class DataHandler extends NotificationHandler implements Serializable {
    
    public DataHandler (Context context) {
        MessageDataParser.parseMessageData (context);
        buildRoomData (context);
        loadDay1 (context);
    }
    
    private void buildRoomData (Context context) {
        Resources resources = context.getResources ();
        String[] roomList = resources.getStringArray (R.array.room_list);
        
        for (String room : roomList) {
            mRoomData.put (room, new RoomData (room));
        }
    }
    
    private void loadDay1 (Context context) {
        FileIOHandler io;
        io = new FileIOHandler (context);
        mMessageData = io.loadMessageData (mDay);
    }
    
    @Override
    public void markMessageAsRead (String room) {
        RoomData roomData;
        roomData = mRoomData.get (room);
        
        int index;
        index = roomData.getMessageSize () - 1;
        while (!roomData.getMessageAt (index).isRead ()) {
            roomData.getMessageAt (index).setRead ();
        }
    }
    
    public boolean isOnChoices () {
        for (int x = 0; x < mChatList.size (); x++) {
            Log.d ("cutb_debug", "room: " + mChatList.get (x) + ", " + mRoomData.get (mChatList.get (x)).isOnChoices ());
            if (mRoomData.get (mChatList.get (x)).isOnChoices ()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int getMessageInterval () {
        return 2;
    }
}
