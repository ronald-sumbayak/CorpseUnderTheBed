package ra.sumbayak.corpseunderthebed.rv.viewholders.chats;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.fragments.NoteFragment;
import ra.sumbayak.corpseunderthebed.animations.Fading;
import ra.sumbayak.corpseunderthebed.rv.models.chats.NoteMessageModel;

public class NoteMessageViewHolder extends NormalMessageViewHolder {
    
    private TextView mAuthor;
    private int mNoteIndex;
    private View mBody, mDivider;
    private MainActivity mContext;
    
    public NoteMessageViewHolder (View itemView) {
        super (itemView);
        mAuthor = (TextView) itemView.findViewById (R.id.msgAuthor);
        mDivider = itemView.findViewById (R.id.notemsg_Divider);
        mBody = (View) mDivider.getParent ();
        mContext = (MainActivity) itemView.getContext ();
    }
    
    @Override
    public void bind (final RoomData roomData, int position) {
        super.bind (roomData, position);
        NoteMessageModel msg;
        msg = (NoteMessageModel) roomData.messageAt (position);
        
        mAuthor.setText (msg.author ());
        mText.setText (msg.body ());
        mNoteIndex = msg.noteIndex ();
    
        mText.measure (-2, -2); // (-2) for wrap_content
        int margin = ((RelativeLayout.LayoutParams) mText.getLayoutParams ()).rightMargin;
        mDivider.getLayoutParams ().width = mText.getMeasuredWidth () + (2 * margin);
        
        mBody.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Fading fading;
                fading = new Fading (mContext.findViewById (R.id.fade_interpolator)) {
                    @Override
                    public void onFadeInEnd () {
                        NoteFragment noteFragment;
                        noteFragment = buildNoteFragment (roomData.room ());
                        putNoteFragment (noteFragment, roomData.room ());
                    }
                };
                fading.run ();
            }
        });
    }
    
    private NoteFragment buildNoteFragment (String room) {
        Bundle bundle;
        bundle = new Bundle ();
        bundle.putString ("cutb.ROOM", room);
        bundle.putInt ("cutb.NOTE_INDEX", mNoteIndex);
    
        NoteFragment noteFragment;
        noteFragment = new NoteFragment ();
        noteFragment.setArguments (bundle);
        return noteFragment;
    }
    
    private void putNoteFragment (NoteFragment noteFragment, String room) {
        FragmentTransaction ft;
        ft = mContext.getSupportFragmentManager ().beginTransaction ();
        ft.addToBackStack (null);
        ft.replace (R.id.fragment_view, noteFragment, "NoteList." + room);
        ft.commit ();
    }
}
