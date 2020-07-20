package com.company.jsonSerializer;
import com.company.metro.Connection;
import com.company.metro.Line;
import com.company.metro.Station;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.*;
import java.util.ArrayList;

public class SerializerToFile {

    private ArrayList<Station> stations;
    private ArrayList<Line> lines;
    private ArrayList<Connection> connections;
    private JSONObject metroObject = new JSONObject();

    public SerializerToFile(ArrayList<Station> stations, ArrayList<Line> lines, ArrayList<Connection> connections) {
        this.stations = stations;
        this.lines = lines;
        this.connections = connections;
    }

    public void createFile(String destPath) throws IOException, JSONException {
        String validPath = destPath + File.separator + "MetroMap.json";
        serializeStationsToObject(stations,lines);
        serializeConnectionsToObject(connections);
        serializeLinesToObject(lines);
        FileWriter writer = new FileWriter(validPath);
        writer.write(metroObject.toString());
        writer.close();
    }


    private void serializeLinesToObject(ArrayList<Line> lineArrayList) throws JSONException {
        int size = lineArrayList.size();
        Line[] lines = lineArrayList.toArray(new Line[size]);
        metroObject.put("Lines", lines);
    }


    private void serializeStationsToObject(ArrayList<Station> stationArrayList, ArrayList<Line> lineArrayList) throws JSONException {
        JSONObject stationsObject = new JSONObject();
        for (Line line : lineArrayList) {
            ArrayList<String> stationsNames = new ArrayList<>();
            for (Station station : stationArrayList) {
                if (line.getIndex().equals(station.getLineIndex())) {
                    stationsNames.add(station.getName());
                }
            }
            String[] names = stationsNames.toArray(new String[stationsNames.size()]);
            stationsObject.put(line.getIndex(), names);
        }
        metroObject.put("Stations", stationsObject);
    }


    private void serializeConnectionsToObject(ArrayList<Connection> connections) throws JSONException {
        JSONArray connectionArray = new JSONArray();
        for (Connection connection : connections) {
            int size = connection.getConnectStations().size();
            Station[] stations = connection.getConnectStations().toArray(new Station[size]);
            connectionArray.put(stations);
        }
        metroObject.put("Connections", connectionArray);
    }
}
