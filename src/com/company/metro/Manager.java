package com.company.metro;

import java.util.*;

public class Manager {


    private ArrayList<Station> stationArrayList = new ArrayList<>();


    public Manager(ArrayList<Station> stations) {
        this.stationArrayList.addAll(stations);
    }


    public ArrayList<Station> getStationsList() {
        ArrayList<Station> stations = new ArrayList<>();
        for (Station station : stationArrayList) {
            Station st = new Station(station.getName(), station.getLineIndex());
            stations.add(st);
        }
        return stations;
    }

    public ArrayList<Line> getLineList() {
        return getLines(stationArrayList);
    }

    public ArrayList<Connection> getConnectionsList() {
        ArrayList<Station> stations = new ArrayList<>(getConnectStationsList(stationArrayList));
        ArrayList<Connection> connections = new ArrayList<>(getConnections(stations));
        return connections;
    }


    private ArrayList<Connection> getConnections(ArrayList<Station> stations) {
        ArrayList<Connection> connections = new ArrayList<>();
        for (Station station : stations) {
            ArrayList<Station> stationArrayList = new ArrayList<>(getConnectStations(station, stations));
            if (stationArrayList.size() != 1) {
                Connection connection = new Connection(stationArrayList);
                connections.add(connection);
            }
        }
        return connections;
    }

    private ArrayList<Station> getConnectStations(Station station, ArrayList<Station> stations) {
        ArrayList<Station> connectStations = new ArrayList<>();
        for (Station station1 : stations) {
            if (station.getConnectionString().contains(station1.getName()) && !station.getLineIndex().equals(station1.getLineIndex())) {
                Station st1 = new Station(station1.getName(), station1.getLineIndex());
                connectStations.add(st1);
                station1.setConnectionString("");
            }
        }
        station.setConnectionString("");
        Station st = new Station(station.getName(), station.getLineIndex());
        connectStations.add(st);
        return connectStations;
    }


    private ArrayList<Line> getLines(ArrayList<Station> stations) {
        HashMap<String, String> linesFields = new HashMap<>();
        for (Station station : stations) {
            String index = station.getLineIndex();
            String name = station.getLineName();
            if (!linesFields.containsKey(index)) {
                linesFields.put(index,name);
            }
        }
        return createLines(linesFields);
    }

    private ArrayList<Line> createLines(HashMap<String,String> hashMap) {
        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<String> indexList = new ArrayList<>(hashMap.keySet());
        for (String index : indexList) {
            Line line = new Line(hashMap.get(index),index);
            lines.add(line);
        }
        return lines;
    }

    private ArrayList<Station> getConnectStationsList(ArrayList<Station> stations) {
        ArrayList<Station> connectStations = new ArrayList<>();
        for (Station station : stations) {
            if (station.getConnectionString().length() != 0) {
                connectStations.add(station);
            }

        }
        return connectStations;
    }

}
