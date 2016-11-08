package ra.sumbayak.corpseunderthebed.rv.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.rv.models.NoteModel;
import ra.sumbayak.corpseunderthebed.rv.viewholders.NoteViewHolder;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    
    private List<NoteModel> mNotes;
    
    public NoteAdapter (List<NoteModel> notes) {
        mNotes = notes;
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
        holder.bind (mNotes.get (position));
    }
    
    @Override
    public int getItemCount () {
        return mNotes.size ();
    }
    
    public void refresh (List<NoteModel> notes) {
        if (notes.size () > mNotes.size ()) {
            mNotes = notes;
            notifyItemInserted (mNotes.size () - 1);
        }
    }
}
