package com.aviation.flight_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aviation.flight_app.model.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    
}