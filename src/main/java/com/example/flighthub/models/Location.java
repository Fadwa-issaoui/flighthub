package com.example.flighthub.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Location {
    private int id;
    private Car car;
    private String customerName;
    private LocalDate rentalDate;
    private int rentalDays;
    private BigDecimal totalCost; // Changed from double to BigDecimal

    public Location(int id, Car car, String customerName, LocalDate rentalDate, int rentalDays) {
        this.id = id;
        this.car = car;
        this.customerName = customerName;
        this.rentalDate = rentalDate;
        this.rentalDays = rentalDays;
        this.totalCost = car.getRentalPricePerDay().multiply(BigDecimal.valueOf(rentalDays)); // Fixed Calculation
        car.rentCar();
    }

    public Location() {}

    public int getId() { return id; }
    public Car getCar() { return car; }
    public String getCustomerName() { return customerName; }
    public LocalDate getRentalDate() { return rentalDate; }
    public int getRentalDays() { return rentalDays; }
    public BigDecimal getTotalCost() { return totalCost; } // Changed return type

    public void returnCar() {
        car.returnCar();
    }

    // Setters
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
        this.totalCost = car.getRentalPricePerDay().multiply(BigDecimal.valueOf(rentalDays)); // Fixed Calculation
    }
}
