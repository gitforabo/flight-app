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
import jakarta.persistence.Table;

@Entity // Говорим Spring, что это сущность БД
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоинкремент (1, 2, 3...)
    private Long id;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    private String departureCity;
    private String arrivalCity;
    private LocalDateTime arrivalTime;
    
    // В БД задержку будем менять через транзакции
    private int delay;

    // Пустой конструктор ОБЯЗАТЕЛЕН для Hibernate
    protected Flight() {}

    public Flight(String flightNumber, String departureCity, String arrivalCity, LocalDateTime arrivalTime, int delay) {
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.arrivalTime = arrivalTime;
        this.delay = delay;
    }

    public abstract double calculatePriority();

    // Геттеры и сеттеры (Hibernate использует их для работы)
    public Long getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public String getArrivalCity() { return arrivalCity; }
    public int getDelay() { return delay; }
    public void setDelay(int delay) { this.delay = delay; }

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