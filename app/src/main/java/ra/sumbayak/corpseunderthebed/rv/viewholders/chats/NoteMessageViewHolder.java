package ra.sumbayak.corpseunderthebed.rv.viewholders.chats;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.fragments.NoteFragment;
import ra.sumbayak.corpseunderthebed.rv.models.chats.NoteMessageModel;

public class NoteMessageViewHolder extends NormalMessageViewHolder {
    
    private TextView mAuthor;
    private int mNoteIndex;
    private View mItemView, mDivider;
    
    public NoteMessageViewHolder (View itemView) {
        super (itemView);
        mAuthor = (TextView) itemView.findViewById (R.id.msgAuthor);
        mItemView = itemView;
        mDivider = itemView.findViewById (R.id.notemsg_Divider);
    }
    
    @Override
    public void bind (final RoomData roomData, int position) {
        super.bind (roomData, position);
        NoteMessageModel msg;
        msg = (NoteMessageModel) roomData.getMessageAt (position);
        
        mAuthor.setText (msg.getAuthor ());
        mNoteIndex = msg.getNoteIndex ();
        
        // (-2) for WRAP_CONTENT
        mText.measure (-2, -2);
        int margin = ((RelativeLayout.LayoutParams) mText.getLayoutParams ()).rightMargin;
        mDivider.getLayoutParams ().width = mText.getMeasuredWidth () + (2 * margin);
        
        mItemView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                MainActivity mainActivity;
                mainActivity = (MainActivity) view.getContext ();
    
                NoteFragment noteFragment;
                noteFragment = buildNoteFragment (roomData.getRoom ());
                putNoteFragment (mainActivity, noteFragment, roomData.getRoom ());
            }
        });
    }
    
    private NoteFragment buildNoteFragment (String room) {
        Bundle bundle;
        bundle = new Bundle ();
        bundle.putString ("cutb.NOTE_OPEN_MODE", "POST_QUICK_OPEN");
        bundle.putString ("cutb.ROOM", room);
        bundle.putInt ("cutb.NOTE_INDEX", mNoteIndex);
    
        NoteFragment noteFragment;
        noteFragment = new NoteFragment ();
        noteFragment.setArguments (bundle);
        return noteFragment;
    }
    
    private void putNoteFragment (Context context, NoteFragment noteFragment, String room) {
        FragmentTransaction ft;
        ft = ((MainActivity) context).getSupportFragmentManager ().beginTransaction ();
        ft.addToBackStack (null);
        ft.replace (R.id.fragment_view, noteFragment, "NoteList." + room);
        ft.commit ();
    }
}
