package ra.sumbayak.corpseunderthebed.datas.msg;

import java.io.Serializable;

public class ChosenChoices implements Serializable {
    
    public Integer begin, end, max;
    
    public ChosenChoices (Integer begin, Integer end, Integer max) {
        this.begin = begin;
        this.end = end;
        this.max = max;
    }
}
