package ra.sumbayak.corpseunderthebed.rv.viewholders;

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
    private LinearLayout mUnreadBackground;
    
    public ChatListViewHolder (View itemView) {
        super (itemView);
        mRoom = (TextView) itemView.findViewById (R.id.clRoom);
        mLastMessage = (TextView) itemView.findViewById (R.id.clLastMessage);
        mMemberCount = (TextView) itemView.findViewById (R.id.clMemberCount);
        mUnreadCount = (TextView) itemView.findViewById (R.id.clUnreadCount);
        mUnreadBackground = (LinearLayout) mUnreadCount.getParent ();
    }
    
    public void bind (RoomData roomData) {
        // room
        mRoom.setText (roomData.getRoom ());
    
        // last message
        mLastMessage.setText (getLastChatMessage (roomData));
    
        // member-count
        if (roomData.getRoomType () == RoomData.TYPE_GROUP) {
            mMemberCount.setText ("(" + roomData.getMemberCount () + ")");
            mMemberCount.setVisibility (View.VISIBLE);
        }
        else
            mMemberCount.setVisibility (View.INVISIBLE);
    
        // unread-count
        if (roomData.getUnreadCount () > 0) {
            mUnreadCount.setText (String.valueOf (roomData.getUnreadCount ()));
            mUnreadBackground.setVisibility (View.VISIBLE);
            mLastMessage.setTypeface (Typeface.DEFAULT_BOLD);
            mRoom.setTypeface (Typeface.DEFAULT_BOLD);
        }
        else {
            mUnreadBackground.setVisibility (View.INVISIBLE);
            mRoom.setTypeface (Typeface.DEFAULT);
            mLastMessage.setTypeface (Typeface.DEFAULT);
        }
    }
    
    private String getLastChatMessage (RoomData roomData) {
        for (int i = roomData.messageSize () - 1; i >=0; i--) {
            if (roomData.getMessageAt (i) instanceof NormalMessageModel) {
                return roomData.getMessageAt (i).getText ();
            }
        }
        
        return null;
    }
    
    public void setOnClickListener (View.OnClickListener onClickListener) {
        itemView.setOnClickListener (onClickListener);
    }
}
