package ra.sumbayak.corpseunderthebed.rv.adapters;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.rv.models.chats.NormalMessageModel;
import ra.sumbayak.corpseunderthebed.rv.viewholders.ChatMessageViewHolder;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageViewHolder> {
    
    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_MEMBER = 1;
    private static final int VIEW_TYPE_INFO = 2;
    
    private RoomData mRoomData;
    private String mRoom;
    
    public ChatMessageAdapter (GameData gameData, String room) {
        mRoomData = gameData.getRoomData (room);
        mRoom = room;
    }
    
    @Override
    public ChatMessageViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        int resId;
        switch (viewType) {
            case VIEW_TYPE_USER: resId = R.layout.itemview_message_right; break;
            case VIEW_TYPE_MEMBER: resId = R.layout.itemview_message_left; break;
            default: resId = 0; break;
        }
        
        LayoutInflater inflater;
        inflater = LayoutInflater.from (parent.getContext ());
    
        View itemView;
        itemView = inflater.inflate (resId, parent, false);
        return new ChatMessageViewHolder (itemView);
    }
    
    @Override
    public void onBindViewHolder (ChatMessageViewHolder holder, int position) {
        NormalMessageModel msg;
        msg = mRoomData.getMessageAt (position);
        
        switch (holder.getItemViewType ()) {
            case VIEW_TYPE_USER: 
            case VIEW_TYPE_MEMBER: bindMessage (holder, msg); break;
            case VIEW_TYPE_INFO: bindInfoMessage (holder, msg); break;
        }
        if (holder.getItemViewType () == VIEW_TYPE_INFO) bindInfoMessage (holder, msg);
        else bindMessage (holder, msg);
    }
    
    private void bindInfoMessage (ChatMessageViewHolder holder, NormalMessageModel msg) {
        holder.getText ().setText (msg.getText ());
    }
    
    private void bindMessage (ChatMessageViewHolder holder, NormalMessageModel msg) {
        setText (holder.getSender (), msg.getSender ());
        setText (holder.getText (), msg.getText ());
        setText (holder.getTime (), msg.getTime ());
        
        if (msg.isConsecutive () || msg.getSender ().equals (mRoom)) {
            setVisibility (holder.getSender (), View.GONE);
            Drawable background;
            background = ContextCompat.getDrawable (holder.getSender ().getContext (), R.drawable.itemview_msg);
            holder.getText ().setBackground (background);
        }
        else {
            setVisibility (holder.getSender (), View.VISIBLE);
            Drawable background;
            background = ContextCompat.getDrawable (holder.getSender ().getContext (), R.drawable.itemview_msg_top);
            holder.getText ().setBackground (background);
        }
        
        if (msg.getReadCount () == 0) {
            setVisibility (holder.getReadCount (), View.INVISIBLE);
        }
        else {
            if (mRoomData.getRoomType () == RoomData.TYPE_GROUP) {
                setText (holder.getReadCount (), "Read " + msg.getReadCount ());
                setVisibility (holder.getReadCount (), View.VISIBLE);
            }
            else {
                setText (holder.getReadCount (), "Read");
                setVisibility (holder.getReadCount (), View.VISIBLE);
            }
        }
    }
    
    private void setText (TextView textView, String text) {
        textView.setText (text);
    }
    
    private void setVisibility (TextView textView, int visibility) {
        textView.setVisibility (visibility);
    }
    
    @Override
    public int getItemViewType (int position) {
        NormalMessageModel message = mRoomData.getMessageAt (position);
        if (message.getSender ().equals ("user")) return VIEW_TYPE_USER;
        else return VIEW_TYPE_MEMBER;
    }
    
    @Override
    public int getItemCount () {
        return mRoomData.getMessageSize ();
    }
    
    public void refreshChatMessage (GameData gameData) {
        mRoomData = gameData.getRoomData (mRoom);
        notifyDataSetChanged ();
    }
}
