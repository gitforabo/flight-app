package com.aviation.flight_app;

public class InvalidFlightDataException extends Exception {
    public InvalidFlightDataException(String message) {
        super(message);
    }
}