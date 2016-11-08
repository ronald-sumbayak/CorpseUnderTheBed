package ra.sumbayak.corpseunderthebed.rv.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.animations.Fading;
import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.fragments.ChatMessageFragment;
import ra.sumbayak.corpseunderthebed.rv.viewholders.ChatListViewHolder;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {
    
    private MainActivity mContext;
    private LayoutInflater mInflater;
    private GameData mGameData;
    private RecyclerView mChatList;
    
    public ChatListAdapter (Context context, GameData gameData) {
        mContext = (MainActivity) context;
        mInflater = LayoutInflater.from (mContext);
        mGameData = gameData;
    }
    
    public ChatListAdapter (Context context, GameData gameData, RecyclerView chatList) {
        mContext = (MainActivity) context;
        mInflater = LayoutInflater.from (mContext);
        mGameData = gameData;
        mChatList = chatList;
    }
    
    @Override
    public ChatListViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView;
        itemView = mInflater.inflate (R.layout.itemview_chatlist, parent, false);
        return new ChatListViewHolder (itemView);
    }
    
    @Override
    public void onBindViewHolder (ChatListViewHolder holder, int position) {
        String room = mGameData.chatList ().get (position);
        holder.bind (mGameData.roomData (room));
        holder.itemView.setOnClickListener (makeOnClickListener (room));
    }
    
    private View.OnClickListener makeOnClickListener (final String room) {
        return new View.OnClickListener () {
            @Override
            public void onClick (final View v) {
                Fading fading;
                fading = new Fading (mContext.findViewById (R.id.fade_interpolator)) {
                    @Override
                    public void onFadeInEnd () {
                        ChatMessageFragment chatMessageFragment;
                        chatMessageFragment = buildChatFragment (room);
                        putChatFragment (chatMessageFragment, room);
                    }
                };
                fading.run ();
            }
        };
    }
    
    private ChatMessageFragment buildChatFragment (String room) {
        Bundle bundle;
        bundle = new Bundle ();
        bundle.putString ("cutb.ROOM", room);
    
        ChatMessageFragment chatMessageFragment;
        chatMessageFragment = new ChatMessageFragment ();
        chatMessageFragment.setArguments (bundle);
        return chatMessageFragment;
    }
    
    private void putChatFragment (ChatMessageFragment fragment, String room) {
        FragmentTransaction ft;
        ft = mContext.getSupportFragmentManager ().beginTransaction ();
        ft.addToBackStack (null);
        ft.replace (R.id.fragment_view, fragment, "ChatRoom." + room);
        ft.commit ();
    }
    
    @Override
    public int getItemCount () {
        return mGameData.chatList ().size ();
    }
    
    public void refresh (GameData gameData) {
        mGameData = gameData;
        notifyDataSetChanged ();
    }
}