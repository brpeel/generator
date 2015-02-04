package com.generator

import org.apache.commons.lang3.text.WordUtils
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean

/**
 * Created by bpeel on 2/2/15.
 */
class Table {

    private String name
    private String dbName
    private ArrayList<Field> fields = new ArrayList<>()


    public Table(String dbname, String name){
        this.dbName = dbname
        this.name = (name.endsWith("DO") ? name : name+"DO")
    }

    public void add(Field column){
        fields.add(column)
    }

    public String getName(){
        return name
    }

    public String getDBIName(){

        String clazz = name
        if (clazz.endsWith("DO") || clazz.endsWith("Do") || clazz.endsWith("do"))
            clazz = clazz.substring(0, clazz.length()-2)
        return clazz
    }

    public String printDO(String pkg){

        Printer fieldPrinter = new Printer()
        boolean includeJodaImport = false
        fields.each {Field c ->
            fieldPrinter.println(c.print())
            includeJodaImport = true || c.isDate()
        }


        Printer out = new Printer()
        out.println("package $pkg")
        out.println()
        out.println("import com.fasterxml.jackson.annotation.JsonProperty")
        if(includeJodaImport)
            out.println("import org.joda.time.DateTime")
        out.println()
        out.println("public class $name{")
        out.println(fieldPrinter.toString())
        out.println("}")
        return out.toString()
    }

    public String printMapper(String pkg, String doPkg){
        Printer out = new Printer()

        String objName = WordUtils.uncapitalize(getDBIName())

        String clazz = name+"Mapper"
        out.println("package $pkg")
        out.println()
        out.println("import $pkg"+".BaseMapper")
        out.println("import $doPkg"+".$name")
        out.println()
        out.println("public class $clazz extends BaseMapper<$name>{")
        out.println()
        out.print("\t").println("@Override")
        out.print("\t").println("protected $name map(Map data) {")
        out.println()
        out.print("\t\t").println("$name $objName = new $name()")
        out.println()
        fields.each {Field c ->
            String getValue = "getValue(\"$c.dbColumnName\")"
            if (c.dataType == "DateTime")
                getValue = "getDateValue(\"$c.dbColumnName\")"

            out.print("\t\t").println(objName+"."+c.name+" = $getValue")
        }
        out.println()
        out.println("\t\treturn $objName")
        out.println("\t}")
        out.println()
        out.println("}")

        return out.toString()
    }

    public String printDBI(String pkg, String mapperPkg, String doPkg){
        Printer out = new Printer()

        String clazz = getDBIName()

        clazz = clazz+"DBI"
        String mapper = name+"Mapper"
        out.println("package $pkg")
        out.println()
        out.println("import $mapperPkg"+".$mapper")
        out.println("import $doPkg"+".$name")
        out.println()
        out.println("import org.skife.jdbi.v2.sqlobject.Bind")
        out.println("import org.skife.jdbi.v2.sqlobject.BindBean")
        out.println("import org.skife.jdbi.v2.sqlobject.SqlQuery")
        out.println("import org.skife.jdbi.v2.sqlobject.SqlUpdate")
        out.println("import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper")
        out.println("import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean")
        out.println()
        out.println("@RegisterMapper($mapper"+".class)")
        out.println("interface $clazz{")
        out.println()
        writeSelect(out)
        out.println()
        writeInsert(out)
        out.println()
        writeUpdate(out)
        out.println("}")

        return out.toString()
    }

    private String writeSelect(Printer out){
        String offset = "\t"
        StringBuilder fieldList = new StringBuilder()
        int count = 0
        fields.each {Field f ->
            if (f.name != "id") {
                if (count % 5 == 0 && count != 0)
                    fieldList.append(" \\\n").append(offset)

                if (count != 0)
                    fieldList.append(",")
                fieldList.append(" :$f.name")
                count++
            }
        }

        out.print(offset).println("@SqlQuery(\"select $fieldList from $dbName where id = :id\")")
        out.print(offset).println("@MapResultAsBean")
        out.print(offset).println("$name get(@Bind(\"id\") int id)")
    }

    private String writeInsert(Printer out){
        String offset = "\t"
        StringBuilder columns = new StringBuilder()
        StringBuilder values = new StringBuilder()
        int count = 0;
        fields.each {Field f ->
            if (f.name != "id") {
                if (count % 4 == 0 && count != 0) {
                    values.append(" \\\n").append(offset)
                    columns.append(" \\\n").append(offset)
                }
                if (count != 0) {
                    values.append(",")
                    columns.append(",")
                }
                values.append(" :$f.name")
                columns.append(" :$f.name")
                count++
            }
        }

        out.print(offset).println("@SqlQuery(\"insert into $name ($columns) \\\n$offset values ($values) \\\n$offset returning id\")")
        out.print(offset).println("int insert(@BindBean $name doBean)")
    }

    private String writeUpdate(Printer out){
        String offset = "\t"
        StringBuilder fieldList = new StringBuilder()
        int count = 0
        fields.each {Field f ->
            if (f.name != "id") {
                if (count % 4 == 0 && count != 0)
                    fieldList.append(" \\\n").append(offset)

                if (count != 0)
                    fieldList.append(",")
                fieldList.append(" $f.dbColumnName = :$f.name")

                count++
            }
        }

        out.print(offset).println("@SqlUpdate(\"update $name set $fieldList where id = :id\")")
        out.print(offset).println("int update(@BindBean $name doBean)")
    }

    private String printMapperGetFields(Printer out){
        out.print("\t").println("private void loadFields(ResultSet r) throws SQLException {")
        out.println()
        out.print("\t\t").println("if (columns)")
        out.print("\t\t\t").println("return")
        out.println()
        out.print("\t\t").println("ResultSetMetaData rsmd = r.getMetaData()")
        out.println()
        out.print("\t\t").println("columns = new HashSet<String>()")
        out.print("\t\t").println("for (int i = 1; i<=rsmd.columnCount; i++)")
        out.print("\t\t\t").println("columns.put(rsmd.getColumnName(i))")
        out.println()
        out.print("\t\t").println("return columns")
        out.println("\t}")//End GetFields
    }

    private String printMapperMap(Printer out){
        out.print("\t").println("public $name map(int index, ResultSet r, StatementContext ctx) throws SQLException {")
        out.println()
        out.print("\t\t").println("loadFields()")
        out.println()
        out.print("\t\t").println("$name obj = new $name()")
        out.println()
        fields.each {Field c -> out.print("\t\t").println(c.printConversion())}
        out.println()
        out.print("\t\t").println("return obj")
        out.println("\t}")//End map
    }
}
