package com.aviation.flight_app;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;

import com.aviation.flight_app.model.CargoFlight;
import com.aviation.flight_app.model.PassengerFlight;
import com.aviation.flight_app.repository.FlightRepository;

//@Component
public class DataInitializer implements CommandLineRunner {

    private final FlightRepository flightRepository;

    // Spring сам подставит сюда репозиторий (Dependency Injection)
    public DataInitializer(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Наполнение базы данных начальными данными ---");

        // 1. Создаем пассажирский рейс
        PassengerFlight p1 = new PassengerFlight(
            "KC-901", "Almaty", "Astana", 
            LocalDateTime.now().plusHours(2), 0, 150
        );

        // 2. Создаем грузовой рейс
        CargoFlight c1 = new CargoFlight(
            "CA-502", "New York", "London", 
            LocalDateTime.now().plusHours(5), 15, 25.5
        );

        // 3. Сохраняем в PostgreSQL
        flightRepository.save(p1);
        flightRepository.save(c1);

        System.out.println("--- Данные успешно сохранены! Количество рейсов: " + flightRepository.count() + " ---");
    }
}