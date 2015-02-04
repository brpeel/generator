package com.generator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.lang.reflect.Constructor

/**
 * Created by bpeel on 2/2/15.
 */
abstract class BaseGenerator {

    protected Logger logger = LoggerFactory.getLogger(getClass())

    public BaseGenerator(){

    }

    public static void main(String... args){
        try{
            BaseGenerator generator = GeneratorFactory.getGenerator(DOGenerator)
            generator.init(args)
            generator.loadArtifacts()
            generator.write()
            generator.close()
        }
        catch (Exception e){
            System.err.println("Error running generator : $e.message")
            e.printStackTrace(System.err)
        }
    }

    protected abstract void init(String... args)
    protected abstract void loadArtifacts()
    protected abstract void write()
    protected abstract void close()
}
