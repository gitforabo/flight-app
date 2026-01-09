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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightAviationApp {

    private static final Logger logger = LoggerFactory.getLogger(FlightAviationApp.class);
    public static void main(String[] args) {
        Path inputPath = Paths.get("src/main/java/aviation/flights.txt");
        Path outpuPath = Paths.get("src/main/java/aviation/audit_report.txt");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        logger.info("Начало обработки файла: {}", inputPath.getFileName());

        String[] arr;
        LocalDateTime arrivalTime;
        String flightNumber;
        int delay;
        try (BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(outpuPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    // 1.Разделяем строку (формат: Дата;Номер;Откуда;Куда;Задержка)
                    arr = line.split(";");
                    if(arr.length < 5) { throw new InvalidFlightDataException("Недостаточно данных в строке"); } 
                    
                    arrivalTime = LocalDateTime.parse(arr[0]);
                    flightNumber = arr[1];
                    delay = Integer.parseInt(arr[4]); // если не сможет парсить кидает NumberFormatException
                    
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

        PassengerFlight pf = new PassengerFlight("LH-400", "London", "Paris", LocalDateTime.parse("2023-10-25T12:00"), 130, 10);
        CargoFlight cf = new CargoFlight("Ll-400", "London", "Paris", LocalDateTime.parse("2023-10-25T12:00"), 130, 10);
        Flight[] flights = new Flight[]{pf, cf};

        for(Flight flight:flights) {
            flight.printInfo(); // Вызовет общую логику из родителя
            double priority = flight.calculatePriority(); // Вызовет разную логику (Полиморфизм!)
            System.out.println("Приоритет обслуживания: " + priority);
            System.out.println("---------------------------");
        }
    }
}