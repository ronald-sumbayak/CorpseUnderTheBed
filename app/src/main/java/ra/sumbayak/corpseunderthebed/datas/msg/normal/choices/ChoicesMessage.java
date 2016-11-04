package ra.sumbayak.corpseunderthebed.datas.msg.normal.choices;

import java.io.Serializable;
import java.util.List;

import ra.sumbayak.corpseunderthebed.datas.msg.Message;

public class ChoicesMessage extends Message implements Serializable {
    
    private List<Choices> mChoicesList;
    private String mSaveAs;
    
    public ChoicesMessage (String room, String saveAs, List<Choices> choicesList) {
        super (room, "system");
        mChoicesList = choicesList;
        mSaveAs = saveAs;
    }
    
    public Choices getChoicesAt (int index) {
        return mChoicesList.get (index);
    }
    
    public int choicesSize () {
        return mChoicesList.size ();
    }
    
    @Override
    public String getMessageType () {
        return TYPE_CHOICES;
    }
}