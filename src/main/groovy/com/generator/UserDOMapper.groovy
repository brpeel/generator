package com.generator

import org.skife.jdbi.v2.tweak.ResultSetMapper

import java.sql.ResultSet

/**
 * Created by bpeel on 2/2/15.
 */
public class UserDOMapper extends BaseMapper<UserDO>{

    @Override
    protected UserDO newObject() {
        return new UserDO()
    }

    @Override
    protected UserDO map(Map data, UserDO object) {
        object.id = getValue(data, "id")
    }
}