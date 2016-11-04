package ra.sumbayak.corpseunderthebed.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.datas.msg.normal.choices.ChoicesMessage;
import ra.sumbayak.corpseunderthebed.handlers.IOHandler;
import ra.sumbayak.corpseunderthebed.rv.adapters.ChatMessageAdapter;
import ra.sumbayak.corpseunderthebed.rv.decorations.VerticalSpacingItemDecoration;
import ra.sumbayak.corpseunderthebed.rv.layoutmanagers.SmoothScrollLayoutManager;
import ra.sumbayak.corpseunderthebed.services.Postman;

public class ChatMessageFragment extends Fragment implements MainActivity.FragmentLink {
    
    private MainActivity mMainActivity;
    private IOHandler mIO;
    private GameData mGameData;
    private LinearLayout mChoicesPanel;
    private RecyclerView mChatMessage;
    private String mRoom;
    
    @Override
    public void onAttach (Context context) {
        super.onAttach (context);
        mMainActivity = (MainActivity) context;
        mIO = IOHandler.getIOInstance (mMainActivity);
        mRoom = getArguments ().getString ("cutb.ROOM");
        mGameData = mIO.loadGameData ();
    }
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d ("cutb_debug", "at ChatMessageFragment.#onCreateView");
        View view;
        view = inflater.inflate (R.layout.fragment_chat, container, false);
        return view;
    }
    
    @Override
    public void onResume () {
        Log.d ("cutb_debug", "at ChatMessageFragment.#onResume");
        super.onResume ();
        setToolbarTitle ();
        setFragmentLink ();
        loadGameData ();
        refreshChatMessage ();
        mChatMessage.scrollToPosition (mGameData.getRoomData (mRoom).messageSize ()-1);
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        Log.d ("cutb_debug", "at ChatMessageFragment.#onViewCreated");
        setupView (view);
        setupChatMessage ();
    }
    
    private void loadGameData () {
        mGameData = mIO.loadGameData ();
    }
    
    private void setupView (View view) {
        Log.d ("cutb_debug", "at ChatMessageFragment.#setupView");
        mChatMessage = (RecyclerView) view.findViewById (R.id.rvChatMessage);
        mChoicesPanel = (LinearLayout) view.findViewById (R.id.choicesPanel);
    }
    
    private void setupChatMessage () {
        Log.d ("cutb_debug", "at ChatMessageFragment.#setupChatMessage");
        mChatMessage.setHasFixedSize (false);
        mChatMessage.setLayoutManager (new SmoothScrollLayoutManager (mMainActivity));
        mChatMessage.addItemDecoration (new VerticalSpacingItemDecoration (8));
        mChatMessage.setAdapter (new ChatMessageAdapter (mGameData.getRoomData (mRoom)));
        mChatMessage.scrollToPosition (mGameData.getRoomData (mRoom).messageSize ()-1);
        refreshChatMessage ();
    }
    
    private void setToolbarTitle () {
        Log.d ("cutb_debug", "at ChatMessageFragment.#setToolbarTitle");
        ActionBar actionBar;
        actionBar = ((AppCompatActivity) getActivity ()).getSupportActionBar ();
        
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled (true);
        actionBar.setHomeAsUpIndicator (R.drawable.button_back);
        actionBar.setTitle (mRoom);
    }
    
    @Override
    public void onBroadcastReceived () {
        Log.d ("cutb_debug", "at ChatMessageFragment.#onBroadcastReceived");
        loadGameData ();
        refreshChatMessage ();
    }
    
    private void refreshChatMessage () {
        Log.d ("cutb_debug", "at ChatMessageFragment.#refreshChatMessage");
        buildChoicesPanel ();
        setOnLayoutChangeListener (mGameData.getRoomData (mRoom).messageSize (), mGameData);
        
        ChatMessageAdapter adapter;
        adapter = (ChatMessageAdapter) mChatMessage.getAdapter ();
        adapter.refreshChatMessage (mGameData.getRoomData (mRoom));
        Log.d ("cutb_debug", "ChatMessage refreshed");
        markMessageAsRead ();
    }
    
    private void markMessageAsRead () {
        
    }
    
    private void setOnLayoutChangeListener (final int messageSize, final GameData gameData) {
        mChatMessage.addOnLayoutChangeListener (new View.OnLayoutChangeListener () {
            @Override
            public void onLayoutChange (View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d ("cutb_debug", "at ChatMessageFragment.#refreshChatMessage.OnLayoutChangeListener.#onLayoutChange");
                mChatMessage.removeOnLayoutChangeListener (this);
                if (lastVisibleIndex () > mGameData.getRoomData (mRoom).messageSize ()-3) {
                    mChatMessage.smoothScrollToPosition (gameData.getRoomData (mRoom).messageSize () - 1);
                }
            }
        });
    }
    
    private int lastVisibleIndex () {
        LinearLayoutManager layoutManager;
        layoutManager = (LinearLayoutManager) mChatMessage.getLayoutManager ();
        return layoutManager.findLastCompletelyVisibleItemPosition ();
    }
    
    private void buildChoicesPanel () {
        Log.d ("cutb_debug", "at ChatMessageFragment.#buildChoicesPanel");
        Log.i ("cutb_debug", "   GameData.onChoices: " + mGameData.isOnChoices ());
        mChoicesPanel.removeAllViews ();
        if (mGameData.getRoomData (mRoom).isOnChoices ()) {
            ChoicesMessage msg = (ChoicesMessage) mGameData.getCurrentMessage ();
            LayoutInflater inflater = getLayoutInflater (null);
        
            for (int i = 0; i < msg.choicesSize (); i++) {
                Button choices;
                choices = makeChoicesButton (inflater, msg, i);
                LinearLayout.LayoutParams params;
                params = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mChoicesPanel.addView (choices, params);
            }
            
            mChatMessage.scrollToPosition (mGameData.getRoomData (mRoom).messageSize ()-1);
        }
    
        assert getView () != null;
        ((View) getView ().getParent ()).invalidate ();
    }
    
    @SuppressLint ("InflateParams")
    private Button makeChoicesButton (LayoutInflater inflater, ChoicesMessage msg, final int position) {
        Button choices;
        choices = (Button) inflater.inflate (R.layout.itemview_choices, null);
        choices.setText (msg.getChoicesAt (position).getLabel ());
        choices.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Log.d ("cutb_debug", "at ChatMessageFragment.#buildChoicesPanel.OnClickListener.#onClick");
                sendChoicesSelection (position);
                mChoicesPanel.removeAllViews ();
                Log.d ("cutb_debug", "choices clicked " + position);
            }
        });
        return choices;
    }
    
    private void sendChoicesSelection (int position) {
        Log.d ("cutb_debug", "at ChatMessageFragment.#sendChoicesSelection");
        Intent intent;
        intent = new Intent (mMainActivity, Postman.class);
        intent.setAction ("cutb.CHOICES");
        intent.putExtra ("selection", position);
        mMainActivity.startService (intent);
    }
    
    private void setFragmentLink () {
        mMainActivity.setFragmentLink (this);
    }
    
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        Log.d ("cutb_debug", "at onOptionsItemSelected, id: " + item.getItemId ());
        switch (item.getItemId ()) {
            case android.R.id.home: {
                assert getActivity ().getActionBar () != null;
                getActivity ().getActionBar ().setDisplayHomeAsUpEnabled (false);
                getActivity ().onBackPressed ();
                return true;
            }
            default: return super.onOptionsItemSelected (item);
        }
    }
}
