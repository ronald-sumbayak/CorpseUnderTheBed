package ra.sumbayak.corpseunderthebed.datas.msg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvitationMessage extends InfoMessage implements Serializable {
    
    private Integer mMemberCount;
    private List<String> mMemberList = new ArrayList<> ();
    
    public InvitationMessage (String room, Integer memberCount, String[] memberList) {
        super (room);
        mMemberCount = memberCount;
        Collections.addAll (mMemberList, memberList);
    }
    
    @Override
    public String getInfoCategory () {
        return CATEGORY_INVITATION;
    }
    
    public Integer getMemberCount () {
        return mMemberCount;
    }
    
    public List<String> getMemberList () {
        return mMemberList;
    }
}
