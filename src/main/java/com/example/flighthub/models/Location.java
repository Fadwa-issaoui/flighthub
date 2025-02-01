package com.example.flighthub.models;

import java.time.LocalDate;

public class Location {
    private int id;
    private Car car;
    private String customerName;
    private LocalDate rentalDate;
    private int rentalDays;
    private double totalCost;

    public Location(int id, Car car, String customerName, LocalDate rentalDate, int rentalDays) {
        this.id = id;
        this.car = car;
        this.customerName = customerName;
        this.rentalDate = rentalDate;
        this.rentalDays = rentalDays;
        this.totalCost = car.getRentalPricePerDay() * rentalDays;
        car.rentCar();
    }

    public Location() {

    }

    public int getId() { return id; }
    public Car getCar() { return car; }
    public String getCustomerName() { return customerName; }
    public LocalDate getRentalDate() { return rentalDate; }
    public int getRentalDays() { return rentalDays; }
    public double getTotalCost() { return totalCost; }

    public void returnCar() {
        car.returnCar();
    }


    // Setters for updating the Location object
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        // Recalculate total cost if needed based on customer-related logic
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
        this.totalCost = car.getRentalPricePerDay() * rentalDays;  // Recalculate total cost based on new rental days
    }
}

