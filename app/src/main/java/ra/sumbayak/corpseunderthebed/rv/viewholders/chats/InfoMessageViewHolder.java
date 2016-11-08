package ra.sumbayak.corpseunderthebed.rv.viewholders.chats;

import android.view.View;
import android.widget.TextView;

import ra.sumbayak.corpseunderthebed.R;
import ra.sumbayak.corpseunderthebed.datas.RoomData;

public class InfoMessageViewHolder extends ChatMessageViewHolder {
    
    private TextView mText;
    
    public InfoMessageViewHolder (View itemView) {
        super (itemView);
        mText = (TextView) itemView.findViewById (R.id.msgText);
    }
    
    @Override
    public void bind (RoomData roomData, int position) {
        mText.setText (roomData.messageAt (position).text ());
        if (roomData.messageAt (position + 1) == null) mText.setVisibility (View.GONE);
    }
}
