package com.generator

import org.apache.tomcat.jdbc.pool.DataSource
import org.apache.tomcat.jdbc.pool.PoolProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by bpeel on 2/2/15.
 */
class DOGenerator extends BaseGenerator{

    final Map tables = ["MemberDO":"mbr_member","AccountDO":"mbr_account"]
    final String database = "jdbc:postgresql://localhost:5432/bsg"
    final String username = "bpeel"
    final String password = "admin"

    final String DATA_PACKAGE = "com.accounting.data"
    final String DOMAIN_PACKAGE = DATA_PACKAGE+".domain"
    final String MAPPER_PACKAGE = DATA_PACKAGE+".mapper"
    final String DBI_PACKAGE = DATA_PACKAGE+".dbi"

    Map<String,Table> doClasses = [:]

    public Connection db;

    public DOGenerator() {}

    @Override
    protected void init(String... args) {
        setupDataSource()
    }

    @Override
    protected void loadArtifacts() {

        String list = ""
        tables.each {it->
            list += ",'$it.value'"
            doClasses.put(it.value, new Table(it.value, it.key))
        }

        list = list.substring(1)

        println list

        String sql = "SELECT table_name, column_name, data_type FROM information_schema.columns WHERE table_schema='member' and table_name in ($list) ORDER BY table_name, ordinal_position"

        ResultSet rs = db.prepareStatement(sql).executeQuery()
        while (rs.next()){
            Table table = doClasses.get(rs.getString("table_name"))
            table.add(new Field(rs.getString("column_name"), rs.getString("data_type")))
        }

        db.close()
    }

    @Override
    protected void write() {

        writeDOs()
        writeMappers()
        writeDBIs()
    }

    private void writeDOs(){
        File dir = new File("src/main/groovy/"+DOMAIN_PACKAGE.replace(".","/"))
        dir.mkdirs()

        doClasses.values().each { Table table ->

            FileWriter fw = new FileWriter(dir.getPath()+"/"+table.name+".groovy")

            fw.write(table.printDO(DOMAIN_PACKAGE))
            fw.close()
        }
    }

    private void writeMappers(){
        File dir = new File("src/main/groovy/"+MAPPER_PACKAGE.replace(".","/"))
        dir.mkdirs()

        doClasses.values().each { Table table ->

            FileWriter fw = new FileWriter(dir.getPath()+"/"+table.name+"Mapper.groovy")

            fw.write(table.printMapper(MAPPER_PACKAGE, DOMAIN_PACKAGE))
            fw.close()
        }
    }

    private void writeDBIs(){
        File dir = new File("src/main/groovy/"+DBI_PACKAGE.replace(".","/"))
        dir.mkdirs()

        doClasses.values().each { Table table ->

            FileWriter fw = new FileWriter(dir.getPath()+"/"+table.getDBIName()+"DBI.groovy")

            fw.write(table.printDBI(DBI_PACKAGE, MAPPER_PACKAGE, DOMAIN_PACKAGE))
            fw.close()
        }
    }


    @Override
    protected void close() {

    }

    private void setupDataSource(){
        try {
            Properties properties = new Properties();

            properties.put("user", username)
            properties.put("password", password)

            db = DriverManager.getConnection(database, properties)

            PreparedStatement pstmt = db.prepareStatement("select 1")
            ResultSet rs = pstmt.executeQuery()

            rs.next()
        }
        catch (Exception e){
            logger.error("Could not connect to Database", e)
        }
    }

    public static BaseGenerator getGenerator(){
        return new DOGenerator()
    }
}
