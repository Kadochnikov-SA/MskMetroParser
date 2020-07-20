package com.company.htmlParser;

import com.company.metro.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ParserHtml {

    private Document document = Jsoup.connect("https://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена").get();
    private ArrayList<Station> stationsWithTwoLines = new ArrayList<>();

    public ParserHtml() throws IOException {
    }

    public ArrayList<Station> getStationsList() {
        ArrayList<Station> stations = new ArrayList<>();
        ArrayList<Element> lines = getLinesFromTables(document);
        ArrayList<String> stationNames = new ArrayList<>(getStationName(lines));
        ArrayList<String> lineIndexes = new ArrayList<>(getLineIndex(lines));
        ArrayList<String> lineNames = new ArrayList<>(getLineName(lines));
        ArrayList<String> connectionStrings = new ArrayList<>(getConnectString(lines));
        for (int i = 0; i < stationNames.size(); i++) {
            String stationName = stationNames.get(i);
            String lineIndex = lineIndexes.get(i);
            String lineName = lineNames.get(i);
            String connectionString = connectionStrings.get(i);
            Station station = new Station(stationName, lineIndex, lineName, connectionString);
            stations.add(station);
        }
        stations.addAll(stationsWithTwoLines);
        return stations;
    }


    private ArrayList<String> getConnectString(ArrayList<Element> elementArrayList) {
        ArrayList<String> connectStrings = new ArrayList<>();
        String connectString = null;
        for (Element element : elementArrayList) {
            Elements td = element.select("td");
            Element name = td.get(3);
            Elements spanElement = name.select("span");
            if (spanElement.size() == 4) {
                connectString = getConnectionStringFromTag(spanElement);
                connectStrings.add(connectString);
            } else if (spanElement.size() == 6) {
                connectString = getConnectionStringFromTag(spanElement);
                connectStrings.add(connectString);
            } else {
                connectString = spanElement.attr("title");
                connectStrings.add(connectString);
            }
        }
        return connectStrings;

    }


    private ArrayList<String> getLineName(ArrayList<Element> elementArrayList) {
        ArrayList<String> lineNames = new ArrayList<>();
        for (Element element : elementArrayList) {
            Elements td = element.select("td");
            Element name = td.get(0);
            Elements e = name.select("a");
            String lineName = e.get(0).attr("title");
            lineNames.add(lineName);
        }
        return lineNames;
    }

    private ArrayList<String> getStationName(ArrayList<Element> elementArrayList) {
        ArrayList<String> nameList = new ArrayList<>();
        for (Element element : elementArrayList) {
            Elements td = element.select("td");
            Elements name = td.get(1).select("a");
            nameList.add(name.get(0).text());
        }
        return nameList;
    }

    private ArrayList<String> getLineIndex(ArrayList<Element> elementArrayList) {
        ArrayList<String> indexList = new ArrayList<>();
        for (Element element : elementArrayList) {
            Elements td = element.select("td");
            Element name = td.get(0);
            if (name.select("span").size() == 5) {
                getStationsWithTwoLines(element);
            }
            String index = name.select("span").get(0).select("span.sortkey").text();
            indexList.add(index);
        }
        return indexList;
    }


    private ArrayList<Element> getLinesFromTables(Document document) {
        ArrayList<Element> elementArrayList = new ArrayList<>();
        Element table1 = document.select("table").get(3);
        Elements linesFromTable1 = table1.select("tr");
        Element table2 = document.select("table").get(4);
        Elements linesFromTable2 = table2.select("tr");
        Element table3 = document.select("table").get(5);
        Elements linesFromTable3 = table3.select("tr");
        elementArrayList.addAll(linesFromTable1);
        elementArrayList.addAll(linesFromTable2);
        elementArrayList.addAll(linesFromTable3);
        elementArrayList.remove(0);
        elementArrayList.remove(238);
        elementArrayList.remove(238);
        elementArrayList.remove(244);
        elementArrayList.remove(244);
        return elementArrayList;
    }

    private void getStationsWithTwoLines(Element tr) {
        String lineIndex = null;
        String stationName = null;
        String lineName = null;
        String connection = null;
        Elements td = tr.select("td");
        Element index = td.get(0);
        lineIndex = index.select("span").get(2).select("span.sortkey").text();
        Elements element1 = td.get(1).select("a");
        stationName = element1.get(0).text();
        Element element2 = td.get(0);
        Elements e = element2.select("span");
        lineName = e.get(3).select("a").attr("title");
        Element name = td.get(3);
        Elements spanElement = name.select("span");
        if (spanElement.size() == 4) {
            connection = getConnectionStringFromTag(spanElement);
        } else if (spanElement.size() == 6) {
            connection = getConnectionStringFromTag(spanElement);
        } else {
            connection = spanElement.attr("title");
        }
        Station station = new Station(stationName,lineIndex,lineName,connection);
        stationsWithTwoLines.add(station);
    }


    private String getConnectionStringFromTag (Elements elements) {
        String connectString = null;
        if (elements.size() == 4) {
            String element1 = elements.get(1).attr("title");
            String element2 = elements.get(3).attr("title");
            connectString = element1 + element2;
        } else if (elements.size() == 6) {
            String element1 = elements.get(1).attr("title");
            String element2 = elements.get(3).attr("title");
            String element3 = elements.get(5).attr("title");
            connectString = element1 + element2 + element3;
        }
        return connectString;
    }


}
