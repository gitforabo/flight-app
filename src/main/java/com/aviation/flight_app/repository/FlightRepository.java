package com.aviation.flight_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aviation.flight_app.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    
    // Spring сам создаст SQL запрос: SELECT * FROM flights WHERE arrival_city = ?
    List<Flight> findByArrivalCity(String city);
    
    // SELECT * FROM flights WHERE delay > ?
    List<Flight> findByDelayGreaterThan(int delayMinutes);

    // Поиск по точному номеру рейса
    List<Flight> findByFlightNumber(String flightNumber);

    // Поиск всех рейсов в конкретный город
    List<Flight> findByArrivalCityIgnoreCase(String city);
}