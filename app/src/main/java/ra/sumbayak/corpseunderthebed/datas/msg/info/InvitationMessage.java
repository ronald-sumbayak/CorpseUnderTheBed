package ra.sumbayak.corpseunderthebed.datas.msg.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvitationMessage extends InfoMessage implements Serializable {
    
    private int mMemberCount;
    private List<String> mMemberList = new ArrayList<> ();
    
    public InvitationMessage (String room, int memberCount, String[] memberList) {
        super (room);
        mMemberCount = memberCount;
        Collections.addAll (mMemberList, memberList);
    }
    
    @Override
    public String getInfoCategory () {
        return CATEGORY_INVITATION;
    }
    
    public int getMemberCount () {
        return mMemberCount;
    }
    
    public List<String> getMemberList () {
        return mMemberList;
    }
}
