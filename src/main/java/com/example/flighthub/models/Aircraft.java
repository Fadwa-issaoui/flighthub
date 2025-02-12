package com.example.flighthub.models;

public class Aircraft {
    private int aircraftId;
    private String model;
    private int capacity;
    private boolean isAvailable;
    private int flightId;

    public Aircraft() {

    }

    public int getFlightId() {
        return flightId;
    }
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public Aircraft(int aircraftId, String model, int capacity, boolean isAvailable) {
        this.aircraftId = aircraftId;
        this.model = model;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }

    public int getAircraftId() {
        return aircraftId;
    }


    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }


    @Override
    public String toString() {
        return "Aircraft{" + "aircraftId=" + aircraftId + ", model='" + model + '\'' + ", capacity=" + capacity + ", isAvailable=" + isAvailable + '}';
    }
}