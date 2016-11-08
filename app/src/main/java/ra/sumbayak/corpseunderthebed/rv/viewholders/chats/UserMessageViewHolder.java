package ra.sumbayak.corpseunderthebed.rv.viewholders.chats;

import android.view.View;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.RoomData;
import ra.sumbayak.corpseunderthebed.rv.models.chats.UserMessageModel;

public class UserMessageViewHolder extends NormalMessageViewHolder {
    
    private TextView mReadCount;
    
    public UserMessageViewHolder (View itemView) {
        super (itemView);
        mReadCount = (TextView) itemView.findViewById (R.id.msgReadCount);
    }
    
    @Override
    public void bind (RoomData roomData, int position) {
        super.bind (roomData, position);
        UserMessageModel msg;
        msg = (UserMessageModel) roomData.messageAt (position);
        
        String readText
            = roomData.type () == RoomData.TYPE_GROUP
            ? "Read " + String.valueOf (msg.readCount ())
            : "Read"
        ;
        
        mReadCount.setText (readText);
        mReadCount.setVisibility (msg.readCount () > 0 ? View.VISIBLE : View.INVISIBLE);
    }
}
