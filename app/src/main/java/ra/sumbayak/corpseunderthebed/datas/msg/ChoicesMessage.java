package ra.sumbayak.corpseunderthebed.datas.msg;

import java.io.Serializable;
import java.util.List;

public class ChoicesMessage extends NormalMessage implements Serializable {
    
    private List<Choices> mChoicesList;
    private String mSaveAs;
    
    public ChoicesMessage (String room, String time, String saveAs, List<Choices> choicesList) {
        super (room, "user", time, null);
        mChoicesList = choicesList;
        mSaveAs = saveAs;
    }
    
    @Override
    public String getMessageType () {
        return TYPE_CHOICES;
    }
    
    @Override
    public String getSender () {
        return "user";
    }
    
    @Override
    public String getText () {
        return null;
    }
    
    public Choices getChoicesAt (int index) {
        return mChoicesList.get (index);
    }
    
    public int getChoicesSize () {
        return mChoicesList.size ();
    }
}