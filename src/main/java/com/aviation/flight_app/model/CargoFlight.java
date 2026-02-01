package aviation;

import java.time.LocalDateTime;

public class CargoFlight extends Flight {
    private double cargoWeight;

    public CargoFlight(String flightNumber, String departureСity, String arrivalCity, LocalDateTime arrivalTime,
            int delay, double cargoWeight) {
        super(flightNumber, departureСity, arrivalCity, arrivalTime, delay);
        this.cargoWeight = cargoWeight;
    }

    public double calculatePriority() {
        return getDelay() + (cargoWeight / 100);
    }
}
