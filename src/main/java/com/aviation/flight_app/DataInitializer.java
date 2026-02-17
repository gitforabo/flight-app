package com.aviation.flight_app;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.aviation.flight_app.model.Airport;
import com.aviation.flight_app.model.CargoFlight;
import com.aviation.flight_app.model.PassengerFlight;
import com.aviation.flight_app.repository.AirportRepository;
import com.aviation.flight_app.repository.FlightRepository;

@Component // Не забудь раскомментировать, чтобы Spring запустил этот класс
public class DataInitializer implements CommandLineRunner {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository; // Добавляем новый репозиторий

    // Внедряем оба репозитория через конструктор
    public DataInitializer(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Наполнение базы данных начальными данными ---");

        // 1. Сначала создаем и сохраняем аэропорты
        Airport almaty = new Airport();
        almaty.setName("Almaty International Airport");
        almaty.setCode("ALA");
        almaty.setCity("Almaty");
        airportRepository.save(almaty);

        Airport astana = new Airport();
        astana.setName("Nursultan Nazarbayev International");
        astana.setCode("NQZ");
        astana.setCity("Astana");
        airportRepository.save(astana);

        Airport nyc = new Airport();
        nyc.setName("John F. Kennedy International");
        nyc.setCode("JFK");
        nyc.setCity("New York");
        airportRepository.save(nyc);

        // 2. Создаем пассажирский рейс (привязываем объект almaty)
        PassengerFlight p1 = new PassengerFlight(
            "KC-901", 
            almaty, // Вместо "Almaty" передаем объект
            "Astana", 
            LocalDateTime.now().plusHours(2), 
            0, 
            150
        );

        // 3. Создаем грузовой рейс (привязываем объект nyc)
        CargoFlight c1 = new CargoFlight(
            "CA-502", 
            nyc, // Вместо "New York" передаем объект
            "London", 
            LocalDateTime.now().plusHours(5), 
            15, 
            25.5
        );

        PassengerFlight p2 = new PassengerFlight(
            "KC-902", 
            astana,
            "Shymkent", 
            LocalDateTime.now().plusHours(3), 
            90, 
            250
        );

        // 4. Сохраняем рейсы в PostgreSQL
        flightRepository.save(p1);
        flightRepository.save(c1);
        flightRepository.save(p2);

        System.out.println("--- Данные успешно сохранены! ---");
        System.out.println("Аэропортов в базе: " + airportRepository.count());
        System.out.println("Рейсов в базе: " + flightRepository.count());
    }
}