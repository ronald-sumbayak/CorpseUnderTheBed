package ra.sumbayak.corpseunderthebed.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.handlers.IOHandler;
import ra.sumbayak.corpseunderthebed.rv.adapters.ChatListAdapter;
import ra.sumbayak.corpseunderthebed.rv.decorations.DividerItemDecoration;

public class ChatListFragment extends Fragment implements MainActivity.FragmentLink {
    
    private IOHandler mIO;
    private GameData mGameData;
    private MainActivity mContext;
    private ChatListAdapter mAdapter;
    
    @Override
    public void onAttach (Context context) {
        super.onAttach (context);
        mContext = (MainActivity) context;
        mIO = IOHandler.getIOInstance (mContext);
        loadGameData ();
    }
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate (R.layout.fragment_menu, container, false);
        return view;
    }
    
    @Override
    public void onResume () {
        super.onResume ();
        setFragmentLink ();
        setupToolbar ();
        refreshChatList ();
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        setupChatList (view);
    }
    
    @Override
    public void onBroadcastReceived () {
        refreshChatList ();
    }
    
    private void setupChatList (View view) {
        RecyclerView chatList;
        chatList = (RecyclerView) view.findViewById (R.id.rvChatList);
        // mChatList.setHasFixedSize (true);
        chatList.setLayoutManager (new LinearLayoutManager (mContext));
        chatList.addItemDecoration (new DividerItemDecoration (mContext));
        mAdapter = new ChatListAdapter (mContext, mGameData, chatList);
        chatList.setAdapter (mAdapter);
    }
    
    private void setupToolbar () {
        ActionBar actionBar;
        actionBar = mContext.getSupportActionBar ();
        assert actionBar != null;
        actionBar.setTitle ("Chats");
        actionBar.setDisplayHomeAsUpEnabled (false);
    }
    
    private void refreshChatList () {
        loadGameData ();
        mAdapter.refresh (mGameData);
    }
    
    private void loadGameData () {
        mGameData = mIO.loadGameData ();
    }
    
    private void setFragmentLink () {
        mContext.setFragmentLink (this);
    }
}
