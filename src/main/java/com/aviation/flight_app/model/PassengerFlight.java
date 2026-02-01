package com.aviation.flight_app.model;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("PASSENGER")
public class PassengerFlight extends Flight {

    private int passengerCount;

    public PassengerFlight() {}

    public PassengerFlight(String flightNumber, String departureСity, String arrivalCity, LocalDateTime arrivalTime,
            int delay, int passengerCount) {
                    super(flightNumber, departureСity, arrivalCity, arrivalTime, delay);
                    this.passengerCount = passengerCount;
    }

    public int getPassengerCount() { return passengerCount; }
    public void setPassengerCount(int passengerCount) { this.passengerCount = passengerCount; }
    
    @Override
    public double calculatePriority() {
        // Используем getDelay() от родителя (Flight)
        return passengerCount * getDelay(); 
    }

    // @Override
    // public void reportDelay(int minutes) {
    //     addDelay(getDelay() + minutes); // Обновляем задержку через сеттер родителя
    // }
}
