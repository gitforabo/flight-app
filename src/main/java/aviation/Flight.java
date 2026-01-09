package aviation;

import java.time.LocalDateTime;

public abstract class Flight {
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime arrivalTime;
    private int delay;

    public Flight(String flightNumber, String departureCity, String arrivalCity, LocalDateTime arrivalTime, int delay) {
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.arrivalTime = arrivalTime;
        this.delay = delay;
    }

    public abstract double calculatePriority();

    public void printInfo() {
        System.out.printf("Рейс %s: %s -> %s | Задержка: %d мин%n", 
                flightNumber, departureCity, arrivalCity, delay);
    }

    public int getDelay() {
        return delay;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }
}
