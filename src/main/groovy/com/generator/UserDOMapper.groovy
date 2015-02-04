package com.generator



/**
 * Created by bpeel on 2/2/15.
 */
public class UserDOMapper extends BaseMapper<UserDO>{



    @Override
    protected UserDO map(Map data) {
        UserDO object = new UserDO()
        object.id = getValue(data, "id")
        return object
    }
}