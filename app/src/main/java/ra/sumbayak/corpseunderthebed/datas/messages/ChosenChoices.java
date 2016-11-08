package ra.sumbayak.corpseunderthebed.datas.messages;

import java.io.Serializable;

public class ChosenChoices implements Serializable {
    
    public int mBegin, mEnd, mMax;
    
    public ChosenChoices (int begin, int end, int max) {
        mBegin = begin;
        mEnd = end;
        mMax = max;
    }
}
