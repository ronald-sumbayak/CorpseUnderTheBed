package ra.sumbayak.corpseunderthebed.rv.viewholders;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.rv.models.chats.NormalMessageModel;

public class ChatListViewHolder extends RecyclerView.ViewHolder {
    
    private TextView mRoom, mLastMessage, mMemberCount, mUnreadCount;
    private LinearLayout mUnreadBackground, mBackground;
    
    public ChatListViewHolder (View itemView) {
        super (itemView);
        mRoom = (TextView) itemView.findViewById (R.id.clRoom);
        mLastMessage = (TextView) itemView.findViewById (R.id.clLastMessage);
        mMemberCount = (TextView) itemView.findViewById (R.id.clMemberCount);
        mUnreadCount = (TextView) itemView.findViewById (R.id.clUnreadCount);
        mUnreadBackground = (LinearLayout) mUnreadCount.getParent ();
        mBackground = (LinearLayout) itemView;
    }
    
    public void bind (RoomData roomData) {
        // room, last message
        mRoom.setText (roomData.room ());
        mLastMessage.setText (getLastChatMessage (roomData));
    
        // member-count
        if (roomData.type () == RoomData.TYPE_GROUP) {
            mMemberCount.setText ("(" + roomData.memberCount () + ")");
            mMemberCount.setVisibility (View.VISIBLE);
        }
        else
            mMemberCount.setVisibility (View.INVISIBLE);
    
        // unread-count
        if (roomData.unreadCount () > 0) {
            mUnreadCount.setText (String.valueOf (roomData.unreadCount ()));
            mUnreadBackground.setVisibility (View.VISIBLE);
            mLastMessage.setTypeface (Typeface.DEFAULT_BOLD);
            mRoom.setTypeface (Typeface.DEFAULT_BOLD);
        }
        else {
            mUnreadBackground.setVisibility (View.INVISIBLE);
            mRoom.setTypeface (Typeface.DEFAULT);
            mLastMessage.setTypeface (Typeface.DEFAULT);
        }
        
        if (roomData.isOnChoices ()) {
            mBackground.setBackgroundColor (Color.parseColor ("#a0ffff00"));
        }
    }
    
    private String getLastChatMessage (RoomData roomData) {
        for (int i = roomData.messageSize () - 1; i >=0; i--) {
            if (roomData.messageAt (i) instanceof NormalMessageModel) {
                return roomData.messageAt (i).text ();
            }
        }
        
        return null;
    }
}
