package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.rv.models.chats.ChatMessageModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.NormalMessageModel;

public class DataHandler extends NotificationHandler implements Serializable {
    
    public DataHandler (Context context) {
        MessageDataParser parser;
        parser = MessageDataParser.getParser (context);
        parser.parseMessageData ();
        
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
        Log.d ("cutb_debug", "at DataHandler.#loadDay1");
        IOHandler io;
        io = new IOHandler (context);
        mMessageData = io.loadMessageData (mDay);
        Log.d ("cutb_debug", mDay + " " + mMessageData);
    }
    
    @Override
    public void markMessageAsRead (String room) {
        RoomData roomData;
        roomData = mRoomData.get (room);
        
        int index;
        index = roomData.messageSize ();
        while (--index >= 0) {
            if (roomData.messageAt (index).type () != ChatMessageModel.TYPE_INFO) {
                NormalMessageModel msg;
                msg = (NormalMessageModel) roomData.messageAt (index);
                
                if (!msg.isRead ()) msg.setRead ();
                else break;
            }
        }
    }
    
    @Override
    public int getMessageInterval () {
        return 2;
    }
    
    
}
