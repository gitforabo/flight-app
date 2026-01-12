package aviation;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Flight {
    private String flightNumber;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime arrivalTime;
    private volatile int delay;

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

    public String getFlightNumber() {
        return flightNumber;
    }
    public int getDelay() {
        return delay;
    }
    // Добавляем synchronized, чтобы только один поток мог менять задержку
    public synchronized void addDelay(int delay) {
        this.delay += delay;
    }
    // public void setDelay(int delay) {
    //     this.delay += delay;
    // }

    public String getArrivalCity() {
        return arrivalCity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Одна ссылка
        if (o == null || getClass() != o.getClass()) return false;
        
        Flight flight = (Flight) o;
        return Objects.equals(arrivalTime, flight.arrivalTime) && Objects.equals(flightNumber, flight.flightNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightNumber, arrivalTime);
    }
}
