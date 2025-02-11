package com.example.flighthub.models;

public class Car {
    private int id;
    private String brand;
    private String model;
    private String licensePlate;
    private double rentalPricePerDay;
    private boolean isAvailable;

    public Car(int id, String brand, String model, String licensePlate, double rentalPricePerDay) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.rentalPricePerDay = rentalPricePerDay;
        this.isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rentCar() {
        this.isAvailable = false;
    }

    public void returnCar() {
        this.isAvailable = true;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }
}


