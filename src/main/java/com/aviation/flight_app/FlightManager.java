package com.aviation.flight_app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.aviation.flight_app.model.Flight;

public class FlightManager<T extends Flight> { // T — это любой класс, который наследуется от Flight
    // Вместо ArrayList используем CopyOnWriteArrayList (безопасен для чтения)
    protected List<T> flightList = new CopyOnWriteArrayList<>();
    
    // Вместо HashSet используем ConcurrentHashMap.newKeySet()
    protected Set<T> uniqueFlights = ConcurrentHashMap.newKeySet();

    public synchronized void addFlight(T flight) {
        flightList.add(flight);
        uniqueFlights.add(flight);
    }

    public List<T> getFlightsSortedByPriority() {
        // Создаем копию списка, чтобы не менять порядок в основном списке
        List<T> sortedList = new ArrayList<>(flightList);
        
        // Используем Comparator для сортировки по убыванию приоритета
        sortedList.sort((f1, f2) -> Double.compare(f2.calculatePriority(), f1.calculatePriority()));
        
        return sortedList;
    }

    public Map<String, T> getFlightMap() {
        return flightList.stream().collect(Collectors.toMap(Flight :: getFlightNumber, f -> f, (existing, replacement) -> existing));
    } // Flight::getFlightNumber тоже самое f -> f.getFlightNumber() / (a, b) -> a при дубликатах ключей оставить старый.

    public void addAllFlights(List<? extends T> newFlights) { // Wildcard (? extends) — в параметрах метода принимают список T или его наследников
        // List<? extends Flight> ← List<PassengerFlight>. А так как: PassengerFlight extends Flight
        for (T f : newFlights) {
            addFlight(f);
        }
    } // ? extends T нужен, когда метод принимает ВНЕШНЮЮ коллекцию,из которой он ТОЛЬКО ЧИТАЕТ объекты типа T.
}
