package org.example.api.utils;

import com.github.javafaker.Faker;

public class TestDataGenerator {
    private static final Faker faker = new Faker();

    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateRandomPassword() {
        return faker.internet().password(8, 16); // Генерация пароля длиной от 8 до 16 символов
    }

    public static String generateRandomName() {
        return faker.name().firstName();
    }
}
