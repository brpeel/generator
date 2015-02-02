package com.generator

import java.sql.ResultSetMetaData

/**
 * Created by bpeel on 2/2/15.
 */
class Table {

    private String name
    private String dbName
    private ArrayList<Field> fields = new ArrayList<>()


    public Table(String name){
        this.dbName = name
        this.name = Mapping.mapTable(name)
        name = (name.endsWith("DO") ? name : name+"DO")
    }

    public void add(Field column){
        fields.add(column)
    }

    public String getName(){
        return name
    }

    public String printDO(){
        Printer out = new Printer()

        out.println("public class $name{")
        fields.each {Field c -> out.println(c.print()) }
        out.println("}")
        return out.toString()
    }

    public String printMapper(){
        Printer out = new Printer()

        String clazz = name+"Mapper"
        out.println("public class $clazz implements ResultSetMapper<$name>{")
        out.println()
        out.println("\tprivate Set<String> columns;")
        out.println()
        printMapperGetFields(out)
        out.println()
        printMapperMap(out)
        out.println()
        out.println("}")

        return out.toString()
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
