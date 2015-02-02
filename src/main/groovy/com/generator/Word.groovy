package com.generator

import org.apache.commons.lang3.text.WordUtils

/**
 * Created by bpeel on 2/2/15.
 */
class Word {

    private static properCase(String str){

        str = str?.replace("_"," ")?.trim()
        if (!str || str.length() == 0)
            return str;

        if (str.contains(" ")) {
            str = WordUtils.capitalize(str.toLowerCase())
            str = str.replaceAll(" ", "")
        }
        return WordUtils.capitalize(str)
    }

    public static String classCase(String str){
        return properCase(str)
    }

    public static String memberCase(String str){
        str = properCase(str)
        return WordUtils.uncapitalize(str)
    }

    public static String jsonCase(String str) {
        if (!str)
            return
        str = str.replaceAll("([A-Z])", " \$1").toLowerCase();

        str = str.replaceAll(" ","_")
        while (str.contains("__"))
            str = str.replaceAll("__", "_")

        return str
    }
}
