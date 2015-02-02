package com.generator

import spock.lang.Specification

/**
 * Created by bpeel on 2/2/15.
 */
class WordTest extends Specification {
    void setup() {

    }

    def"test jsonCase"(String input, String output) {
        expect:
        Word.jsonCase(input) == output

        where:
        input      | output
        null       | null
        "id"       | "id"
        "added by" | "added_by"
        "addedBy"  | "added_by"
        "added_By"  | "added_by"
        "added_by"  | "added_by"
    }
}
