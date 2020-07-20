package com.company.jsonSerializer;

import com.company.metro.Line;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class SerializerFromFile {

    private String path;

    public SerializerFromFile(String path) {
        this.path = path;
    }

    public void printLines() throws JSONException {
        HashMap<String, Integer> lineMap = new HashMap<>(getNumberOfStations());
        ArrayList<String> names = new ArrayList<>(lineMap.keySet());
        for (String name : names) {
            System.out.println(name + ": " + lineMap.get(name) + " станций.");

        }
    }

    private HashMap<String, Integer> getNumberOfStations() throws JSONException {
        HashMap<String, Integer> stationsMap = new HashMap<>();
        ArrayList<Line> lines = new ArrayList<>(serializeLinesLinesToJsonObject());
        JSONObject linesObject = createJSONObject().getJSONObject("Stations");
        for (Line line : lines) {
            JSONArray lineObject = linesObject.getJSONArray(line.getIndex());
            stationsMap.put(line.getName(), lineObject.length());
        }
        return stationsMap;
    }

    private ArrayList<Line> serializeLinesLinesToJsonObject() throws JSONException {
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
