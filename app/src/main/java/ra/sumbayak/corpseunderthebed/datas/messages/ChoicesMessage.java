package ra.sumbayak.corpseunderthebed.datas.messages;

import java.io.Serializable;
import java.util.List;

public class ChoicesMessage extends Message implements Serializable {
    
    private final List<Choice> mChoices;
    private final String mSave;
    
    public ChoicesMessage (String room, String save, List<Choice> choices) {
        super (room, "system");
        mChoices = choices;
        mSave = save;
    }
    
    public Choice getChoices (int index) {
        return mChoices.get (index);
    }
    
    public int choicesSize () {
        return mChoices.size ();
    }
    
    @Override
    public String notificationMessage () {
        return "Give a response to " + room ();
    }
    
    @Override
    public String type () {
        return TYPE_CHOICES;
    }
}