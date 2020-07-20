package com.company.metro;

import java.util.ArrayList;

public class Connection {

    private ArrayList<Station> connectStations = new ArrayList<>();

    public Connection(ArrayList<Station> connectStations) {
        this.connectStations.addAll(connectStations);
    }

    public ArrayList<Station> getConnectStations() {
        return connectStations;
    }
}
