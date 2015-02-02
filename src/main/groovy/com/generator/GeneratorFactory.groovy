package com.generator

/**
 * Created by bpeel on 2/2/15.
 */
class GeneratorFactory {

    public static BaseGenerator getGenerator(Class clazz){
        return clazz.newInstance()
    }
}
