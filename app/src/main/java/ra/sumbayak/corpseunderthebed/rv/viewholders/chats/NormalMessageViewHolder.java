package ra.sumbayak.corpseunderthebed.rv.viewholders.chats;

import android.view.View;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.rv.models.chats.NormalMessageModel;

public class NormalMessageViewHolder extends ChatMessageViewHolder {
    
    private TextView mSender, mTime;
    TextView mText;
    
    public NormalMessageViewHolder (View itemView) {
        super (itemView);
        mSender = (TextView) itemView.findViewById (R.id.msgSender);
        mText = (TextView) itemView.findViewById (R.id.msgText); 
        mTime = (TextView) itemView.findViewById (R.id.msgTime);
    }
    
    @Override
    public void bind (RoomData roomData, int position) {
        NormalMessageModel msg;
        msg = (NormalMessageModel) roomData.messageAt (position);
        mSender.setText (msg.sender ());
        mSender.setVisibility (msg.isConsecutive () ? View.GONE : View.VISIBLE);
        mTime.setText (msg.time ());
        mText.setText (msg.text ());
    }
}
