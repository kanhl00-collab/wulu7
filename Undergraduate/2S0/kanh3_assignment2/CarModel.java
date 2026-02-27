package com.company;

class CarModel {
    // Model of the car
    private String name;
    // Fuel consumption in liters per 100 kilometer
    private double fuelEconomy;
    // Size of the tank in liters
    private double gasTankSize;

    CarModel(String name, double fuelEconomy, double gasTankSize) {
        this.name = name;
        this.fuelEconomy = fuelEconomy;
        this.gasTankSize = gasTankSize;
    }

    String getName() {
        return name;
    }

    double getFuelEconomy() {
        return fuelEconomy;
    }

    double getGasTankSize() {
        return gasTankSize;
    }
}
