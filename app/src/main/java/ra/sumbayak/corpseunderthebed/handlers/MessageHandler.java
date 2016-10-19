package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.datas.msg.*;
import ra.sumbayak.corpseunderthebed.rv.models.ChatMessageModel;

public abstract class MessageHandler extends GameData implements Serializable {
    
    @Override
    public void handleMessage (Context context) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage");
        Log.d ("cutb_debug", "   index: " + mIndex);
        if (mIndex >= 0) {
            Log.d ("cutb_debug", "   msg: " + mMessageData.getMessages ().get (mIndex) + " " + isOnChoices ());
        }
        if (!isOnChoices ()) {
            incrementIndex (context);
            Message msg = mMessageData.getMessages ().get (mIndex);
            
            switch (msg.getMessageType ()) {
                case Message.TYPE_CHOICES: handleMessage ((ChoicesMessage) msg); break;
                case Message.TYPE_NORMAL: handleMessage ((NormalMessage) msg); break;
                case Message.TYPE_NOTE: handleMessage ((NoteMessage) msg); break;
                case Message.TYPE_INFO: handleMessage ((InfoMessage) msg); break;
                default: break;
            }
            
            //listener.onMessageHandled ();
        }
        saveProgress (context);
    }
    
    private void handleMessage (NormalMessage msg) {
        elevateChatList (msg.getRoom ());
        
        RoomData roomData;
        roomData = mRoomData.get (msg.getRoom ());
        
        ChatMessageModel newMessage;
        newMessage = new ChatMessageModel (msg);
        
        roomData.addNewMessage (newMessage);
        mChosenMessage.add (msg);
    }
    
    private void handleMessage (ChoicesMessage msg) {
        elevateChatList (msg.getRoom ());
        mRoomData.get (msg.getRoom ()).setOnChoices (true);
        Log.d ("cutb_debug", "RoomData: " + msg.getRoom () + ", " + mRoomData.get (msg.getRoom ()).isOnChoices ());
    }
    
    private void handleMessage (NoteMessage msg) {
        elevateChatList (msg.getRoom ());
    }
    
    private void handleMessage (InfoMessage msg) {
        switch (msg.getInfoCategory ()) {
            case InfoMessage.CATEGORY_INVITATION: handleInfo ((InvitationMessage) msg); break;
            default: break;
        }
    }
    
    private void handleInfo (InvitationMessage msg) {
        elevateChatList (msg.getRoom ());
        Log.d ("cutb_debug", msg.getRoom ());
        Log.d ("cutb_debug", mRoomData.get (msg.getRoom ()) + "");
        Log.d ("cutb_debug", msg.getMemberCount () + " " + msg.getMemberList ());
        mRoomData.get (msg.getRoom ()).setRoomAsGroup (msg.getMemberCount (), msg.getMemberList ());
    }
    
    private void incrementIndex (Context context) {
        while (!mChosenChoices.empty () && mIndex.equals (mChosenChoices.peek ().end)) {
            mIndex = mChosenChoices.peek ().max;
            mChosenChoices.pop ();
        }
        
        if (mIndex == mMessageData.getSize () - 1) {
            FileIOHandler io;
            io = new FileIOHandler (context);
            mMessageData = io.loadMessageData (++mDay);
            mIndex = -1;
        }
        
        mIndex++;
    }
    
    private void elevateChatList (String room) {
        if (!mChatList.contains (room)) {
            mChatList.add (room);
        }
        
        for (int x = mChatList.indexOf (room); x > 0; x--) {
            String temp = mChatList.get (x);
            mChatList.set (x, mChatList.get (x - 1));
            mChatList.set (x - 1, temp);
        }
    }
    
    private void saveProgress (Context context) {
        FileIOHandler io;
        io = new FileIOHandler (context);
        io.saveGameData (this);
    }
}
