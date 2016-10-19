package ra.sumbayak.corpseunderthebed.datas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MessageDate implements Serializable {
    
    private static final List<String> DAY_NAME = new ArrayList<> (Arrays.asList (
        "Day", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    ));
    
    private static final List<String> MONTH_NAME = new ArrayList<> (Arrays.asList (
        "Month", "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ));
    
    private Integer mDay, mDate, mMonth;
    
    MessageDate (String date) {
        mDay = DAY_NAME.indexOf (date.split (", ")[0]);
        mMonth = MONTH_NAME.indexOf (date.split (" ")[1]);
        mDate = Integer.valueOf (date.split (" ")[2]);
    }
    
    public String getFullDate () {
        return getDayName () + ", " + getMonthName () + " " + mDate;
    }
    
    private String getDayName () {
        return DAY_NAME.get (mDay);
    }
    
    private String getMonthName () {
        return MONTH_NAME.get (mMonth);
    }
}
