package com.generator

import spock.lang.Specification

/**
 * Created by bpeel on 2/2/15.
 */
class TableTest extends Specification {

    void setup(){
    }

    def "test table printDO - no mapping"(){
        given:
        Table table = new Table("Accounting_user")

        when:
        String clazz = table.printDO().replace("\n","")

        then:
        clazz == "public class AccountingUserDO{}"
    }


    def "test table printDO - mapping"(){
        given:
        Mapping.load(["Accounting_user":"UserDO"], null)
        Table table = new Table("Accounting_user")
        table.add(new Field("id","int"))

        when:
        String clazz = table.printDO().replace("\n","").replace("\t"," ")
        println clazz

        then:
        clazz == "public class UserDO{ @JsonProperty(\"id\") int id}"
    }

    def "test table printMapper"(){
        given:
        Mapping.load(["Accounting_user":"UserDO"], null)
        Table table = new Table("Accounting_user")
        table.add(new Field("id","int"))

        when:
        String clazz = table.printMapper()//.replace("\n","").replace("\t"," ")
        println clazz

        then:
        clazz == "public class UserDO{ @JsonProperty(\"id\") int id}"
    }
}
