package com.company;

import com.company.htmlParser.ParserHtml;
import com.company.jsonSerializer.SerializerFromFile;
import com.company.jsonSerializer.SerializerToFile;
import com.company.metro.Manager;
import com.company.metro.Station;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, JSONException{


        ParserHtml parserHtml = new ParserHtml();
        ArrayList<Station>stations = new ArrayList<>(parserHtml.getStationsList());

        Manager manager = new Manager(stations);

        String destPath = ""; // Enter path where you want to create file

        SerializerToFile serializerToFile = new SerializerToFile(manager.getStationsList(),manager.getLineList(),manager.getConnectionsList());
        serializerToFile.createFile(destPath);
        SerializerFromFile serializerFromFile = new SerializerFromFile(destPath + "/MetroMap.json");
        serializerFromFile.printLines();
    }
}
