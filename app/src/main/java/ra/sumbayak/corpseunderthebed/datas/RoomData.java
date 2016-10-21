package ra.sumbayak.corpseunderthebed.datas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ra.sumbayak.corpseunderthebed.rv.models.NoteModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.ChatMessageModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.NormalMessageModel;

public class RoomData implements Serializable {
    
    public static int TYPE_PRIVATE = 0;
    public static int TYPE_GROUP = 1;
    
    private boolean mOnChoices = false;
    private int mMemberCount = 1, mRoomType = TYPE_PRIVATE;
    private List<ChatMessageModel> mMessageList = new ArrayList<> ();
    private List<NoteModel> mNoteList = new ArrayList<> ();
    private List<String> mMemberList = new ArrayList<> ();
    private String mRoom;
    
    public RoomData (String room) {
        mRoom = room;
    }
    
    public int getMemberCount () {
        return mMemberCount;
    }
    
    public String getRoom () {
        return mRoom;
    }
    
    public int getRoomType () {
        return mRoomType;
    }
    
    public void addNewMessage (ChatMessageModel newMessage) {
        if (newMessage.getMessageType () == ChatMessageModel.MESSAGE_TYPE_NORMAL) {
            NormalMessageModel prevMessage;
            prevMessage = (NormalMessageModel) getMessageAt (-1);
    
            if (prevMessage != null) {
                if (((NormalMessageModel) newMessage).getSender ().equals (prevMessage.getSender ())) {
                    ((NormalMessageModel) newMessage).setConsecutive (true);
                }
            }
        }
        
        mMessageList.add (newMessage);
    }
    
    public void addNewNote (NoteModel newNote) {
        mNoteList.add (newNote);
    }
    
    public ChatMessageModel getMessageAt (int index) {
        ChatMessageModel msg = null;
        
        try {
            msg = mMessageList.get (index);
        }
        catch (IndexOutOfBoundsException e) {
            if (mMessageList.size () > 0) {
                msg = mMessageList.get (mMessageList.size () - 1);
            }
        }
        
        return msg;
    }
    
    public int getMessageSize () {
        return mMessageList.size ();
    }
    
    public int getUnreadCount () {
        int unreadCount = 0;
        
        for (int i = mMessageList.size ()-1; i >= 0; i--) {
            if (mMessageList.get (i).getMessageType () == ChatMessageModel.MESSAGE_TYPE_NORMAL) {
                NormalMessageModel msg;
                msg = (NormalMessageModel) mMessageList.get (i);
                
                if (msg.isRead ()) unreadCount++;
                else break;
            }
        }
    
        return unreadCount;
    }
    
    public void setOnChoices (Boolean onChoices) {
        mOnChoices = onChoices;
    }
    
    public boolean isOnChoices () {
        return mOnChoices;
    }
    
    public void setRoomAsGroup (int memberCount, List<String> memberList) {
        mRoomType = TYPE_GROUP;
        mMemberCount = memberCount;
        mMemberList = memberList;
    }
    
    public List<String> getMemberList () {
        return mMemberList;
    }
}
