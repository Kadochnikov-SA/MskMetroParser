package com.company.jsonSerializer;
import com.company.metro.Connection;
import com.company.metro.Line;
import com.company.metro.Station;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * This class collects one common Json object from their lists
 * of Java objects and writes it to a text file. Which will store
 * complete information about stations, lines, and additives of the Moscow metro.
 */

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

    /**
     * This method collects all Json objects created from lists of transfers,
     * lines, and stations, converts them to the desired format, and serializes
     * them into a single Json object. Then it writes the full information to a
     * text file. Then saves the text file to the specified path.
     * @param destPath The path to save the text file with the Json object
     */
    public void createFile(String destPath) throws IOException, JSONException {
        String validPath = destPath + File.separator + "MetroMap.json";
        serializeStationsToObject(stations,lines);
        serializeConnectionsToObject(connections);
        serializeLinesToObject(lines);
        FileWriter writer = new FileWriter(validPath);
        writer.write(metroObject.toString());
        writer.close();
    }

    /**
     * This method serializes Java line objects into a Json object.
     * @param lineArrayList List of all lines
     */
    private void serializeLinesToObject(ArrayList<Line> lineArrayList) throws JSONException {
        int size = lineArrayList.size();
        Line[] lines = lineArrayList.toArray(new Line[size]);
        metroObject.put("Lines", lines);
    }


    /**
     * This method converts the list of stations to the desired format and serializes
     * them from Java objects to a Json object.
     * @param stationArrayList List of all stations
     * @param lineArrayList A list of all lines needed to bring the object to the desired format
     */
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


    /**
     * This method serializes the list of all Java object-specific issues into a Json object.
     * @param connections List of all transfers
     */
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
