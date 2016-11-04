package ra.sumbayak.corpseunderthebed.rv.viewholders.chats;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import ra.sumbayak.corpseunderthebed.datas.RoomData;

public abstract class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    
    ChatMessageViewHolder (View itemView) {
        super (itemView);
    }
    
    public abstract void bind (RoomData roomData, int position);
}
