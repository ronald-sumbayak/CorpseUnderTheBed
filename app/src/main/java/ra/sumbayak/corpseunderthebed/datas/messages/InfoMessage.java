package ra.sumbayak.corpseunderthebed.datas.messages;

public abstract class InfoMessage extends Message implements java.io.Serializable {
    
    public static final String CATEGORY_INVITATION = "invitation";
    
    InfoMessage (String room) {
        super (room, "system");
    }
    
    @Override
    public String type () {
        return TYPE_INFO;
    }
    
    public abstract String category ();
}
