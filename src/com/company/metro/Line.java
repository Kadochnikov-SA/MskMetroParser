package com.company.metro;

/**
 * This class describes the metro line. Stores the index of the line and its name.
 */
public class Line {

    private String name;
    private String index;

    public Line(String name, String index) {
        this.name = name;
        this.index = index;
    }


    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }


}
