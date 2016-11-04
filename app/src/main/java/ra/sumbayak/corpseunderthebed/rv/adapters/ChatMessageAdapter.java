package ra.sumbayak.corpseunderthebed.rv.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.rv.models.chats.ChatMessageModel;
import ra.sumbayak.corpseunderthebed.rv.viewholders.chats.*;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageViewHolder> {
    
    private final int[] mResIdList = {
        R.layout.itemview_message_info,
        R.layout.itemview_message_normal,
        R.layout.itemview_message_user,
        R.layout.itemview_message_note,
        R.layout.itemview_message_post
    };
    
    private static final int VIEW_TYPE_INFO = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_USER = 2;
    private static final int VIEW_TYPE_NOTE = 3;
    private static final int VIEW_TYPE_POST = 4;
    
    private RoomData mRoomData;
    
    public ChatMessageAdapter (RoomData roomData) {
        mRoomData = roomData;
    }
    
    @Override
    public ChatMessageViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        inflater = LayoutInflater.from (parent.getContext ());
    
        View itemView;
        itemView = inflater.inflate (mResIdList[viewType], parent, false);
        return getViewHolderInstance (viewType, itemView);
    }
    
    private ChatMessageViewHolder getViewHolderInstance (int viewType, View itemView) {
        switch (viewType) {
            case VIEW_TYPE_INFO: return new InfoMessageViewHolder (itemView);
            case VIEW_TYPE_NORMAL: return new NormalMessageViewHolder (itemView);
            case VIEW_TYPE_USER: return new UserMessageViewHolder (itemView);
            case VIEW_TYPE_NOTE: return new NoteMessageViewHolder (itemView);
            case VIEW_TYPE_POST: return new PostMessageViewHolder (itemView);
            default: return null;
        }
    }
    
    @Override
    public void onBindViewHolder (ChatMessageViewHolder holder, int position) {
        holder.bind (mRoomData, position);
    }
    
    @Override
    public int getItemViewType (int position) {
        ChatMessageModel msg;
        msg = mRoomData.getMessageAt (position);
        return msg.getMessageType ();
    }
    
    @Override
    public int getItemCount () {
        return mRoomData.messageSize ();
    }
    
    public void refreshChatMessage (RoomData roomData) {
        Log.d ("cutb_debug", "at ChatMessageAdapter.#refreshChatMessage");
        Log.d ("cutb_debug", "   new size: " + roomData.messageSize ());
        Log.d ("cutb_debug", "   old size: " + mRoomData.messageSize ());
        if (roomData.messageSize () > mRoomData.messageSize ()) {
            int oldSize;
            oldSize = mRoomData.messageSize ();
            mRoomData = roomData;
            notifyItemInserted (oldSize);
        }
    }
}
