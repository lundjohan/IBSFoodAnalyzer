package com.johanlund.base_classes;

import org.threeten.bp.LocalDateTime;

/**
 * Created by Johan on 2017-10-05.
 */

public class Break {
    LocalDateTime time;

    public Break(LocalDateTime t){
        time = t;
    }
    public LocalDateTime getTime(){
        return time;
    }
}
