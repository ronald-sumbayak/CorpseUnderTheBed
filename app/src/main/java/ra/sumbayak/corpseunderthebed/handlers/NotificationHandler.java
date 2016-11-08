package ra.sumbayak.corpseunderthebed.handlers;

import android.app.Notification;
import android.content.Context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.datas.messages.Message;
import ra.sumbayak.corpseunderthebed.datas.messages.NormalMessage;

public abstract class NotificationHandler extends ChoicesHandler implements Serializable {
    
    public String getInGameNotification () {
        Message msg;
        msg = currentMessage ();
        
        switch (roomData (msg.room ()).type ()) {
            case RoomData.TYPE_GROUP: return "[" + msg.room () + "] " + msg.notificationMessage ();
            case RoomData.TYPE_PRIVATE: return msg.notificationMessage ();
            default: return null;
        }
    }
    
    @Override
    public Notification.Builder getStatusBarNotification (Context context) {
        if (!mInbox.empty ()) return buildNotification (context);
        else return null;
    }
    
    public Notification.Builder buildNotification (Context context) {
        Notification.InboxStyle inboxStyle;
        inboxStyle = new Notification.InboxStyle ();
        inboxStyle.setSummaryText ("+" + (mInbox.size () - 6) + " more");
        inboxStyle.setBigContentTitle (mInbox.size () + " New Messages");
    
        Map<String, Stack<String>> inbox;
        String roomList = "";
        inbox = new HashMap<> ();
        
        for (int i = mInbox.size ()-1; i >= (mInbox.size ()-6) && i >= 0; i--) {
            NormalMessage msg = (NormalMessage) mInbox.get (i);
            Stack<String> room = inbox.get (msg.room ());
            if (room == null) room = new Stack<> ();
            String text = msg.notificationMessage ();
//            if (text.length () > 35) {
//                text = text.substring (0, 35);
//                text = text.concat ("...");
//            }
            if (mRoomData.get (msg.room ()).type () == RoomData.TYPE_PRIVATE) {
                text = text.split (" ", 2)[1];
            }
            room.push (text);
            inbox.put (msg.room (), room);
        }
    
        for (Map.Entry<String, Stack<String>> ib : inbox.entrySet ()) {
            inboxStyle.addLine (ib.getKey ());
            roomList = roomList.concat (", " + ib.getKey ());
            Stack<String> msg = ib.getValue ();
            while (!msg.empty ()) {
                inboxStyle.addLine (msg.peek ());
                msg.pop ();
            }
        }
        
        Notification.Builder builder;
        builder = new Notification.Builder (context);
        builder.setContentTitle (mInbox.size () + " New Messages");
        builder.setContentText (roomList.substring (2, roomList.length ()));
        builder.setSmallIcon (R.drawable.ic_launcher);
        builder.setStyle (inboxStyle);
        return builder;
    }
}
