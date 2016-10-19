package ra.sumbayak.corpseunderthebed.rv.adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.runnables.FadingAnimation;
import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.fragments.ChatFragment;
import ra.sumbayak.corpseunderthebed.rv.models.ChatMessageModel;
import ra.sumbayak.corpseunderthebed.rv.viewholders.ChatListViewHolder;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {
    
    private GameData mGameData;
    
    public ChatListAdapter (GameData gameData) {
        Log.d ("cutb_debug", "at ChatListAdapter.#Constructor");
        mGameData = gameData;
    }
    
    @Override
    public ChatListViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        inflater = LayoutInflater.from (parent.getContext ());
        
        View itemView;
        itemView = inflater.inflate (R.layout.itemview_chatlist, parent, false);
        return new ChatListViewHolder (itemView);
    }
    
    @Override
    public void onBindViewHolder (ChatListViewHolder holder, int position) {
        String room = mGameData.getChatList ().get (position);
        RoomData roomData = mGameData.getRoomData ().get (room);
        
        setItemView (holder, roomData);
        setOnClickListener (holder.itemView, roomData.getRoom ());
    }
    
    private void setItemView (ChatListViewHolder holder, RoomData roomData) {
        // sender (room)
        setText (holder.getRoom (), roomData.getRoom ());
        
        // last message
        ChatMessageModel msg = roomData.getMessageAt (-1);
        if (msg == null)
            setText (holder.getLastMessage (), null);
        else
            setText (holder.getLastMessage (), msg.getText ());
        
        // member count
        if (roomData.getRoomType () == RoomData.TYPE_GROUP) {
            setText (holder.getMemberCount (), "(" + roomData.getMemberCount () + ")");
            setVisibility (holder.getMemberCount (), View.VISIBLE);
        }
        else setVisibility (holder.getMemberCount (), View.INVISIBLE);
        
        // unread count
        if (roomData.getUnreadCount () > 0) {
            setVisibility (holder.getUnreadCount (), View.VISIBLE);
            setText (holder.getUnreadCount (), String.valueOf (roomData.getUnreadCount ()));
        }
        else setVisibility (holder.getUnreadCount (), View.INVISIBLE);
    }
    
    private void setText (TextView textView, String text) {
        textView.setText (text);
    }
    
    private void setVisibility (View view, int visibility) {
        view.setVisibility (visibility);
    }
    
    private void setOnClickListener (View itemView, final String room) {
        itemView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (final View v) {
                final MainActivity mainActivity;
                mainActivity = (MainActivity) v.getContext ();
                
                FadingAnimation fadingAnimation;
                fadingAnimation = new FadingAnimation (mainActivity);
                fadingAnimation.setFadeInEnd (new FadingAnimation.FadeInAnimationEnd () {
                    @Override
                    public void onFadeAnimationEnd () {
                        ChatFragment chatFragment;
                        chatFragment = buildChatFragment (room);
                        mainActivity.replaceFragment (R.id.fragment_view, chatFragment, "ChatRoom." + room);
                    }
                });
                fadingAnimation.run ();
            }
        });
    }
    
    private ChatFragment buildChatFragment (String room) {
        Bundle bundle;
        bundle = new Bundle ();
        bundle.putString ("cutb.ROOM", room);
    
        ChatFragment chatFragment;
        chatFragment = new ChatFragment ();
        chatFragment.setArguments (bundle);
        return chatFragment;
    }
    
    @Override
    public int getItemCount () {
        return mGameData.getChatList ().size ();
    }
    
    public void refreshChatList (GameData gameData) {
        Log.d ("cutb_debug", "at ChatListAdapter.#refreshChatList");
        mGameData = gameData;
        notifyDataSetChanged ();
    }
}