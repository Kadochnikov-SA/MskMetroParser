package com.company.metro;

import java.util.*;

/**
 * This class gets a list of stations when creating them, distributes
 * them along lines, and creates transfer objects.
 */
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

    /**
     * This method creates a list of line objects..
     * @return List of objects line
     */
    public ArrayList<Line> getLineList() {
        return getLines(stationArrayList);
    }

    /**
     * This method creates a list of all transfers.
     * @return List that stores line objects
     */
    public ArrayList<Connection> getConnectionsList() {
        ArrayList<Station> stations = new ArrayList<>(getConnectStationsList(stationArrayList));
        ArrayList<Connection> connections = new ArrayList<>(getConnections(stations));
        return connections;
    }

    /**
     * This method creates transfer
     * @param stations Gets a list of stations
     * @return List that stores transfer objects
     */
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

    /**
     * This method determines between which stations there is a transfer,
     * and between which there is no transfer. Also checks that the same transfer
     * is not added to the list twice.
     * @param station A station that has an interchange with other stations.
     * @param stations List of stations that have transfers.
     * @return A list of stations that contains stations with a single transfer.
     */
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

    /**
     * This method gets inform
     * @param stations Retrieves a list of all stations.
     * @return List of objects of the line class
     */
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

    /**
     * This method creates objects of the line class from the received fields.
     * @param hashMap Takes HashMap as a parameter where the key and value pair are fields for the line object.
     * @return List of objects line
     */
    private ArrayList<Line> createLines(HashMap<String,String> hashMap) {
        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<String> indexList = new ArrayList<>(hashMap.keySet());
        for (String index : indexList) {
            Line line = new Line(hashMap.get(index),index);
            lines.add(line);
        }
        return lines;
    }

    /**
     * This method returns checks if a station has an interchange with other stations, it is added to the list.
     * @param stations List of all stations
     * @return List of stations that have transfers
     */
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
