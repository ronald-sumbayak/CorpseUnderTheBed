package ra.sumbayak.corpseunderthebed.rv.viewholders.chats;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import ra.sumbayak.corpseunderthebed.activities.MainActivity;
import ra.sumbayak.corpseunderthebed.datas.messages.Post;
import ra.sumbayak.corpseunderthebed.fragments.PostFragment;

public class ShareMessageViewHolder extends NoteMessageViewHolder {
    
    private Post mPost;
    
    public ShareMessageViewHolder (final View itemView) {
        super (itemView);
        mText.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                FragmentTransaction ft;
                ft = ((MainActivity) itemView.getContext ()).getSupportFragmentManager ().beginTransaction ();
                
                PostFragment postFragment;
                postFragment = buildPostFragment ();
                postFragment.show (ft, "PostPopUp");
            }
        });
    }
    
    private PostFragment buildPostFragment () {
        Bundle bundle;
        bundle = new Bundle ();
        bundle.putSerializable ("POST_CONTENT", mPost);
        
        PostFragment postFragment;
        postFragment = new PostFragment ();
        postFragment.setArguments (bundle);
        return postFragment;
    }
}
