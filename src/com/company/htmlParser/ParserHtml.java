package com.company.htmlParser;

import com.company.metro.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This public class is used for connecting to the Wikipedia page and
 * getting information about stations, lines, and transfers of the Moscow metro.
 */
public class ParserHtml {

    private Document document;
    private ArrayList<Station> stationsWithTwoLines = new ArrayList<>();

    public ParserHtml() throws IOException {
        try {
            document = Jsoup.connect("https://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This public method gets ready-made information for the fields of the station object.
     * Creates a list of these objects.
     *
     * @return A list of stations that have the fields name, line index,
     * line name, and a line containing a description of the transfer.
     */
    public ArrayList<Station> getStationsList() {
        ArrayList<Station> stations = new ArrayList<>();
        ArrayList<Element> lines = getFieldsFromTables(document);
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


    /**
     * This public method gets information about possible transfers between
     * stations from the web page code. Information about the transfer is stored
     * in a single line and requires further processing.
     *
     * @param elementArrayList As a parameter, you get a ready-made list of fields from tables on
     *                         the web page that describe stations.
     * @return A ready-made list of lines that describe transfers between stations.
     */
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

    /**
     * This method gets station names from fields in the table with General information about stations.
     *
     * @param elementArrayList As a parameter, you get a ready-made list of fields from tables on
     *                         the web page that describe stations.
     * @return Ready list of names of Moscow metro stations.
     */
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


    /**
     * This method gets a list of station names from the table fields on the web page.
     *
     * @param elementArrayList As a parameter, you get a ready-made list of fields from tables on
     *                         the web page that describe stations.
     * @return List of names of Moscow metro lines.
     */
    private ArrayList<String> getStationName(ArrayList<Element> elementArrayList) {
        ArrayList<String> nameList = new ArrayList<>();
        for (Element element : elementArrayList) {
            Elements td = element.select("td");
            Elements name = td.get(1).select("a");
            nameList.add(name.get(0).text());
        }
        return nameList;
    }

    /**
     * This method gets a list of indexes for all Moscow metro lines.
     *
     * @param elementArrayList As a parameter, you get a ready-made list of fields from tables on
     *                         the web page that describe stations.
     * @return A list of indices of all lines of the Moscow metro.
     */
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


    /**
     * This method retrieves fields in tables with information about stations.
     * There are five tables on the Wikipedia page, but only the third, fourth,
     * and fifth tables have the necessary information. These tables are obtained
     * by index. Then you get a list of fields with only the necessary information.
     * Auxiliary table fields are deleted.
     *
     * @param document A document containing the html code of the Wikipedia page.
     * @return Full list of fields from all tables with information about Moscow metro stations.
     */
    private ArrayList<Element> getFieldsFromTables(Document document) {
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


    /**
     * This method gets information about stations that are located on two metro
     * lines at the same time. There are only five such stations.
     *
     * @param tr Gets a table element with the tr tag as a parameter
     */
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
        Station station = new Station(stationName, lineIndex, lineName, connection);
        stationsWithTwoLines.add(station);
    }


    /**
     * This method gets the lines describing the transplant directly from the code.
     * The transfer can be between two, three or four stations. This method selects
     * the desired number of stations participating in the transfer, and combines
     * them into a single row.
     *
     * @param elements As a parameter, you get a ready-made list of fields from tables on
     *                 the web page that describe stations.
     * @return A line describing the transfer.
     */
    private String getConnectionStringFromTag(Elements elements) {
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
