package ra.sumbayak.corpseunderthebed.handlers;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.messages.ChosenChoices;
import ra.sumbayak.corpseunderthebed.datas.messages.ChoicesMessage;

abstract class ChoicesHandler extends MessageHandler implements Serializable {
    
    @Override
    public void handleChoices (Context context, int selection) {
        startNewSession (context);
        ChoicesMessage msg;
        msg = (ChoicesMessage) currentMessage ();
        
        calculateChoicesIndex (msg, selection);
        mRoomData.get (msg.room ()).setOnChoices (false);
        incrementIndex ();
        
        saveGameData ();
        handleMessage (currentMessage ());
    }
    
    private void calculateChoicesIndex (ChoicesMessage msg, Integer selection) {
        /* STEP BACK! DO NOT TOUCH IT! */
        Log.d ("cutb_debug", "at ChoicesHandler.#calculateChoicesIndex");
        Log.d ("cutb_debug", "   selection: " + selection);
        Log.d ("cutb_debug", "   initial index: " + mIndex);
        int skipped = 0, begin, end, max = 0;
        
        for (int i = 0; i < msg.choicesSize (); i++) {
            if (i < selection) skipped += msg.getChoices (i).replies ();
            max += msg.getChoices (i).replies ();
        }
        
        begin = mIndex + skipped;
        end = begin + msg.getChoices (selection).replies ();
        Log.d ("cutb_debug", "   skipped: " + skipped);
        Log.d ("cutb_debug", "   begin: " + begin);
        Log.d ("cutb_debug", "   end: " + end);
        Log.d ("cutb_debug", "   max: " + max);
        mActiveChoices.push (new ChosenChoices (begin, end, mIndex + max));
        
        mIndex = begin;
        Log.d ("cutb_debug", "   Index result: " + mIndex);
    }
    
//    private void elevateChatList (String room) {
//        if (!mChatList.contains (room)) {
//            mChatList.add (room);
//        }
//        
//        for (int x = mChatList.indexOf (room); x > 0; x--) {
//            String temp = mChatList.get (x);
//            mChatList.set (x, mChatList.get (x - 1));
//            mChatList.set (x - 1, temp);
//        }
//    }
}
