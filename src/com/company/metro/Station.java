package com.company.metro;

/**
 * This class describes the station. Stores the station name, line name,
 * line index, and a string that describes the transfer between stations, if any.
 */
public class Station {

    private String name;
    private String lineIndex;
    private String lineName;
    private String connectionString;

    public Station(String name, String lineIndex) {
        this.name = name;
        this.lineIndex = lineIndex;
    }

    public Station (String name, String lineIndex, String lineName, String connectionString) {
        this.lineIndex = lineIndex;
        this.lineName = lineName;
        this.name = name;
        this.connectionString = connectionString;
    }

    public String getName() {
        return name;
    }

    public String getLineIndex() {
        return lineIndex;
    }

    public String getLineName() {
        return lineName;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}
