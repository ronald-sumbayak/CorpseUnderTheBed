package ra.sumbayak.corpseunderthebed.rv.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;

public class ChatListViewHolder extends RecyclerView.ViewHolder {
    
    private TextView room, lastMessage, memberCount, unreadCount;
    
    public ChatListViewHolder (View itemView) {
        super (itemView);
        room = (TextView) itemView.findViewById (R.id.clRoom);
        lastMessage = (TextView) itemView.findViewById (R.id.clLastMessage);
        memberCount = (TextView) itemView.findViewById (R.id.clMemberCount);
        unreadCount = (TextView) itemView.findViewById (R.id.clUnreadCount);
    }
    
    public TextView getRoom () { return room; }
    public TextView getLastMessage () { return lastMessage; }
    public TextView getMemberCount () { return memberCount; }
    public TextView getUnreadCount () { return unreadCount; }
}
