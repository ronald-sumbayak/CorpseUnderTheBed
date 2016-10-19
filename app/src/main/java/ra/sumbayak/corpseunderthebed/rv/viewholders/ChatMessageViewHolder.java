package ra.sumbayak.corpseunderthebed.rv.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    
    private TextView mSender, mText, mReadCount, mTime;
    
    public ChatMessageViewHolder (View itemView) {
        super (itemView);
        mSender = (TextView) itemView.findViewById (R.id.cmSender);
        mText = (TextView) itemView.findViewById (R.id.cmText);
        mReadCount = (TextView) itemView.findViewById (R.id.cmReadCount);
        mTime = (TextView) itemView.findViewById (R.id.cmTime);
    }
    
    public TextView getSender () { return mSender; }
    public TextView getText () { return mText; }
    public TextView getReadCount () { return mReadCount; }
    public TextView getTime () { return mTime; }
}
