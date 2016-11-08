package ra.sumbayak.corpseunderthebed.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.animations.BoomerangSlide;
import ra.sumbayak.corpseunderthebed.animations.Fading;
import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.datas.messages.ChoicesMessage;
import ra.sumbayak.corpseunderthebed.handlers.IOHandler;
import ra.sumbayak.corpseunderthebed.rv.adapters.ChatMessageAdapter;
import ra.sumbayak.corpseunderthebed.rv.decorations.VerticalSpacingItemDecoration;
import ra.sumbayak.corpseunderthebed.rv.layoutmanagers.SmoothScrollLayoutManager;
import ra.sumbayak.corpseunderthebed.services.Postman;

public class ChatMessageFragment extends Fragment implements MainActivity.FragmentLink {
    
    private MainActivity mContext;
    private IOHandler mIO;
    private GameData mGameData;
    private String mRoom;
    private LinearLayout mChoicesPanel, mNotificationPanel;
    private RecyclerView mChatMessage;
    private ChatMessageAdapter mAdapter;
    private BottomSheetDialog mPanel;
    private BoomerangSlide mNotification;
    
    @Override
    public void onAttach (Context context) {
        super.onAttach (context);
        mContext = (MainActivity) context;
        mIO = IOHandler.getIOInstance (mContext);
        mRoom = getArguments ().getString ("cutb.ROOM");
        loadGameData ();
    }
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate (R.layout.fragment_chat, container, false);
        return view;
    }
    
    @Override
    public void onResume () {
        super.onResume ();
        setFragmentLink ();
        setupToolbar ();
        refreshChatMessage ();
        mChatMessage.scrollToPosition (mGameData.roomData (mRoom).messageSize ()-1);
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        setupView (view);
        setupChatMessage ();
    }
    
    private void loadGameData () {
        mGameData = mIO.loadGameData ();
    }
    
    private void setupView (View view) {
        mChatMessage = (RecyclerView) view.findViewById (R.id.rvChatMessage);
        mChoicesPanel = (LinearLayout) view.findViewById (R.id.openChoicesButton);
        mNotificationPanel = (LinearLayout) view.findViewById (R.id.panelNotification);
        mNotification = new BoomerangSlide (mNotificationPanel, 750, 2500);
    }
    
    private void setupChatMessage () {
        mChatMessage.setLayoutManager (new SmoothScrollLayoutManager (mContext));
        mChatMessage.addItemDecoration (new VerticalSpacingItemDecoration (8));
        mAdapter = new ChatMessageAdapter (mContext, mGameData.roomData (mRoom));
        mChatMessage.setAdapter (mAdapter);
        mChatMessage.scrollToPosition (mGameData.roomData (mRoom).messageSize () - 1);
        refreshChatMessage ();
    }
    
    private void setupToolbar () {
        ActionBar actionBar;
        actionBar = ((AppCompatActivity) getActivity ()).getSupportActionBar ();
        
        assert actionBar != null;
        actionBar.setTitle (mRoom);
        actionBar.setDisplayHomeAsUpEnabled (true);
        actionBar.setHomeAsUpIndicator (R.drawable.button_back);
    }
    
    @Override
    public void onBroadcastReceived () {
        if (mGameData.inbox ().peek ().room ().equals (mRoom))
            refreshChatMessage ();
        else if (!mNotification.isRunning ())
            showNotification ();
    }
    
    private void showNotification () {
        loadGameData ();
        setNotificationMessage ();
        mNotification.run ();
    }
    
    private void setNotificationMessage () {
        TextView message;
        message = (TextView) mNotificationPanel.findViewById (R.id.text);
        message.setText (mGameData.getInGameNotification ());
    }
    
    private void refreshChatMessage () {
        loadGameData ();
        if (mGameData.roomData (mRoom).notes ().size () > 0) setHasOptionsMenu (true);
        setupChoicesPanel ();
        setOnLayoutChangeListener ();
        mAdapter.refresh (mGameData.roomData (mRoom));
        markMessageAsRead ();
    }
    
    private void markMessageAsRead () {
        // TODO: tell me what to do here arrrrg
    }
    
    private void setOnLayoutChangeListener () {
        mChatMessage.addOnLayoutChangeListener (new View.OnLayoutChangeListener () {
            @Override
            public void onLayoutChange (View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mChatMessage.removeOnLayoutChangeListener (this);
                if (lastVisibleIndex () > mGameData.roomData (mRoom).messageSize ()-4) {
                    mChatMessage.smoothScrollToPosition (mGameData.roomData (mRoom).messageSize () - 1);
                }
            }
        });
    }
    
    private int lastVisibleIndex () {
        LinearLayoutManager layoutManager;
        layoutManager = (LinearLayoutManager) mChatMessage.getLayoutManager ();
        return layoutManager.findLastCompletelyVisibleItemPosition ();
    }
    
    private void setupChoicesPanel () {
        if (mGameData.roomData (mRoom).isOnChoices ()) {
            mChoicesPanel.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    mPanel = new BottomSheetDialog (mContext);
                    mPanel.setContentView (buildChoicesPanel ((ChoicesMessage) mGameData.currentMessage ()));
                    mPanel.show ();
                }
            });
            TransitionManager.beginDelayedTransition (mChoicesPanel);
            mChoicesPanel.setVisibility (View.VISIBLE);
        }
    }
    
    @SuppressLint ("InflateParams")
    private View buildChoicesPanel (ChoicesMessage msg) {
        LayoutInflater inflater;
        inflater = getLayoutInflater (null);
        
        LinearLayout panel;
        panel = (LinearLayout) inflater.inflate (R.layout.view_choices_panel, null);
        int length=0;
        
        for (int i = 0; i < msg.choicesSize (); i++) {
            LinearLayout.LayoutParams params;
            params = new LinearLayout.LayoutParams (-2, -2);
            Button choice;
            choice = makeChoicesButton (inflater, msg.getChoices (i).label (), i);
            panel.addView (choice, params);
            length += choice.getText ().length ();
        }
        
        if (length > 35) panel.setOrientation (LinearLayout.VERTICAL);
        return panel;
    }
    
    @SuppressLint ("InflateParams")
    private Button makeChoicesButton (LayoutInflater inflater, String label, final int position) {
        Button choices;
        choices = (Button) inflater.inflate (R.layout.itemview_choices, null);
        choices.setText (label);
        choices.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                mChoicesPanel.setVisibility (View.GONE);
                mPanel.dismiss ();
                sendChoicesSelection (position);
            }
        });
        return choices;
    }
    
    private void sendChoicesSelection (int position) {
        Intent intent;
        intent = new Intent (mContext, Postman.class);
        intent.setAction ("cutb.CHOICES");
        intent.putExtra ("selection", position);
        mContext.startService (intent);
    }
    
    private void setFragmentLink () {
        mContext.setFragmentLink (this);
    }
    
    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        inflater.inflate (R.menu.menu_chat, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        Log.d ("cutb_debug", "item.id: " + item.getItemId ());
        switch (item.getItemId ()) {
            case android.R.id.home:
                Log.d ("cutb_debug", "back pressed");
                mContext.onBackPressed ();
                return false;
            case R.id.button_open_notes:
                Fading fading;
                fading = new Fading (mContext.findViewById (R.id.fade_interpolator)) {
                    @Override
                    public void onFadeInEnd () {
                        Bundle bundle;
                        bundle = new Bundle ();
                        bundle.putString ("cutb.ROOM", mRoom);
                        bundle.putInt ("cutb.NOTE_INDEX", 0);
            
                        NoteFragment fragment;
                        fragment = new NoteFragment ();
                        fragment.setArguments (bundle);
            
                        FragmentTransaction ft;
                        ft = mContext.getSupportFragmentManager ().beginTransaction ();
                        ft.addToBackStack (null);
                        ft.replace (R.id.fragment_view, fragment, "NoteList." + mRoom);
                        ft.commit ();
                    }
                };
                fading.run ();
                return false;
            default:
                return super.onOptionsItemSelected (item);
        }
    }
}
