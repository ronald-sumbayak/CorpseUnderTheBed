package ra.sumbayak.corpseunderthebed.datas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ra.sumbayak.corpseunderthebed.rv.models.NoteModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.ChatMessageModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.InfoMessageModel;
import ra.sumbayak.corpseunderthebed.rv.models.chats.NormalMessageModel;

public class RoomData implements Serializable {
    
    public static final int TYPE_PRIVATE = 0;
    public static final int TYPE_GROUP = 1;
    
    private boolean mOnChoices = false;
    private int mMemberCount = 1, mRoomType = TYPE_PRIVATE;
    private List<ChatMessageModel> mMessageList = new ArrayList<> ();
    private List<NoteModel> mNotes = new ArrayList<> ();
    private List<String> mMembers = new ArrayList<> ();
    private String mRoom;
    
    public RoomData (String room) {
        mRoom = room;
        mMessageList.add (new InfoMessageModel ("new game"));
    }
    
    public RoomData addMessage (ChatMessageModel newMessage) {
        if (newMessage instanceof NormalMessageModel)
        {
            if (messageAt (-1) instanceof NormalMessageModel) {
                NormalMessageModel prevMessage;
                prevMessage = (NormalMessageModel) messageAt (-1);
    
                if (prevMessage != null) {
                    if (((NormalMessageModel) newMessage).sender ().equals (prevMessage.sender ())) {
                        ((NormalMessageModel) newMessage).setConsecutive (true);
                    }
                }
            }
        }
        
        mMessageList.add (newMessage);
        return this;
    }
    
    public void removeMessage (int index) {
        if (mMessageList.size () > 0) {
            if (index == -1) mMessageList.remove (mMessageList.size () - 1);
            else mMessageList.remove (index);
        }
    }
    
    public RoomData addNote (NoteModel newNote) {
        mNotes.add (newNote);
        return this;
    }
    
    public ChatMessageModel messageAt (int index) {
        ChatMessageModel msg = null;
        
        try {
            msg = mMessageList.get (index);
        }
        catch (IndexOutOfBoundsException e) {
            if (mMessageList.size () > 0 && index == -1) {
                msg = mMessageList.get (mMessageList.size () - 1);
            }
        }
        
        return msg;
    }
    
    public String room () {
        return mRoom;
    }
    
    public int messageSize () {
        return mMessageList.size ();
    }
    
    public int type () {
        return mRoomType;
    }
    
    public int memberCount () {
        return mMemberCount;
    }
    
    public int unreadCount () {
        int unreadCount = 0;
        
        for (int i = mMessageList.size ()-1; i >= 0; i--) {
            if (mMessageList.get (i).type () == ChatMessageModel.TYPE_NORMAL) {
                NormalMessageModel msg;
                msg = (NormalMessageModel) mMessageList.get (i);
                
                if (msg.isRead ()) break;
                unreadCount++;
            }
        }
    
        return unreadCount;
    }
    
    public boolean isOnChoices () {
        return mOnChoices;
    }
    
    public void setOnChoices (boolean onChoices) {
        mOnChoices = onChoices;
    }
    
    public void setAsGroup (int memberCount, List<String> memberList) {
        mRoomType = TYPE_GROUP;
        mMemberCount = memberCount;
        mMembers = memberList;
    }
    
    public List<NoteModel> notes () {
        return mNotes;
    }
    
    public List<String> members () {
        return mMembers;
    }
}
