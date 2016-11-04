package ra.sumbayak.corpseunderthebed.rv.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.rv.models.NoteModel;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    
    private TextView mAuthor, mDate, mTime, mBody;
    
    public NoteViewHolder (View itemView) {
        super (itemView);
        mAuthor = (TextView) itemView.findViewById (R.id.noteAuthor);
        mDate = (TextView) itemView.findViewById (R.id.noteDate);
        mTime = (TextView) itemView.findViewById (R.id.noteTime);
        mBody = (TextView) itemView.findViewById (R.id.noteBody);
    }
    
    public void bind (NoteModel note) {
        mAuthor.setText (note.getAuthor ());
        mDate.setText (note.getDate ());
        mTime.setText (note.getTime ());
        mBody.setText (note.getBody ());
    }
}
