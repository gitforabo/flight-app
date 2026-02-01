package aviation;

import java.time.LocalDateTime;

public class PassengerFlight extends Flight implements Delayable {

    private int passengerCount;

    public PassengerFlight(String flightNumber, String departureСity, String arrivalCity, LocalDateTime arrivalTime,
            int delay, int passengerCount) {
                    super(flightNumber, departureСity, arrivalCity, arrivalTime, delay);
                    this.passengerCount = passengerCount;
    }
    
    @Override
    public double calculatePriority() {
        // Используем getDelay() от родителя (Flight)
        return passengerCount * getDelay(); 
    }

    @Override
    public void reportDelay(int minutes) {
        addDelay(getDelay() + minutes); // Обновляем задержку через сеттер родителя
    }
}
