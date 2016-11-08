package ra.sumbayak.corpseunderthebed.rv.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.rv.viewholders.chats.*;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageViewHolder> {
    
    private final int[] mResIdList = {
        R.layout.itemview_message_info,
        R.layout.itemview_message_normal,
        R.layout.itemview_message_user,
        R.layout.itemview_message_note,
        R.layout.itemview_message_share
    };
    
    private static final int TYPE_INFO = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_USER = 2;
    private static final int TYPE_NOTE = 3;
    private static final int TYPE_POST = 4;
    
    private LayoutInflater mInflater;
    private RoomData mRoomData;
    
    public ChatMessageAdapter (Context context, RoomData roomData) {
        mInflater = LayoutInflater.from (context);
        mRoomData = roomData;
    }
    
    @Override
    public ChatMessageViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView;
        itemView = mInflater.inflate (mResIdList[viewType], parent, false);
        return getViewHolderInstance (viewType, itemView);
    }
    
    private ChatMessageViewHolder getViewHolderInstance (int viewType, View itemView) {
        switch (viewType) {
            case TYPE_INFO: return new InfoMessageViewHolder (itemView);
            case TYPE_NORMAL: return new NormalMessageViewHolder (itemView);
            case TYPE_USER: return new UserMessageViewHolder (itemView);
            case TYPE_NOTE: return new NoteMessageViewHolder (itemView);
            case TYPE_POST: return new ShareMessageViewHolder (itemView);
            default: return null;
        }
    }
    
    @Override
    public void onBindViewHolder (ChatMessageViewHolder holder, int position) {
        holder.bind (mRoomData, position);
    }
    
    @Override
    public int getItemViewType (int position) {
        return mRoomData.messageAt (position).type ();
    }
    
    @Override
    public int getItemCount () {
        return mRoomData.messageSize ();
    }
    
    public void refresh (RoomData roomData) {
        if (roomData.messageSize () > mRoomData.messageSize ()) {
            mRoomData = roomData;
            notifyItemInserted (mRoomData.messageSize () - 1);
        }
    }
}
