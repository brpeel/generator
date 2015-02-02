package com.generator

/**
 * Created by bpeel on 2/2/15.
 */
class Printer {
    private StringBuilder buf = new StringBuilder()

    public Printer print(String s){
        buf.append(s)
        return this
    }

    public Printer println(String s){
        return print(s).print("\n")
    }

    public Printer println(){
        return print("\n")
    }

    @Override
    public String toString() {
        return buf.toString();
    }
}
