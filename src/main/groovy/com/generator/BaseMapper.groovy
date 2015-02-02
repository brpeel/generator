package com.generator

import org.skife.jdbi.v2.StatementContext
import org.skife.jdbi.v2.tweak.ResultSetMapper

import java.lang.reflect.ParameterizedType
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.Timestamp

/**
 * Created by bpeel on 12/12/14.
 */
public abstract class BaseMapper<T> implements ResultSetMapper<T> {


    T map(int index, ResultSet r, StatementContext ctx) throws SQLException {

        def object = newObject();

        Map data = parseRow(r)

        return map(data, object);
    }

    private Map parseRow(ResultSet r) throws SQLException {

        ResultSetMetaData rsmd = r.getMetaData()
        HashMap<String, Object> data = new HashMap<>()

        for (int i = 1; i<=rsmd.columnCount; i++)
            data.put(rsmd.getColumnName(i), r.getObject(i))

        return data
    }

    protected def getValue(Map data, String name){
        def value = data.get(name)
        if (!value)
            return null
        return value
    }

    protected def getDateValue(Map data, String name){
        Timestamp value = data.get(name)
        if (!value)
            return null
        return new DateTime(value.getTime())
    }

    protected abstract T newObject()
    protected abstract T map(Map data, T object)
}
