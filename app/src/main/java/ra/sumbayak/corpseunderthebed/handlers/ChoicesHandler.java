package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.datas.msg.ChoicesMessage;
import ra.sumbayak.corpseunderthebed.datas.msg.ChosenChoices;
import ra.sumbayak.corpseunderthebed.rv.models.ChatMessageModel;

public abstract class ChoicesHandler extends MessageHandler implements Serializable {
    
    @Override
    public void handleChoices (Context context, Integer selection) {
        ChoicesMessage msg;
        msg = (ChoicesMessage) mMessageData.getMessageAt (mIndex);
        
        elevateChatList (msg.getRoom ());
        
        ChatMessageModel newMessage;
        newMessage = new ChatMessageModel (
            msg.getChoicesAt (selection).getText (), msg.getTime (), getReadCount (msg.getRoom ())
        );
        
        mRoomData.get (msg.getRoom ()).addNewMessage (newMessage);
        mChosenMessage.add (msg);
        calculateChoicesIndex (msg, selection);
        
        mRoomData.get (msg.getRoom ()).setOnChoices (false);
    }
    
    private void calculateChoicesIndex (ChoicesMessage msg, Integer selection) {
        Log.d ("cutb_debug", "at ChoicesHandler.#calculateChoicesIndex");
        Integer skipped = 0, begin, end, max = 0;
        
        for (int i = 0; i < msg.getChoicesSize (); i++) {
            if (i < selection) skipped += msg.getChoicesAt (i).getReplies ();
            max += msg.getChoicesAt (i).getReplies ();
        }
        
        begin = mIndex + skipped;
        end = begin + msg.getChoicesAt (selection).getReplies ();
        mChosenChoices.push (new ChosenChoices (begin, end, mIndex + max));
        
        mIndex = begin;
        Log.d ("cutb_debug", "   Index result: " + mIndex);
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
    
    private Integer getReadCount (String room) {
        if (mRoomData.get (room).getRoomType () == RoomData.TYPE_GROUP) {
            List<String> memberList;
            memberList = mRoomData.get (room).getMemberList ();
            memberList.removeAll (mDeadFriend);
            return memberList.size ();
        }
        else {
            if (mDeadFriend.contains (room)) {
                return 0;
            }
            else return 1;
        }
    }
}
