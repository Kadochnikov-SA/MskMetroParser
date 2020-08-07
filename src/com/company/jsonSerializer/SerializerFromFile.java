package com.company.jsonSerializer;

import com.company.metro.Line;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class reads a file with Json objects, gets the necessary information from it,
 * creates java line objects, and outputs a list of lines and the number of stations
 * on each of them to the console
 */
public class SerializerFromFile {

    private String path;

    public SerializerFromFile(String path) {
        this.path = path;
    }

    /**
     * This method displays a list of Moscow metro lines in the console with the number
     * of stations on each of them.
     */
    public void printLines() throws JSONException {
        HashMap<String, Integer> lineMap = new HashMap<>(getNumberOfStations());
        ArrayList<String> names = new ArrayList<>(lineMap.keySet());
        for (String name : names) {
            System.out.println(name + ": " + lineMap.get(name) + " станций.");

        }
    }

    /**
     * This method gets the information serializes the Json object in the list of Java objects
     * of stations, counts their number for each line.
     * @return Hash Map where the key is the name of the line, and the value is the number
     * of stations on the line.
     */
    private HashMap<String, Integer> getNumberOfStations() throws JSONException {
        HashMap<String, Integer> stationsMap = new HashMap<>();
        ArrayList<Line> lines = new ArrayList<>(serializeLinesToJsonObject());
        JSONObject linesObject = createJSONObject().getJSONObject("Stations");
        for (Line line : lines) {
            JSONArray lineObject = linesObject.getJSONArray(line.getIndex());
            stationsMap.put(line.getName(), lineObject.length());
        }
        return stationsMap;
    }

    /**
     * This method gets a list of java station objects by serializing them from Json objects.
     * @return List of all stations
     */
    private ArrayList<Line> serializeLinesToJsonObject() throws JSONException {
        ArrayList<Line> lines = new ArrayList<>();
        JSONObject jsonObject = createJSONObject();
        JSONArray lineArray = jsonObject.getJSONArray("Lines");
        for (int i = 0; i < lineArray.length(); i++) {
            JSONObject object = (JSONObject) lineArray.get(i);
            Line line = new Line(object.getString("name"), object.getString("index"));
            lines.add(line);
        }
        return lines;
    }

    /**
     * This method creates a Json object from a text file with information about stations, lines, and transfers.
     * @return Json object that describes all information about stations, transfers, and lines
     */
    private JSONObject createJSONObject() throws JSONException {
        StringBuilder builder = new StringBuilder();
        try {
            ArrayList<String> strings = (ArrayList<String>) Files.readAllLines(Paths.get(path));
            strings.forEach(builder::append);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String mosMetro = builder.toString();
        JSONObject object = new JSONObject(mosMetro);
        return object;
    }


}
