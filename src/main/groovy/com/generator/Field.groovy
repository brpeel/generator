package com.generator
/**
 * Created by bpeel on 2/2/15.
 */
class Field {

    String name
    String dbColumnName
    String dataType

    public Field(String name, String dataType){
        this.dbColumnName = name
        this.dataType = parseDataType(dataType)
        this.name = Mapping.mapColumn(name)
    }

    public String print(){
        Printer out = new Printer()

        String jsonname = dbColumnName.toLowerCase()
        out.print("\t").println("@JsonProperty(\"$jsonname\")")
        out.print("\t").print(dataType).print(" ").println(name)

        return out.toString()
    }

    private String parseDataType(String dbDataType){
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

    public boolean isDate(){
        return dataType == "DateTime"
    }
}
