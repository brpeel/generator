package com.generator

import org.apache.commons.lang3.text.WordUtils

/**
 * Created by bpeel on 2/2/15.
 */
enum Mapping {

    INSTANCE

    private Map<String,String> tables
    private Map<String,String> columns

    private Mapping(){

    }

    public static void load(Map<String,String> tables, Map<String,String> columns){
        Mapping.INSTANCE.tables = tables
        Mapping.INSTANCE.columns = columns
    }

    private String mapEntity(String name, Map map, Closure properCase){
        String camelCase = properCase(name)
        if (map) {
            if (map.get(name))
                return map.get(name)
            if (map.get(camelCase))
                return map.get(camelCase)
        }
        return camelCase
    }

    public static String mapTable(String name){
        return Mapping.INSTANCE.mapEntity(name, Mapping.INSTANCE.tables, {it -> Word.classCase(it)})
    }

    public static String mapColumn(String name){
        return Mapping.INSTANCE.mapEntity(name, Mapping.INSTANCE.columns, {it -> Word.memberCase(it)})
    }
}
