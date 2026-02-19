package com.aviation.flight_app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aviation.flight_app.model.Flight;
import com.aviation.flight_app.service.FlightService;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService; // Внедряем сервис вместо репозитория

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    @GetMapping("/priority")
    public List<Flight> getPriorityFlights() {
        return flightService.getHighPriorityFlights();
    }

    // МЕТОД ДЛЯ ДОБАВЛЕНИЯ
    @PostMapping
    public Flight addFlight(@RequestBody Flight flight) { // @RequestBody превращает JSON из запроса в объект Java
        return flightService.saveFlight(flight);
    }

    // МЕТОД ДЛЯ УДАЛЕНИЯ
    @DeleteMapping("/{id}")
    public Flight deleteFlight(@PathVariable Long id) { // PathVariable получаеть {id} из запроса
        return flightService.deleteFlightAndReturn(id);
    }  // метод вернет JSON удаленного объекта

    // 1. Поиск по номеру рейса: /api/flights/number/KC-901
    @GetMapping("/number/{number}")
    public List<Flight> getByNumber(@PathVariable String number) {
        return flightService.getFlightByNumber(number);
    }

    // 2. Поиск по городу: /api/flights/search?city=Astana
    @GetMapping("/search")
    public List<Flight> searchByCity(@RequestParam String city) { // ищет данные в query-строке (всё, что идет после знака ?)
        return flightService.getFlightsByCity(city);
    }
}