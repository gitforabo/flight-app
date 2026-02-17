package com.aviation.flight_app.model;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CARGO")
public class CargoFlight extends Flight {
    private double cargoWeight;

    public CargoFlight() {}

    public CargoFlight(String flightNumber, Airport departureAirport, String arrivalCity, LocalDateTime arrivalTime,
            int delay, double cargoWeight, Pilot pilot) {
        super(flightNumber, departureAirport, arrivalCity, arrivalTime, delay, pilot);
        this.cargoWeight = cargoWeight;
    }

    public double calculatePriority() {
        return getDelay() + (cargoWeight / 100);
    }

    public double getCargoWeight() { return cargoWeight; }
    public void setCargoWeight(double cargoWeight) { this.cargoWeight = cargoWeight; }
}
