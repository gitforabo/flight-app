package com.aviation.flight_app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code; // Например: "NQZ"
    private String name; // Например: "Nursultan Nazarbayev Intl"
    private String city; // Например: "Astana"

    // Обратная связь: список рейсов, вылетающих отсюда
    @OneToMany(mappedBy = "departureAirport")
    @JsonIgnore // Важно! Чтобы не было бесконечного цикла в JSON
    private List<Flight> departingFlights;

    public Airport() {} // Пустой конструктор

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getCity() { return city; }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCity(String city) {
        this.city = city;
    }
}