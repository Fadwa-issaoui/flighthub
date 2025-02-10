package com.example.flighthub.models;

public class Airport {
    private int airportId;
    private String name;
    private String location;
    private String code;

    public Airport() {
        // Default constructor
    }

    public Airport(int airportId, String name, String location, String code) {
        this.airportId = airportId;
        this.name = name;
        this.location = location;
        this.code = code;
    }

    public int getAirportId() {
        return airportId;
    }

    public void setAirportId(int airportId) {
        this.airportId = airportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "airportId=" + airportId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
