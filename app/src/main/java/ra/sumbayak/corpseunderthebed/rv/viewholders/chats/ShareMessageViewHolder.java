package ra.sumbayak.corpseunderthebed.rv.viewholders.chats;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.datas.messages.Post;
import ra.sumbayak.corpseunderthebed.fragments.PostFragment;
import ra.sumbayak.corpseunderthebed.rv.models.chats.ShareMessageModel;

public class ShareMessageViewHolder extends NormalMessageViewHolder {
    
    private TextView mAuthor;
    private View mBody, mDivider;
    private MainActivity mContext;
    
    public ShareMessageViewHolder (final View itemView) {
        super (itemView);
        mAuthor = (TextView) itemView.findViewById (R.id.msgAuthor);
        mDivider = itemView.findViewById (R.id.sharemsg_Divider);
        mBody = (View) mDivider.getParent ();
        mContext = (MainActivity) itemView.getContext ();
    }
    
    private PostFragment buildPostFragment (Post post) {
        Bundle bundle;
        bundle = new Bundle ();
        bundle.putSerializable ("POST_CONTENT", post);
        
        PostFragment postFragment;
        postFragment = new PostFragment ();
        postFragment.setArguments (bundle);
        return postFragment;
    }
    
    @Override
    public void bind (RoomData roomData, final int position) {
        super.bind (roomData, position);
        final ShareMessageModel msg;
        msg = (ShareMessageModel) roomData.messageAt (position);
        mAuthor.setText (msg.post ().author ());
        mText.setText (msg.post ().body ());
        
        mText.measure (-2, -2); // (-2) for wrap_content
        int margin = ((RelativeLayout.LayoutParams) mText.getLayoutParams ()).rightMargin;
        mDivider.getLayoutParams ().width = mText.getMeasuredWidth () + (2 * margin);
        
        mBody.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                FragmentTransaction ft;
                ft = mContext.getSupportFragmentManager ().beginTransaction ();
                
                PostFragment postFragment;
                postFragment = buildPostFragment (msg.post ());
                postFragment.show (ft, "PostPopUp");
            }
        });
    }
}
