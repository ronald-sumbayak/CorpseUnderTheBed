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
import ra.sumbayak.corpseunderthebed.datas.msg.Message;
import ra.sumbayak.corpseunderthebed.datas.msg.info.InfoMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.info.InvitationMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.NormalMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.choices.ChoicesMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.PostMessage;
import ra.sumbayak.corpseunderthebed.rv.models.NoteModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.*;

abstract class MessageHandler extends GameData implements Serializable {
    
    @Override
    public void handleMessage (Context context) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage (Context)");
        Log.d ("cutb_debug", "   onChoices: " + isOnChoices ());
        startNewSession (context);
        handleMessage ();
    }
    
    private void handleMessage () {
        if (!isOnChoices ()) {
            incrementIndex ();
    
            if (isEndOfDay ()) startNewDay ();
            else handleMessage (getCurrentMessage ());
        }
    }
    
    private void handleMessage (Message msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage");
        Log.d ("cutb_debug", "   index: " + mIndex);
        switch (msg.getMessageType ()) {
            case Message.TYPE_NORMAL: handleMessage ((NormalMessage) msg); break;
            case Message.TYPE_CHOICES: handleMessage ((ChoicesMessage) msg); break;
            case Message.TYPE_NOTE: handleMessage ((PostMessage) msg); break;
            case Message.TYPE_INFO: handleMessage ((InfoMessage) msg); break;
            case Message.TYPE_COMMENT: handleMessage (mContext); break;
            default: break;
        }
    }
    
    private void handleMessage (NormalMessage msg) {
        //Log.d ("cutb_debug", "at MessageHandler.#handleMessage (NormalMessage)");
        elevateChatList (msg.getRoom ());
        Log.d ("cutb_debug", "   Text   : " + msg.getText ());
        Log.d ("cutb_debug", "   Sender : " + msg.getSender ());
        if (msg.getSender ().equals (mUser)) handleUserMessage (msg);
        else handleNormalMessage (msg);
    }
    
    private void handleNormalMessage (NormalMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleNormalMessage");
        Log.d ("cutb_debug", "   Sender: " + msg.getSender ());
        NormalMessageModel newNormalMessage;
        newNormalMessage = new NormalMessageModel (msg);
        getRoomData (msg.getRoom ()).addNewMessage (newNormalMessage);
        
        // post-handleMessage
        mInbox.add (msg);
        saveTempGameData ();
        mPostman.createNewAlarm (getMessageInterval ());
    }
    
    private void handleUserMessage (NormalMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleUserMessage");
        UserMessageModel newUserMessage;
        newUserMessage = new UserMessageModel (msg, getReadCount (msg.getRoom ()));
        getRoomData (msg.getRoom ()).addNewMessage (newUserMessage);
        
        // post-handleMessage
        saveGameData ();
        mPostman.notifyFrontEnd ();
        mPostman.createNewAlarm (getMessageInterval ());
    }
    
    private void handleMessage (ChoicesMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage (ChoicesMessage)");
        elevateChatList (msg.getRoom ());
        mRoomData.get (msg.getRoom ()).setOnChoices (true);
        saveTempGameData ();
        mPostman.createNewAlarm (3);
    }
    
    private void handleMessage (PostMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage (PostMessage)");
        elevateChatList (msg.getRoom ());
        
        if (msg.isSaveAsNote ()) handleNoteMessage (msg);
        else handlePostMessage (msg);
    }
    
    private void handlePostMessage (PostMessage msg) {
        PostMessageModel newPostMessage;
        newPostMessage = new PostMessageModel (msg);
        getRoomData (msg.getRoom ()).addNewMessage (newPostMessage);
        
        // post-handleMessage
        mInbox.add (msg);
        saveTempGameData ();
        mPostman.createNewAlarm (getMessageInterval ());
    }
    
    private void handleNoteMessage (PostMessage msg) {
        NoteMessageModel newNoteMessage = new NoteMessageModel (msg);
        NoteModel newNote = new NoteModel (msg.getPost ());
        getRoomData (msg.getRoom ()).addNewMessage (newNoteMessage);
        getRoomData (msg.getRoom ()).addNewNote (newNote);
        newNoteMessage.setNoteIndex (getRoomData (msg.getRoom ()).noteSize ()-1);
        
        // post-handleMessage
        mInbox.add (msg);
        saveTempGameData ();
        mPostman.createNewAlarm (getMessageInterval ());
    }
    
    private void handleMessage (InfoMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage (InfoMessage)");
        switch (msg.getInfoCategory ()) {
            case InfoMessage.CATEGORY_INVITATION: handleInfoMessage ((InvitationMessage) msg); break;
            default: break;
        }
        handleMessage ();
    }
    
    private void handleInfoMessage (InvitationMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleInfoMessage (InvitationMessage)");
        elevateChatList (msg.getRoom ());
        mRoomData.get (msg.getRoom ()).setRoomAsGroup (msg.getMemberCount (), msg.getMemberList ());
        saveGameData ();
    }
    
    /* This is magic. Do not touch! */
    private void incrementIndex () {
        Log.d ("cutb_debug", "at MessageHandler.#incrementIndex");
        Log.d ("cutb_debug", "   Initial index: " + mIndex);
        while (!mActiveChoices.empty () && (mIndex == mActiveChoices.peek ().mEnd)) {
            mIndex = mActiveChoices.peek ().mMax;
            mActiveChoices.pop ();
        }
        
        mIndex++;
        Log.d ("cutb_debug", "   Result index: " + mIndex);
    }
    
    private boolean isEndOfDay () {
        Log.d ("cutb_debug", "at MessageHandler.#isEndOfDay");
        Log.d ("cutb_debug", mIndex + " " + mMessageData);
        return mIndex == mMessageData.size ();
    }
    
    private void startNewDay () {
        loadMessageData ();
        removeEmptyDay ();
        putDateMessage ();
        //pendingGameForNextDay ();
        handleMessage (mContext);
    }
    
    private void loadMessageData () {
        IOHandler io;
        io = new IOHandler (mContext);
        
        mIndex = -1;
        mDay = mDay + 1;
        mMessageData = io.loadMessageData (mDay);
    }
    
    private void putDateMessage () {
        for (Map.Entry<String, RoomData> roomData : mRoomData.entrySet ()) {
            roomData.getValue ().addNewMessage (new InfoMessageModel (mMessageData.getDate ()));
        }
    }
    
    private void removeEmptyDay () {
        for (Map.Entry<String, RoomData> roomDataEntry : mRoomData.entrySet ()) {
            RoomData roomData = roomDataEntry.getValue ();
            
            if (roomData.getMessageAt (-1).getMessageType () == ChatMessageModel.MESSAGE_TYPE_INFO) {
                roomData.removeMessage (-1);
            }
        }
    }
    
    private void pendingGameForNextDay () {
        Log.d ("cutb_debug", "at MessageHandler.#pendingGameForNextDay");
        MessageTime time;
        Log.d ("cutb_debug", "   mMessageData: " + mMessageData);
        Log.d ("cutb_debug", "   mDay: " + mDay);
        time = ((NormalMessage) mMessageData.getMessageAt (0)).getTime ();
        
        Calendar calendar;
        calendar = Calendar.getInstance ();
        calendar.set (Calendar.DATE, calendar.get (Calendar.DATE) + 1);
        calendar.set (Calendar.HOUR, time.getTimeHour ());
        calendar.set (Calendar.MINUTE, time.getTimeMinute ());
        calendar.set (Calendar.AM_PM, time.getTimePeriod ());
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
        if (mRoomData.get (room).getRoomType () == RoomData.TYPE_GROUP) {
            List<String> memberList;
            memberList = mRoomData.get (room).getMemberList ();
            memberList.removeAll (mDeadFriend);
            return memberList.size ();
        }
        else {
            if (mDeadFriend.contains (room)) return 0;
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
