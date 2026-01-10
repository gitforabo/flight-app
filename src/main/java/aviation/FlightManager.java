package aviation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FlightManager<T extends Flight> { // T — это любой класс, который наследуется от Flight
    List<T> flightList = new ArrayList<>();
    Set<T> uniqueFlights = new HashSet<>();

    public void addFlight(T flight) {
        flightList.add(flight);
        uniqueFlights.add(flight); // Если дубликат (по equals/hashCode), Set его не добавит
    }

    public List<T> getFlightsSortedByPriority() {
        // Создаем копию списка, чтобы не менять порядок в основном списке
        List<T> sortedList = new ArrayList<>(flightList);
        
        // Используем Comparator для сортировки по убыванию приоритета
        sortedList.sort((f1, f2) -> Double.compare(f2.calculatePriority(), f1.calculatePriority()));
        
        return sortedList;
    }

    public Map<String, T> getFlightMap() {
        Map<String, T> flightMap = new HashMap<>();
        for (T flight : flightList) {
            // Ключ — номер рейса, значение — сам объект
            flightMap.put(flight.getFlightNumber(), flight);
        }
        return flightMap;
    }

    public void addAllFlights(List<? extends T> newFlights) { // Wildcard (? extends) — в параметрах метода принимают список T или его наследников
        // List<? extends Flight> ← List<PassengerFlight>. А так как: PassengerFlight extends Flight
        for (T f : newFlights) {
            addFlight(f);
        }
    } // ? extends T нужен, когда метод принимает ВНЕШНЮЮ коллекцию,из которой он ТОЛЬКО ЧИТАЕТ объекты типа T.
}
