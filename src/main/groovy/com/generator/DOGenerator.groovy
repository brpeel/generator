package com.generator

import org.apache.tomcat.jdbc.pool.DataSource
import org.apache.tomcat.jdbc.pool.PoolProperties

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by bpeel on 2/2/15.
 */
class DOGenerator extends BaseGenerator{

    public Connection db;

    public DOGenerator() {}

    @Override
    protected void init(String... args) {
        setupDataSource()
    }

    @Override
    protected void loadArtifacts() {

    }

    @Override
    protected void write() {

    }

    @Override
    protected void close() {

    }

    private void setupDataSource(){
        Properties properties = new Properties();

        properties.put("user","bpeel")
        properties.put("password","admin")

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bsg", properties)

        PreparedStatement pstmt = conn.prepareStatement("select 1")
        ResultSet rs = pstmt.executeQuery()

        rs.next()
        println rs.getInt(1)

    }

    public static BaseGenerator getGenerator(){
        return new DOGenerator()
    }
}
