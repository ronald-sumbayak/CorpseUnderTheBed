package ra.sumbayak.corpseunderthebed.datas;

import android.support.annotation.IntDef;
import android.view.View;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import ra.sumbayak.corpseunderthebed.rv.models.ChatMessageModel;

public class RoomData implements Serializable {
    
    public static int TYPE_PRIVATE = 0;
    public static int TYPE_GROUP = 1;
    
    private Boolean mOnChoices = false;
    private Integer mMemberCount = 1, mRoomType = TYPE_PRIVATE;
    private List<ChatMessageModel> mMessageList = new ArrayList<> ();
    private List<String> mMemberList = new ArrayList<> ();
    private String mRoom;
    
    public RoomData (String room) {
        mRoom = room;
    }
    
    public Integer getMemberCount () {
        return mMemberCount;
    }
    
    public String getRoom () {
        return mRoom;
    }
    
    public int getRoomType () {
        return mRoomType;
    }
    
    public void addNewMessage (ChatMessageModel newMessage) {
        ChatMessageModel prevMessage;
        prevMessage = getMessageAt (-1);
        
        if (prevMessage != null) {
            if (newMessage.getSender ().equals (prevMessage.getSender ())) {
                newMessage.setConsecutive (true);
            }
        }
        
        mMessageList.add (newMessage);
    }
    
    public ChatMessageModel getMessageAt (int index) {
        try {
            ChatMessageModel msg;
            msg = mMessageList.get (index);
            return msg;
        }
        catch (IndexOutOfBoundsException e) {
            if (mMessageList.size () == 0) {
                return null;
            }
            else {
                ChatMessageModel msg;
                msg = mMessageList.get (mMessageList.size () - 1);
                return msg;
            }
        }
    }
    
    public Integer getMessageSize () {
        return mMessageList.size ();
    }
    
    public Integer getUnreadCount () {
        Integer unreadCount = 0;
        
        for (int i = mMessageList.size ()-1; i >= 0; i--) {
            if (!mMessageList.get (i).isRead ()) {
                unreadCount++;
            }
        }
        
        return unreadCount;
    }
    
    public void setOnChoices (Boolean onChoices) {
        mOnChoices = onChoices;
    }
    
    public Boolean isOnChoices () {
        return mOnChoices;
    }
    
    public void setRoomAsGroup (Integer memberCount, List<String> memberList) {
        mRoomType = TYPE_GROUP;
        mMemberCount = memberCount;
        mMemberList = memberList;
    }
    
    public List<String> getMemberList () {
        return mMemberList;
    }
    
    @Visibility
    public Integer getUnreadCountVisibility () {
        return getFieldVisibility (getUnreadCount ());
    }
    
    @Visibility
    public Integer getMemberCountVisibility () {
        return getFieldVisibility (mMemberCount - 1);
    }
    
    @Visibility
    private Integer getFieldVisibility (Integer field) {
        if (field == 0) return View.INVISIBLE;
        else return View.VISIBLE;
    }
    
    @IntDef ({View.VISIBLE, View.INVISIBLE})
    @Retention (RetentionPolicy.SOURCE)
    private  @interface Visibility {
        
    }
}
