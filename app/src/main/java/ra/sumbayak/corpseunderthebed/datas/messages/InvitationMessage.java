package ra.sumbayak.corpseunderthebed.datas.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvitationMessage extends InfoMessage implements Serializable {
    
    private final List<String> mMembers = new ArrayList<> ();
    
    public InvitationMessage (String room, String[] members) {
        super (room);
        Collections.addAll (mMembers, members);
    }
    
    public List<String> members () {
        return mMembers;
    }
    
    @Override
    public String category () {
        return CATEGORY_INVITATION;
    }
    
    @Override
    public String notificationMessage () {
        return "You invited to group " + room ();
    }
}
