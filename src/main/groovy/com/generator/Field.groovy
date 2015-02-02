package com.generator

import org.apache.commons.lang3.text.WordUtils
import org.skife.jdbi.v2.StatementContext

import java.sql.ResultSet
import java.sql.Timestamp

/**
 * Created by bpeel on 2/2/15.
 */
class Field {

    private String name
    private String dbName
    private String dataType

    public Field(String name, String dataType){
        this.dbName = name
        this.dataType = getDataType(dataType)
        this.name = Mapping.mapColumn(name)
    }

    public String print(){
        Printer out = new Printer()

        String jsonname = dbName.toLowerCase()
        out.print("\t").println("@JsonProperty(\"$jsonname\")")
        out.print("\t").print(dataType).print(" ").println(name)

        return out.toString()
    }

    public String printConversion(){
        String type = WordUtils.capitalize(dataType)
        String conversion = "r.get$type(\"$dbName\")"

        if (type == "DateTime"){
            ResultSet rs;

            Timestamp time =
        }

        return "obj.$name = (!columns.contains($dbName) ? $conversion : null)"
    }

    private String getDataType(String dbDataType){
        switch(dbDataType){
            case "bigint" : "long"; break;
            case "boolean" : "boolean"; break;
            case "character varying" : "String"; break;
            case "text" : "String"; break;
            case "integer" : "int"; break;
            case "smallint" : "int"; break;
            case "numeric" : "Float"; break;
            case "timestamp with time zone" : "DateTime"; break;
            case "timestamp without time zone" : "DateTime"; break;
            default: "Object"
        }
    }
}
