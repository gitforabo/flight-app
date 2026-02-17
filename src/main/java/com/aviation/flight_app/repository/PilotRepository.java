package com.aviation.flight_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aviation.flight_app.model.Pilot;

public interface PilotRepository extends JpaRepository<Pilot, Long> {
    
}
