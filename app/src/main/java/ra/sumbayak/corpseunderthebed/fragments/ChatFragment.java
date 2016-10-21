package ra.sumbayak.corpseunderthebed.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.choices.ChoicesMessage;
import ra.sumbayak.corpseunderthebed.handlers.FileIOHandler;
import ra.sumbayak.corpseunderthebed.rv.adapters.ChatMessageAdapter;
import ra.sumbayak.corpseunderthebed.rv.decorations.VerticalSpacingItemDecoration;
import ra.sumbayak.corpseunderthebed.rv.layoutmanagers.SmoothScrollLayoutManager;
import ra.sumbayak.corpseunderthebed.services.Postman;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static ra.sumbayak.corpseunderthebed.R.id.choicesPanel;
import static ra.sumbayak.corpseunderthebed.R.id.wrap_content;

public class ChatFragment extends Fragment implements MainActivity.FragmentLink {
    
    private LinearLayout mChoicesPanel;
    private MainActivity mMainActivity;
    private RecyclerView mChatMessage;
    private String mRoom;
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d ("cutb_debug", "at ChatFragment.#onCreateView");
        View view;
        view = inflater.inflate (R.layout.fragment_chat, container, false);
        return view;
    }
    
    @Override
    public void onResume () {
        Log.d ("cutb_debug", "at ChatFragment.#onResume");
        super.onResume ();
        setToolbarTitle ();
        setFragmentLink ();
        refreshChatMessage (getGameData ());
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        Log.d ("cutb_debug", "at ChatFragment.#onViewCreated");
        setupView (view);
        setupChatMessage (getGameData ());
    }
    
    private void setupView (View view) {
        Log.d ("cutb_debug", "at ChatFragment.#setupView");
        mChatMessage = (RecyclerView) view.findViewById (R.id.rvChatMessage);
        mChoicesPanel = (LinearLayout) view.findViewById (choicesPanel);
        mMainActivity = (MainActivity) getActivity ();
        mRoom = getArguments ().getString ("cutb.ROOM");
    }
    
    private GameData getGameData () {
        return mMainActivity.getGameData ();
    }
    
    private void setupChatMessage (final GameData gameData) {
        Log.d ("cutb_debug", "at ChatFragment.#setupChatMessage");
        mChatMessage.setHasFixedSize (false);
        mChatMessage.setLayoutManager (new SmoothScrollLayoutManager (getContext ()));
        mChatMessage.addItemDecoration (new VerticalSpacingItemDecoration (8));
        mChatMessage.setAdapter (new ChatMessageAdapter (gameData, mRoom));
        mChatMessage.scrollToPosition (gameData.getRoomData (mRoom).getMessageSize () - 1);
        refreshChatMessage (gameData);
    }
    
    private void setToolbarTitle () {
        Log.d ("cutb_debug", "at ChatFragment.#setToolbarTitle");
        ActionBar actionBar;
        actionBar = ((AppCompatActivity) getActivity ()).getSupportActionBar ();
        assert actionBar != null;
        actionBar.setTitle (mRoom);
    }
    
    @Override
    public void onBroadcastReceived () {
        Log.d ("cutb_debug", "at ChatFragment.#onBroadcastReceived");
        refreshChatMessage (getGameData ());
    }
    
    private void refreshChatMessage (final GameData gameData) {
        Log.d ("cutb_debug", "at ChatFragment.#refreshChatMessage");
        buildChoicesPanel (gameData);
        setOnLayoutChangeListener (gameData.getRoomData (mRoom).getMessageSize (), gameData);
        
        ChatMessageAdapter adapter;
        adapter = (ChatMessageAdapter) mChatMessage.getAdapter ();
        adapter.refreshChatMessage (gameData);
        markMessageAsRead ();
    }
    
    private void markMessageAsRead () {
        GameData gameData;
        gameData = getGameData ();
        gameData.markMessageAsRead (mRoom);
    
        FileIOHandler io;
        io = new FileIOHandler (getActivity ());
        io.saveGameData (gameData);
    }
    
    private void setOnLayoutChangeListener (final int messageSize, final GameData gameData) {
        mChatMessage.addOnLayoutChangeListener (new View.OnLayoutChangeListener () {
            @Override
            public void onLayoutChange (View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d ("cutb_debug", "at ChatFragment.#refreshChatMessage.OnLayoutChangeListener.#onLayoutChange");
                mChatMessage.removeOnLayoutChangeListener (this);
                mChatMessage.scrollToPosition (gameData.getRoomData (mRoom).getMessageSize () - 1);
            }
        });
    }
    
    private int getLastItemPosition () {
        LinearLayoutManager layoutManager;
        layoutManager = (LinearLayoutManager) mChatMessage.getLayoutManager ();
        return layoutManager.findLastCompletelyVisibleItemPosition ();
    }
    
    private void buildChoicesPanel (GameData gameData) {
        Log.d ("cutb_debug", "at ChatFragment.#buildChoicesPanel");
        Log.i ("cutb_debug", "   GameData.onChoices: " + gameData.isOnChoices ());
        mChoicesPanel.removeAllViews ();
        if (gameData.isOnChoices ()) {
            ChoicesMessage msg;
            msg = (ChoicesMessage) gameData.getMessageData ().getMessageAt (gameData.getIndex ());
            LayoutInflater inflater;
            inflater = (LayoutInflater) getActivity ().getSystemService (LAYOUT_INFLATER_SERVICE);
        
            for (int i = 0; i < msg.choicesSize (); i++) {
                Button choices;
                choices = makeChoicesButton (inflater, msg, i);
                LinearLayout.LayoutParams params;
                params = new LinearLayout.LayoutParams (wrap_content, wrap_content);
                mChoicesPanel.addView (choices, params);
            }
        }
        
        assert getView () != null;
        ((View) getView ().getParent ()).invalidate ();
    }
    
    @SuppressLint ("InflateParams")
    private Button makeChoicesButton (LayoutInflater inflater, ChoicesMessage msg, final int position) {
        Log.d ("cutb_debug", "at ChatFragment.#makeChoicesButton");
        Log.d ("cutb_debug", "   Position: " + position);
        Log.d ("cutb_debug", "   Text: " + msg.getChoicesAt (position).getLabel ());
        Button choices;
        choices = (Button) inflater.inflate (R.layout.itemview_choices, null);
        choices.setText (msg.getChoicesAt (position).getLabel ());
        choices.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Log.d ("cutb_debug", "at ChatFragment.#buildChoicesPanel.OnClickListener.#onClick");
                sendChoicesSelection (position);
                mChoicesPanel.removeAllViews ();
            }
        });
        return choices;
    }
    
    private void sendChoicesSelection (int position) {
        Log.d ("cutb_debug", "at ChatFragment.#sendChoicesSelection");
        Intent intent;
        intent = new Intent (mMainActivity, Postman.class);
        intent.setAction ("cutb.CHOICES");
        intent.putExtra ("selection", position);
        mMainActivity.startService (intent);
    }
    
    private void setFragmentLink () {
        mMainActivity.setFragmentLink (this);
    }
}
