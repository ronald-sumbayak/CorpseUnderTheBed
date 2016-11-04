package ra.sumbayak.corpseunderthebed.rv.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.fragments.ChatMessageFragment;
import ra.sumbayak.corpseunderthebed.runnables.FadingAnimation;
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
        holder.bind (mGameData.getRoomData (room));
        holder.setOnClickListener (makeOnClickListener (room));
    }
    
    private View.OnClickListener makeOnClickListener (final String room) {
        View.OnClickListener listener;
        listener = new View.OnClickListener () {
            @Override
            public void onClick (final View v) {
                final MainActivity mainActivity;
                mainActivity = (MainActivity) v.getContext ();
                
                FadingAnimation fadingAnimation;
                fadingAnimation = new FadingAnimation (mainActivity);
                fadingAnimation.setFadeInEnd (new FadingAnimation.FadeInAnimationEnd () {
                    @Override
                    public void onFadeAnimationEnd () {
                        ChatMessageFragment chatMessageFragment;
                        chatMessageFragment = buildChatFragment (room);
                        replaceMenuFragment (mainActivity, chatMessageFragment, room);
                    }
                });
                fadingAnimation.run ();
            }
        };
        return listener;
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
    
    private void replaceMenuFragment (MainActivity mainActivity, ChatMessageFragment chatMessageFragment, String room) {
        FragmentTransaction ft;
        ft = mainActivity.getSupportFragmentManager ().beginTransaction ();
        ft.addToBackStack (null);
        ft.replace (R.id.fragment_view, chatMessageFragment, "ChatRoom." + room);
        ft.commit ();
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