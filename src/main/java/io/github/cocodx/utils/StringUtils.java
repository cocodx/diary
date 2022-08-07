package io.github.cocodx.utils;

/**
 * @author amazfit
 * @date 2022-08-02 上午6:31
 **/
public class StringUtils {

    public static Boolean isNotEmpty(String str){
        return str!=null && str.trim().length()>0;
    }

    public static Boolean isEmpty(Object str){
        if (str==null){
            return true;
        }
        return false;
    }
}
