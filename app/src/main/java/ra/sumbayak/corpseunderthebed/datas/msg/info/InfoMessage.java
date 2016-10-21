package ra.sumbayak.corpseunderthebed.datas.msg.info;

import java.io.Serializable;

import ra.sumbayak.corpseunderthebed.datas.msg.Message;

public abstract class InfoMessage extends Message implements Serializable {
    
    public static final String CATEGORY_INVITATION = "invitation";
    
    public InfoMessage (String room) {
        super (room, "system");
    }
    
    @Override
    public String getMessageType () {
        return TYPE_INFO;
    }
    
    public abstract String getInfoCategory ();
}
