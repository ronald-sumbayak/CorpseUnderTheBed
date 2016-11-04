package ra.sumbayak.corpseunderthebed.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private MainActivity mMainActivity;
    private RecyclerView mChatList;
    
    @Override
    public void onAttach (Context context) {
        super.onAttach (context);
        mMainActivity = (MainActivity) context;
        mIO = IOHandler.getIOInstance (mMainActivity);
    }
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d ("cutb_debug", "at ChatListFragment.#onCreateView");
        View view;
        view = inflater.inflate (R.layout.fragment_menu, container, false);
        return view;
    }
    
    @Override
    public void onResume () {
        Log.d ("cutb_debug", "at ChatListFragment.#onResume");
        super.onResume ();
        setToolbarTitle ();
        setFragmentLink ();
        loadGameData ();
        refreshChatList ();
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        Log.d ("cutb_debug", "at ChatListFragment.#onViewCreated");
        loadGameData ();
        setupChatList (view);
        setToolbarTitle ();
    }
    
    @Override
    public void onBroadcastReceived () {
        Log.d ("cutb_debug", "at ChatListFragment.#onBroadcastReceived");
        loadGameData ();
        refreshChatList ();
    }
    
    private void loadGameData () {
        mGameData = mIO.loadGameData ();
    }
    
    private void setupChatList (View view) {
        Log.d ("cutb_debug", "at ChatListFragment.#setupChatList");
        mChatList = (RecyclerView) view.findViewById (R.id.rvChatList);
        mChatList.setHasFixedSize (true);
        mChatList.setLayoutManager (new LinearLayoutManager (getActivity ()));
        mChatList.addItemDecoration (new DividerItemDecoration (getActivity ()));
        mChatList.setAdapter (new ChatListAdapter (mGameData));
    }
    
    private void setToolbarTitle () {
        Log.d ("cutb_debug", "at ChatListFragment.#setToolbarTitle");
        ActionBar actionBar;
        actionBar = mMainActivity.getSupportActionBar ();
        assert actionBar != null;
        actionBar.setTitle ("Chats");
    }
    
    private void refreshChatList () {
        Log.d ("cutb_debug", "at ChatListFragment.#refreshChatList");
        ChatListAdapter adapter;
        adapter = (ChatListAdapter) mChatList.getAdapter ();
        adapter.refreshChatList (mGameData);
    }
    
    private void setFragmentLink () {
        mMainActivity.setFragmentLink (this);
    }
}
