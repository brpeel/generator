package com.generator

import spock.lang.Specification

/**
 * Created by bpeel on 2/2/15.
 */
class MappingTest extends Specification {

    void setup(){
        Mapping.load(["Accounting_user":"User"], ["id":"id", "added_by":"addedBy"])
    }
    def "test table Mapping"(String input, String result) {
        expect:
        Mapping.mapTable(input) == result

        where:
        input | result
        "Accounting_user"| "User"
        "AccountingUser" | "AccountingUser"
        "" | ""
    }

    def "test table Column Mapping"(String input, String result) {
        expect:
        Mapping.mapColumn(input) == result

        where:
        input | result
        "id"| "id"
        "added_by" | "addedBy"
        "reported_by" | "reportedBy"
        "firstName" | "firstName"
    }

    void testMapColumn() {

    }
}
