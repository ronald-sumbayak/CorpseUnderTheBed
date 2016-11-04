package ra.sumbayak.corpseunderthebed.rv.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.rv.viewholders.NoteViewHolder;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    
    private RoomData mRoomData;
    
    public NoteAdapter (RoomData roomData) {
        mRoomData = roomData;
    }
    
    @Override
    public NoteViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        inflater = LayoutInflater.from (parent.getContext ());
        
        View itemView;
        itemView = inflater.inflate (R.layout.itemview_note, parent, false);
        return new NoteViewHolder (itemView);
    }
    
    @Override
    public void onBindViewHolder (NoteViewHolder holder, int position) {
        holder.bind (mRoomData.getNote (position));
    }
    
    @Override
    public int getItemCount () {
        return mRoomData.noteSize ();
    }
    
    public void refreshNote (RoomData roomData) {
        if (roomData.noteSize () > mRoomData.noteSize ()) {
            mRoomData = roomData;
            notifyItemInserted (roomData.noteSize () - 1);
        }
    }
}
