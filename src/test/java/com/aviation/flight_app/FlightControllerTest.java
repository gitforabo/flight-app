package com.aviation.flight_app;

import com.aviation.flight_app.model.Flight;
import com.aviation.flight_app.model.PassengerFlight;
import com.aviation.flight_app.repository.FlightRepository;
import com.aviation.flight_app.service.FlightService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // JUnit. будем использовать Mockito для создания подделок
class FlightServiceTest { 

    @Mock // Создаем "фейковый" репозиторий. Он не лезет в базу данных, а просто имитирует её работу.
    private FlightRepository flightRepository;

    @InjectMocks // Создаем РЕАЛЬНЫЙ сервис, но автоматически вставляем в него наш @Mock репозиторий.
    private FlightService flightService;
    
    @Test // ТЕСТ 1: Проверяем, что если рейс есть в базе, сервис его находит и отдает
    void getFlightById_ShouldReturnFlight_WhenExists() {
        // --- 1. GIVEN (Подготовка данных) ---
        Long id = 1L;
        PassengerFlight mockFlight = new PassengerFlight();
        mockFlight.setFlightNumber("KC-901");
        
        // ОБУЧАЕМ МOК: "Когда тебя (репозиторий) спросят ID 1, ответь, что ты нашел mockFlight"
        when(flightRepository.findById(id)).thenReturn(Optional.of(mockFlight));

        // --- 2. WHEN (Вызов метода, который тестируем) ---
        Flight result = flightService.getFlightById(id);

        // --- 3. THEN (Проверка результата) ---
        assertNotNull(result); // Проверяем, что нам не вернули пустоту
        assertEquals("KC-901", result.getFlightNumber()); // Проверяем, тот ли это рейс
        verify(flightRepository, times(1)).findById(id); // Проверяем, что сервис реально сходил в репозиторий ровно 1 раз
    }

    @Test //ТЕСТ 2: Математика. Проверяем формулу приоритета в классе PassengerFlight.
    void calculatePriority_ShouldReturnCorrectValue() {
        // --- GIVEN ---
        PassengerFlight flight = new PassengerFlight();
        flight.setDelay(60); // Допустим, задержка 60 мин
        flight.setPassengerCount(100); // И 100 пассажиров

        // --- WHEN ---
        double actualPriority = flight.calculatePriority();

        // --- THEN ---
        // Твоя формула в коде: 100 * 60 = 6000
        double expectedPriority = 6000.0; 
        
        // 0.001 — это допустимая погрешность при сравнении дробных чисел
        assertEquals(expectedPriority, actualPriority, 0.001);
    }

    @Test // ТЕСТ 3: Проверка на ошибку. Что будет, если рейса нет?
    void getFlightById_ShouldThrowException_WhenNotFound() {
        // --- GIVEN ---
        Long id = 999L;
        // ОБУЧАЕМ МОК: "Когда тебя спросят про ID 999, ответь, что ничего не нашел (Optional.empty())"
        when(flightRepository.findById(id)).thenReturn(Optional.empty());

        // --- WHEN & THEN (Действие и проверка одновременно) ---
        // Мы ожидаем, что выполнение кода внутри лямбды { ... } выбросит исключение RuntimeException
        assertThrows(RuntimeException.class, () -> {
            flightService.getFlightById(id);
        });

        // Проверяем, что попытка поиска в базе всё же была
        verify(flightRepository).findById(id);
    }
}