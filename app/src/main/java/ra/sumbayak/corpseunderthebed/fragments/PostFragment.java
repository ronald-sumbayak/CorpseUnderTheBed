package ra.sumbayak.corpseunderthebed.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.messages.Post;

public class PostFragment extends DialogFragment {
    
    private Window mWindow;
    
    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWindow = getDialog ().getWindow ();
        assert mWindow != null;
        mWindow.requestFeature (Window.FEATURE_NO_TITLE);
        
        View view;
        view = inflater.inflate (R.layout.fragment_post, container, true);
        return view;
    }
    
    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        Post post = (Post) getArguments ().getSerializable ("POST_CONTENT");
        
        assert post != null;
        ((TextView) view.findViewById (R.id.postAuthor)).setText (post.author ());
        ((TextView) view.findViewById (R.id.postDate)).setText (post.date ());
        ((TextView) view.findViewById (R.id.postTime)).setText (post.time ());
        ((TextView) view.findViewById (R.id.postBody)).setText (post.body ());
    }
    
    @Override
    public void onResume () {
        mWindow.setLayout (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mWindow.setBackgroundDrawable (new ColorDrawable (Color.TRANSPARENT));
        setStyle (STYLE_NO_FRAME, android.R.style.Theme);
        super.onResume ();
    }
}
