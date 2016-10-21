package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.datas.msg.Message;
import ra.sumbayak.corpseunderthebed.datas.msg.info.InfoMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.info.InvitationMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.NormalMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.choices.ChoicesMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.note.NoteMessage;
import ra.sumbayak.corpseunderthebed.rv.models.NoteModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.NormalMessageModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.NoteMessageModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.PostMessageModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.UserMessageModel;

public abstract class MessageHandler extends GameData implements Serializable {
    
    @Override
    public void handleMessage (Context context) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage");
        Log.d ("cutb_debug", "   index: " + mIndex);
        
        if (!isOnChoices ()) {
            incrementIndex (context);
            Message msg = getLastMessage ();
            
            switch (msg.getMessageType ()) {
                case Message.TYPE_CHOICES: handleMessage ((ChoicesMessage) msg); break;
                case Message.TYPE_NORMAL: handleMessage ((NormalMessage) msg); break;
                case Message.TYPE_NOTE: handleMessage ((NoteMessage) msg); break;
                case Message.TYPE_INFO: handleMessage ((InfoMessage) msg); break;
                default: break;
            }
            
            // mPostman take action...
            saveGameProgress (context);
        }
    }
    
    private void handleMessage (NormalMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handlerMessage (NormalMessage)");
        elevateChatList (msg.getRoom ());
        
        if (msg.getSender ().equals (mUser)) handleUserMessage (msg);
        else handleNormalMessage (msg);
        mInbox.add (msg);
    }
    
    private void handleNormalMessage (NormalMessage msg) {
        NormalMessageModel newNormalMessage;
        newNormalMessage = new NormalMessageModel (msg);
        getRoomData (msg.getRoom ()).addNewMessage (newNormalMessage);
    }
    
    private void handleUserMessage (NormalMessage msg) {
        UserMessageModel newUserMessage;
        newUserMessage = new UserMessageModel (msg, getReadCount (msg.getRoom ()));
        getRoomData (msg.getRoom ()).addNewMessage (newUserMessage);
    }
    
    private void handleMessage (ChoicesMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage (ChoicesMessage)");
        elevateChatList (msg.getRoom ());
        mRoomData.get (msg.getRoom ()).setOnChoices (true);
    }
    
    private void handleMessage (NoteMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage (NoteMessage)");
        elevateChatList (msg.getRoom ());
        
        if (msg.isSaveAsNote ()) handleNoteMessage (msg);
        else handlePostMessage (msg);
        mInbox.add (msg);
    }
    
    private void handlePostMessage (NoteMessage msg) {
        PostMessageModel newPostMessage;
        newPostMessage = new PostMessageModel (msg);
        getRoomData (msg.getRoom ()).addNewMessage (newPostMessage);
    }
    
    private void handleNoteMessage (NoteMessage msg) {
        NoteMessageModel newNoteMessage = new NoteMessageModel (msg);
        NoteModel newNote = new NoteModel (msg.getPost ());
        getRoomData (msg.getRoom ()).addNewMessage (newNoteMessage);
        getRoomData (msg.getRoom ()).addNewNote (newNote);
    }
    
    private void handleMessage (InfoMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleMessage (InfoMessage)");
        switch (msg.getInfoCategory ()) {
            case InfoMessage.CATEGORY_INVITATION: handleInfoMessage ((InvitationMessage) msg); break;
            default: break;
        }
    }
    
    private void handleInfoMessage (InvitationMessage msg) {
        Log.d ("cutb_debug", "at MessageHandler.#handleInfoMessage (InvitationMessage)");
        Log.d ("cutb_debug", "   Room: " + msg.getRoom ());
        Log.d ("cutb_debug", "   MemberCount: " + msg.getMemberCount ());
        Log.d ("cutb_debug", "   MemberList: " + msg.getMemberList ());
        elevateChatList (msg.getRoom ());
        mRoomData.get (msg.getRoom ()).setRoomAsGroup (msg.getMemberCount (), msg.getMemberList ());
    }
    
    private void incrementIndex (Context context) {
        while (!mActiveChoices.empty () && (mIndex == mActiveChoices.peek ().end)) {
            mIndex = mActiveChoices.peek ().max;
            mActiveChoices.pop ();
        }
        
        if (mIndex == mMessageData.size () - 1) {
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
    
    private int getReadCount (String room) {
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
    
    private void saveGameProgress (Context context) {
        FileIOHandler io;
        io = new FileIOHandler (context);
        io.saveGameData (this);
    }
}
