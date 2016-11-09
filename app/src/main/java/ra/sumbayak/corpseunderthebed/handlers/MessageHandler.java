package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.datas.MessageTime;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.datas.messages.*;
import ra.sumbayak.corpseunderthebed.datas.messages.ShareMessage;
import ra.sumbayak.corpseunderthebed.rv.models.NoteModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.*;

abstract class MessageHandler extends GameData implements Serializable {
    
    @Override
    public void handleMessage (Context context) {
        startNewSession (context);
        handleMessage ();
    }
    
    private void handleMessage () {
        if (mMessageData == null) return;
        if (!isOnChoices ()) {
            incrementIndex ();
            if (isEndOfDay ()) {
                if (mDay == 6) return;
                startNewDay ();
            }
            else handleMessage (currentMessage ());
        }
    }
    
    protected void handleMessage (Message msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage");
        Log.i ("cutb_debug", "   index: " + mIndex);
        switch (msg.type ()) {
            case Message.TYPE_NORMAL: handleMessage ((NormalMessage) msg); break;
            case Message.TYPE_CHOICES: handleMessage ((ChoicesMessage) msg); break;
            case Message.TYPE_NOTE: handleMessage ((NoteMessage) msg); break;
            case Message.TYPE_SHARE: handleMessage ((ShareMessage) msg); break;
            case Message.TYPE_INFO: handleMessage ((InfoMessage) msg); break;
            case Message.TYPE_COMMENT: handleMessage (mContext); break;
            default: break;
        }
    }
    
    private void handleMessage (NormalMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage_NormalMessage");
        Log.i ("cutb_debug", "   sender: " + msg.sender ());
        Log.i ("cutb_debug", "   text: " + msg.text ());
        elevateChatList (msg.room ());
        if (msg.sender ().equals (mUser)) handleUserMessage (msg);
        else handleNormalMessage (msg);
    }
    
    private void handleNormalMessage (NormalMessage msg) {
        NormalMessageModel newMessage;
        newMessage = new NormalMessageModel (msg);
        roomData (msg.room ()).addMessage (newMessage);
        
        // post-handleMessage
        mInbox.push (msg);
        saveTempData ();
        mPostman.createNewAlarm (getMessageInterval ());
    }
    
    private void handleUserMessage (NormalMessage msg) {
        UserMessageModel newMessage;
        newMessage = new UserMessageModel (msg, getReadCount (msg.room ()));
        roomData (msg.room ()).addMessage (newMessage);
        
        // post-handleMessage
        //mInbox.push (msg);
        saveGameData ();
        mPostman.notifyFrontEnd ();
        mPostman.createNewAlarm (getMessageInterval ());
    }
    
    private void handleMessage (ChoicesMessage msg) {
        elevateChatList (msg.room ());
        mRoomData.get (msg.room ()).setOnChoices (true);
        saveTempData ();
        mPostman.createNewAlarm (getMessageInterval ());
    }
    
    private void handleMessage (NoteMessage msg) {
        elevateChatList (msg.room ());
        NoteMessageModel newMessage = new NoteMessageModel (msg);
        NoteModel newNote = new NoteModel (msg, mMessageData.date ());
        newMessage.setNoteIndex (roomData (msg.room ()).notes ().size ());
        roomData (msg.room ()).addMessage (newMessage).addNote (newNote);
        
        // post-handleMessage
        mInbox.push (msg);
        saveTempData ();
        mPostman.createNewAlarm (getMessageInterval ());
    }
    
    private void handleMessage (ShareMessage msg) {
        elevateChatList (msg.room ());
        ShareMessageModel newMessage;
        newMessage = new ShareMessageModel (msg);
        roomData (msg.room ()).addMessage (newMessage);
        
        // post-handleMessage
        mInbox.push (msg);
        saveTempData ();
        mPostman.createNewAlarm (getMessageInterval ());
    }
    
