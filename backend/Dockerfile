# Первый этап: Сборка
FROM maven:3.8.6-openjdk-17-slim AS builder

# Копируем файлы в контейнер
COPY . /app

# Переходим в рабочую директорию
WORKDIR /app

# Сборка проекта с помощью Maven
RUN mvn clean package -DskipTests

# Второй этап: Запуск
FROM openjdk:17-jdk-slim

# Создаем директорию для приложения
WORKDIR /app

# Копируем скомпилированный JAR файл из предыдущего этапа
COPY --from=builder /app/target/nazik-api-0.0.1-SNAPSHOT.jar /app/nazik-api.jar

# Запускаем JAR файл
CMD ["java", "-jar", "/app/nazik-api.jar"]
