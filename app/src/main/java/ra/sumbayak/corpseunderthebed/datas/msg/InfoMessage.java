package ra.sumbayak.corpseunderthebed.datas.msg;

import java.io.Serializable;

public abstract class InfoMessage extends Message implements Serializable {
    
    public static final String CATEGORY_INVITATION = "invitation";
    
    public InfoMessage (String room) {
        super (room);
    }
    
    public abstract String getInfoCategory ();
    
    @Override
    public String getMessageType () {
        return TYPE_INFO;
    }
    
    @Override
    public String getSender () {
        return "system";
    }
    
    @Override
    public String getText () {
        return null;
    }
    
    @Override
    public String getTime () {
        return null;
    }
}
