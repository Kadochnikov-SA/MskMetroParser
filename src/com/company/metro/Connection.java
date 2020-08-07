package com.company.metro;

import java.util.ArrayList;

/**
 * This class describes transfers between stations.
 * Stores a list of stations that have a connection between them.
 */

public class Connection {

    private ArrayList<Station> connectStations = new ArrayList<>();

    public Connection(ArrayList<Station> connectStations) {
        this.connectStations.addAll(connectStations);
    }

    public ArrayList<Station> getConnectStations() {
        return connectStations;
    }
}
