package com.mtm.api;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Data {

    ArrayList<Country> allCountry;
    ArrayList<Country> responseCountry;

    public Data(ArrayList<Country> allCountry, ArrayList<Country> responseCountry) {

        this.allCountry = allCountry;
        this.responseCountry = responseCountry;
    }

    public Data() {
        this.allCountry = new ArrayList<>();
        this.responseCountry = new ArrayList<>();
    }

    public ArrayList<Country> getAllCountry() {
        return allCountry;
    }

    public void setAllCountry(ArrayList<Country> allCountry) {
        this.allCountry = allCountry;
    }

    public ArrayList<Country> getResponseCountry() {
        return responseCountry;
    }

    public void setResponseCountry(ArrayList<Country> responseCountry) {
        this.responseCountry = responseCountry;
    }

    public void readJsonFile(String filename) throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray a = (JSONArray) parser.parse(new FileReader("cities.json"));

        for (Object o : a) {
            JSONObject object = (JSONObject) o;
            String country = (String) object.get("country");
            String name = (String) object.get("name");
            String lat = (String) object.get("lat");
            String lng = (String) object.get("lng");
            Country tmpCountry = new Country(country, name, lat, lng);
            allCountry.add(tmpCountry);
        }
    }

    public void setTurkey() {
        for (int i = 0; i < responseCountry.size(); i++) {
            if (responseCountry.get(i).getCountry().equals("TR")) {
                Country tmp = responseCountry.get(i);
                responseCountry.remove(i);
                responseCountry.add(0, tmp);

            }
        }
    }
}
