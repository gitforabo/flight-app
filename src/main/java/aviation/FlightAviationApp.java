package aviation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightAviationApp {

    private static final Logger logger = LoggerFactory.getLogger(FlightAviationApp.class);
    public static void main(String[] args) {
        Path inputPath = Paths.get("src/main/java/aviation/flights.txt");
        Path outpuPath = Paths.get("src/main/java/aviation/audit_report.txt");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        logger.info("Начало обработки файла: {}", inputPath.getFileName());

        FlightManager<Flight> manager = new FlightManager<>();

        try (BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(outpuPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    // 1.Разделяем строку (формат: Дата;Номер;Откуда;Куда;Задержка)
                    String[] arr = line.split(";");
                    if(arr.length < 5) { throw new InvalidFlightDataException("Недостаточно данных в строке"); } 
                    
                    String flightNumber = arr[1];
                    LocalDateTime arrivalTime = LocalDateTime.parse(arr[0]);
                    String departureСity = arr[2];
                    String arrivalCity = arr[3];
                    int delay = Integer.parseInt(arr[4]); // если не сможет парсить кидает NumberFormatException
                    
                    if (flightNumber.contains("CF")) {
                        CargoFlight cf = new CargoFlight(flightNumber, departureСity, arrivalCity, arrivalTime, delay, 30);
                        manager.addFlight(cf);
                    } else {
                        PassengerFlight pf = new PassengerFlight(flightNumber, departureСity, arrivalCity, arrivalTime, delay, 300);
                        manager.addFlight(pf);
                    }

                    // 2.Логика задержки
                    if(delay > 60) {
                        logger.warn("Рейс {} задержан на {} минут", flightNumber, delay);
                        // Рассчитываем новое время так как LocalDateTime immutable
                        LocalDateTime newTime = arrivalTime.plusMinutes(delay);

                        String report = String.format("РЕЙС %s: Новое время прибытия: %s (Задержка: %d мин)\n",
                                flightNumber, newTime.format(formatter), delay);

                        writer.write(report);

                        logger.debug("Записан отчет для рейса {}", flightNumber);
                    }
                } catch (DateTimeParseException | NumberFormatException e) {
                    logger.error("Ошибка парсинга данных (дата или число) в строке: {}. Причина: {}", line, e.getMessage());
                } catch (InvalidFlightDataException e) {
                    logger.error("Найдена битая строка: {}. Ошибка: {}", line, e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Критическая ошибка ввода-вывода", e);
        }
        logger.info("Обработка завершена.");

        System.out.println("------t------------t---------");

        System.out.println("Всего записей: " + manager.flightList.size());
        System.out.println("Уникальных рейсов: " + manager.uniqueFlights.size());

        System.out.println("------t------------t---------");
        
        manager.flightList.forEach(Flight :: printInfo);
        
        System.out.println("\n=== АНАЛИТИКА ПОТОКОВ ===");

        // 1. Фильтрация: Только грузовые рейсы с задержкой
        manager.flightList.stream()
            .filter(f -> f instanceof CargoFlight)
            .filter(f -> f.getDelay() > 0)
            .forEach(f -> f.printInfo());

        // 2. Агрегация: Суммарная задержка пассажирских рейсов
        int totalPassengerDelay = manager.flightList.stream()
            .filter(f -> f instanceof PassengerFlight)
            .mapToInt(f -> f.getDelay())
            .sum();
            System.out.println("Общая задержка пассажиров: " + totalPassengerDelay + " мин.");

        // 3. Optional: Поиск самого приоритетного рейса
        Optional<Flight> opt = manager.flightList.stream()  // Контейнер для значения, которое может отсутствовать
            .max(Comparator.comparingDouble(Flight::calculatePriority));
        // Эквивалент (f1, f2) -> Double.compare(f1.calculatePriority(), f2.calculatePriority())
        opt.ifPresent(f -> System.out.println("Самый приоритетный рейс: " + f.getFlightNumber()));
        // stream → max по приоритету → Optional → если есть → вывести номер

        // 4. Группировка: Рейсы по городам прибытия
        Map<String, List<Flight>> cityGroups = manager.flightList.stream()
            .collect(Collectors.groupingBy(Flight::getArrivalCity));

        cityGroups.forEach((city, list) -> 
            System.out.println("В город " + city + " летит рейсов: " + list.size()));
    }
}