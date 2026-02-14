package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao;
    private final Scanner scanner;

    public UserService() {
        this.userDao = new UserDao();
        this.scanner = new Scanner(System.in);
    }

    public void createUser() {
        System.out.println("\nСоздание нового пользователя");

        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        if (userDao.existsByEmail(email)) {
            System.out.println("Пользователь с таким email уже существует!");
            return;
        }

        System.out.print("Введите возраст: ");
        Integer age = null;
        try {
            age = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат возраста!");
            return;
        }

        User user = new User(name, email, age);
        try {
            User savedUser = userDao.save(user);
            System.out.println("Пользователь создан: " + savedUser);
        } catch (Exception e) {
            System.out.println("Ошибка при создании пользователя: " + e.getMessage());
            logger.error("Error creating user", e);
        }
    }

    public void getAllUsers() {
        System.out.println("\nСписок всех пользователей");
        try {
            List<User> users = userDao.findAll();
            if (users.isEmpty()) {
                System.out.println("Пользователи не найдены");
            } else {
                users.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при получении пользователей: " + e.getMessage());
            logger.error("Error getting all users", e);
        }
    }

    public void getUserById() {
        System.out.println("\nПоиск пользователя по ID");
        System.out.print("Введите ID пользователя: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());
            userDao.findById(id).ifPresentOrElse(
                    user -> System.out.println("Найден пользователь: " + user),
                    () -> System.out.println("Пользователь с ID " + id + " не найден")
                                                );
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID!");
        } catch (Exception e) {
            System.out.println("Ошибка при поиске пользователя: " + e.getMessage());
            logger.error("Error getting user by id", e);
        }
    }

    public void updateUser() {
        System.out.println("\nОбновление пользователя");
        System.out.print("Введите ID пользователя для обновления: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());

            userDao.findById(id).ifPresentOrElse(
                    user -> {
                        System.out.println("Текущие данные: " + user);

                        System.out.print("Введите новое имя (оставьте пустым для сохранения текущего): ");
                        String name = scanner.nextLine();
                        if (!name.trim().isEmpty()) {
                            user.setName(name);
                        }

                        System.out.print("Введите новый email (оставьте пустым для сохранения текущего): ");
                        String email = scanner.nextLine();
                        if (!email.trim().isEmpty()) {
                            if (!email.equals(user.getEmail()) && userDao.existsByEmail(email)) {
                                System.out.println("Пользователь с таким email уже существует!");
                                return;
                            }
                            user.setEmail(email);
                        }

                        System.out.print("Введите новый возраст (оставьте пустым для сохранения текущего): ");
                        String ageInput = scanner.nextLine();
                        if (!ageInput.trim().isEmpty()) {
                            try {
                                user.setAge(Integer.parseInt(ageInput));
                            } catch (NumberFormatException e) {
                                System.out.println("Неверный формат возраста!");
                                return;
                            }
                        }

                        try {
                            User updatedUser = userDao.update(user);
                            System.out.println("Пользователь обновлен: " + updatedUser);
                        } catch (Exception e) {
                            System.out.println("Ошибка при обновлении пользователя: " + e.getMessage());
                            logger.error("Error updating user", e);
                        }
                    },
                    () -> System.out.println("Пользователь с ID " + id + " не найден")
                                                );
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID!");
        }
    }

    public void deleteUser() {
        System.out.println("\nУдаление пользователя");
        System.out.print("Введите ID пользователя для удаления: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());

            System.out.print("Вы уверены, что хотите удалить пользователя? (y/n): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("y")) {
                try {
                    userDao.delete(id);
                    System.out.println("Пользователь с ID " + id + " удален");
                } catch (Exception e) {
                    System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
                    logger.error("Error deleting user", e);
                }
            } else {
                System.out.println("Удаление отменено");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат ID!");
        }
    }

    public void searchByEmail() {
        System.out.println("\nПоиск пользователя по email");
        System.out.print("Введите email: ");

        String email = scanner.nextLine();
        try {
            userDao.findByEmail(email).ifPresentOrElse(
                    user -> System.out.println("Найден пользователь: " + user),
                    () -> System.out.println("Пользователь с email " + email + " не найден")
                                                      );
        } catch (Exception e) {
            System.out.println("Ошибка при поиске пользователя: " + e.getMessage());
            logger.error("Error searching user by email", e);
        }
    }

    public void displayMenu() {
        System.out.println("\nМеню управления пользователями");
        System.out.println("1. Создать нового пользователя");
        System.out.println("2. Показать всех пользователей");
        System.out.println("3. Найти пользователя по ID");
        System.out.println("4. Найти пользователя по email");
        System.out.println("5. Обновить пользователя");
        System.out.println("6. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    public void close() {
        scanner.close();
    }
}
