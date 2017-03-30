package com.sola.baselib.util;

/**
 * Created by Administrator on 2016/12/30.
 */
public class EqualsUtil {
    
    public static boolean equals(Object o1, Object o2){
        if(o1 == null || o2 == null){
            return o1 == o2;
        }
        return o1.equals(o2);
    }
}
