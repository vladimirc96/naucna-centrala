package com.upp.naucnacentrala.client;

import com.upp.naucnacentrala.model.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleClient {

    @Autowired
    RestTemplate restTemplate;

    public Location getCoordinates(String city){
        city = city.replaceAll(" ", "+");
        ResponseEntity responseEntity = restTemplate.getForEntity("https://maps.googleapis.com/maps/api/geocode/json?address=" + city +
                "&key=AIzaSyDAhIfbemeaLl0uSU4Ob29nTGEHKbso6Eo", String.class);
        JSONObject jsonObject = new JSONObject(responseEntity.getBody().toString());
        JSONArray results = jsonObject.getJSONArray("results");
        JSONObject resultsArray = results.getJSONObject(0);
        JSONObject geometry = resultsArray.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");
        return new Location(Double.parseDouble(location.get("lng").toString()), Double.parseDouble(location.get("lat").toString()));
    }


}
