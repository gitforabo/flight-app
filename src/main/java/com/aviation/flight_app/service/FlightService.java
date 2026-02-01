package com.aviation.flight_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aviation.flight_app.model.Flight;
import com.aviation.flight_app.repository.FlightRepository;

import jakarta.transaction.Transactional;

@Service // Помечаем класс как сервис для бизнес-логики
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    // Пример бизнес-логики: найти рейсы с высоким приоритетом
    public List<Flight> getHighPriorityFlights() {
        return flightRepository.findAll().stream()
                .filter(f -> f.calculatePriority() > 50)
                .toList();
    }

    // Удаление рейса по ID
    /*@Transactional // Важно для методов, которые изменяют или удаляют данные
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }*/

    @Transactional // Важно! Гарантирует, что если удаление не пройдет, данные не потеряются
    public Flight deleteFlightAndReturn(Long id) {
    // 1. Ищем объект в базе
    Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Рейс с ID " + id + " не найден"));

    flightRepository.delete(flight); // 2. Удаляем его
    return flight;                   // 3. Возвращаем тот объект, который нашли в пункте 1
}

    // Добавление рейса (Spring сам поймет, Passenger это или Cargo по данным из JSON)
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public List<Flight> getFlightByNumber(String number) {
    return flightRepository.findByFlightNumber(number);
    }

    public List<Flight> getFlightsByCity(String city) {
        return flightRepository.findByArrivalCityIgnoreCase(city);
    }
}