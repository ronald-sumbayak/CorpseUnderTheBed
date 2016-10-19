package ra.sumbayak.corpseunderthebed.fragments;

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
import ra.sumbayak.corpseunderthebed.rv.adapters.ChatListAdapter;
import ra.sumbayak.corpseunderthebed.rv.decorations.DividerItemDecoration;

public class MenuFragment extends Fragment implements MainActivity.FragmentLink {
    
    private MainActivity mMainActivity;
    private RecyclerView mChatList;
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d ("cutb_debug", "at MenuFragment.#onCreateView");
        View view;
        view = inflater.inflate (R.layout.fragment_menu, container, false);
        return view;
    }
    
    @Override
    public void onBroadcastReceived () {
        Log.d ("cutb_debug", "at MenuFragment.#onBroadcastReceived");
        refreshChatList (getGameData ());
    }
    
    @Override
    public void onResume () {
        Log.d ("cutb_debug", "at MenuFragment.#onResume");
        super.onResume ();
        setToolbarTitle ();
        setFragmentLink ();
        refreshChatList (getGameData ());
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        Log.d ("cutb_debug", "at MenuFragment.#onViewCreated");
        mMainActivity = (MainActivity) getActivity ();
        setupChatList (getGameData (), view);
        setToolbarTitle ();
    }
    
    private GameData getGameData () {
        Log.d ("cutb_debug", "at MenuFragment.#getGameData");
        return mMainActivity.getGameData ();
    }
    
    private void setupChatList (GameData gameData, View view) {
        Log.d ("cutb_debug", "at MenuFragment.#setupChatList");
        mChatList = (RecyclerView) view.findViewById (R.id.rvChatList);
        mChatList.setHasFixedSize (true);
        mChatList.setLayoutManager (new LinearLayoutManager (getActivity ()));
        mChatList.addItemDecoration (new DividerItemDecoration (getActivity ()));
        mChatList.setAdapter (new ChatListAdapter (gameData));
    }
    
    private void setToolbarTitle () {
        Log.d ("cutb_debug", "at MenuFragment.#setToolbarTitle");
        ActionBar actionBar;
        actionBar = mMainActivity.getSupportActionBar ();
        assert actionBar != null;
        actionBar.setTitle ("Chats");
    }
    
    private void refreshChatList (GameData gameData) {
        Log.d ("cutb_debug", "at MenuFragment.#refreshChatList");
        ChatListAdapter adapter;
        adapter = (ChatListAdapter) mChatList.getAdapter ();
        adapter.refreshChatList (gameData);
    }
    
    private void setFragmentLink () {
        mMainActivity.setFragmentLink (this);
    }
}
