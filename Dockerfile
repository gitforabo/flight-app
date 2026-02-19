# ЭТАП 1: СБОРКА (BUILD)
# Используем тяжелый образ с Gradle и JDK 21 для компиляции
FROM --platform=$BUILDPLATFORM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
# Собираем JAR-файл, пропуская тесты для ускорения процесса
RUN gradle build -x test

# ЭТАП 2: ЗАПУСК (RUNTIME)
# Используем легкий образ только с JRE (без лишних инструментов сборки)
FROM eclipse-temurin:21-jdk
WORKDIR /app
# Копируем только готовый JAR из первого этапа (build)
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]