package ra.sumbayak.corpseunderthebed.datas.msg;

import java.io.Serializable;

public class ChosenChoices implements Serializable {
    
    public int mBegin, mEnd, mMax;
    
    public ChosenChoices (int begin, int end, int max) {
        mBegin = begin;
        mEnd = end;
        mMax = max;
    }
}