    private void handleMessage (InfoMessage msg) {
        switch (msg.category ()) {
            case InfoMessage.CATEGORY_INVITATION: handleInfoMessage ((InvitationMessage) msg); break;
            default: break;
        }
        handleMessage ();
    }
    
    private void handleInfoMessage (InvitationMessage msg) {
        elevateChatList (msg.room ());
        mRoomData.get (msg.room ()).setAsGroup (msg.members ().size (), msg.members ());
        saveGameData ();
    }
    
    /* This is magic. Do not touch! */
    protected void incrementIndex () {
        Log.d ("cutb_debug", "at MessageHandler.#incrementIndex");
        Log.i ("cutb_debug", "   initial index: " + mIndex);
        while (!mActiveChoices.empty () && (mIndex == mActiveChoices.peek ().mEnd)) {
            mIndex = mActiveChoices.peek ().mMax;
            mActiveChoices.pop ();
        }
        
        mIndex++;
        Log.i ("cutb_debug", "   result index: " + mIndex);
    }
    
    private boolean isEndOfDay () {
        Log.d ("cutb_debug", "at MessageHandler.#isEndOfDay");
        Log.d ("cutb_debug", mIndex + " " + mMessageData);
        return mIndex == mMessageData.size ();
    }
    
    private void startNewDay () {
        loadNextDay ();
        removeEmptyDay ();
        putDateMessage ();
        //pendingGameForNextDay ();
        handleMessage ();
    }
    
    private void loadNextDay () {
        IOHandler io;
        io = new IOHandler (mContext);
        
        mIndex = -1;
        mDay = mDay + 1;
        mMessageData = io.loadMessageData (mDay);
    }
    
    private void removeEmptyDay () {
        for (Map.Entry<String, RoomData> roomDataEntry : mRoomData.entrySet ()) {
            RoomData roomData = roomDataEntry.getValue ();
            
            if (roomData.messageAt (-1).type () == ChatMessageModel.TYPE_INFO) {
                roomData.removeMessage (-1);
            }
        }
    }
    
    private void putDateMessage () {
        for (Map.Entry<String, RoomData> roomData : mRoomData.entrySet ()) {
            roomData.getValue ().addMessage (new InfoMessageModel (mMessageData.date ()));
        }
    }
    
    private void pendingGameForNextDay () {
        Log.d ("cutb_debug", "at MessageHandler.#pendingGameForNextDay");
        MessageTime time;
        Log.d ("cutb_debug", "   mMessageData: " + mMessageData);
        Log.d ("cutb_debug", "   mDay: " + mDay);
        time = ((NormalMessage) mMessageData.get (0)).time ();
        
        Calendar calendar;
        calendar = Calendar.getInstance ();
        calendar.set (Calendar.DATE, calendar.get (Calendar.DATE) + 1);
        calendar.set (Calendar.HOUR, time.hour ());
        calendar.set (Calendar.MINUTE, time.minute ());
        calendar.set (Calendar.AM_PM, time.period ());
        mPostman.pendingGameForNextDay (calendar);
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
    
    private int getReadCount (String room) {
        /* you are not expected to understand this */
        if (mRoomData.get (room).type () == RoomData.TYPE_GROUP) {
            List<String> members;
            members = mRoomData.get (room).members ();
            members.removeAll (mDeadFriends);
            return members.size ();
        }
        else {
            if (mDeadFriends.contains (room)) return 0;
            else return 1;
        }
    }
    
//
//                                    ####  ####  #####  ####
//                                     ####  ####  ####   #____
//                                                        [.  .]
//       __________    __________    __________    ________|  |_____.
//      |          |  |          |  |          |  |                  \
//      | []    [] |  | []    [] |  | []    [] |  | []           [*]  \
//      |__________|==|__________|==|__________|==|____________________)
//        0      0      0      0      0      0      0                0
//
//    let me know if the train moves...
}
