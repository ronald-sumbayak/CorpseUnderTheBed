package ra.sumbayak.corpseunderthebed.fragments;

import android.content.Context;
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

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.datas.GameData;
import ra.sumbayak.corpseunderthebed.handlers.IOHandler;
import ra.sumbayak.corpseunderthebed.rv.adapters.NoteAdapter;

public class NoteFragment extends Fragment implements MainActivity.FragmentLink {
    
    private RecyclerView mNoteList;
    private GameData mGameData;
    private String mRoom;
    private int mInitialIndex;
    
    @Override
    public void onAttach (Context context) {
        super.onAttach (context);
        mRoom = getArguments ().getString ("cutb.ROOM");
        mInitialIndex = getArguments ().getInt ("cutb.NOTE_INDEX");
        mGameData = IOHandler.getIOInstance (getActivity ()).loadGameData ();
    }
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate (R.layout.fragment_note, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        setupNoteList ();
        setupToolbar ();
    }
    
    private void setupNoteList () {
        assert getView () != null;
        mNoteList = (RecyclerView) getView ().findViewById (R.id.rvNoteList);
        mNoteList.setLayoutManager (new LinearLayoutManager (getActivity ()));
        mNoteList.setAdapter (new NoteAdapter (mGameData.roomData (mRoom).notes ()));
        mNoteList.scrollToPosition (mInitialIndex);
    }
    
    private void setupToolbar () {
        ActionBar actionBar;
        actionBar = ((AppCompatActivity) getActivity ()).getSupportActionBar ();
        assert actionBar != null;
        actionBar.setTitle ("Notes");
        actionBar.setDisplayHomeAsUpEnabled (true);
        actionBar.setHomeAsUpIndicator (R.drawable.button_back);
        
    }
    
    @Override
    public void onResume () {
        super.onResume ();
        mGameData = IOHandler.getIOInstance (getActivity ()).loadGameData ();
        MainActivity mainActivity;
        mainActivity = (MainActivity) getActivity ();
        mainActivity.setFragmentLink (this);
    
        NoteAdapter adapter;
        adapter = (NoteAdapter) mNoteList.getAdapter ();
        adapter.refresh (mGameData.roomData (mRoom).notes ());
    }
    
    @Override
    public void onBroadcastReceived () {
        
    }
    
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        Log.d ("cutb_debug", "option selected noteFragment" + item.getItemId () + "");
        return super.onOptionsItemSelected (item);
    }
}
