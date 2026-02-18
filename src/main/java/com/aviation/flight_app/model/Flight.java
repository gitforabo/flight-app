package com.aviation.flight_app.model;

import java.time.LocalDateTime; // Импорты для Hibernate
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity // сущность БД. Один репозиторий (FlightRepository) = Одна независимая сущность (Entity).
@Table(name = "flights") // Имя таблицы
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Все наследники в одной таблице
@DiscriminatorColumn(name = "flight_type") // Колонка для различения типов
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY, 
    property = "flight_type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PassengerFlight.class, name = "PASSENGER"),
    @JsonSubTypes.Type(value = CargoFlight.class, name = "CARGO")
})
public abstract class Flight {

    @Id // Первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоинкремент (1, 2, 3...) создаеться в БД
    private Long id;

    @Column(name = "flight_number", nullable = false)
    @NotBlank(message = "Номер рейса не может быть пустым") // Валидация, не пропускает пустых
    private String flightNumber;

    @ManyToOne 
    @JoinColumn(name = "departure_airport_id")  // В таблице flights появится колонка departure_airport_id
    @NotNull(message = "Аэропорт вылета должен быть указан")
    private Airport departureAirport;

    @NotBlank(message = "Город прибытия обязателен") 
    private String arrivalCity;
    
    @NotNull(message = "Время прибытия обязательно")
    private LocalDateTime arrivalTime;
    
    @Min(value = 0, message = "Задержка не может быть отрицательной") 
    private int delay;

    @ManyToOne
    @JoinColumn(name = "pilot_id") // В таблице flights появится колонка pilot_id
    private Pilot pilot;

    protected Flight() {} // Пустой конструктор ОБЯЗАТЕЛЕН для Hibernate

    public Flight(String flightNumber, Airport departureAirport, String arrivalCity, LocalDateTime arrivalTime, int delay, Pilot pilot) {
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport; 
        this.arrivalCity = arrivalCity;
        this.arrivalTime = arrivalTime;
        this.delay = delay;
        this.pilot = pilot;
    }

    public abstract double calculatePriority();

    // Геттеры и сеттеры (Hibernate использует их для работы)
    public Long getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public String getArrivalCity() { return arrivalCity; }
    public int getDelay() { return delay; }
    public void setDelay(int delay) { this.delay = delay; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }

    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public Airport getDepartureAirport() { return departureAirport; }
    public Pilot getPilot() { return pilot; }
    public String getDepartureCityName() {
        return departureAirport != null ? departureAirport.getCity() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(id, flight.id); // В JPA лучше сравнивать по ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}