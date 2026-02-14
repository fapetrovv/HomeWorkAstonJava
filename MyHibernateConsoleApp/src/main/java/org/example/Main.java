package org.example;

import org.example.dao.HibernateUtil;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Приложение user-service запущено");

        try {
            UserService userService = new UserService();
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                userService.displayMenu();
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        userService.createUser();
                        break;
                    case "2":
                        userService.getAllUsers();
                        break;
                    case "3":
                        userService.getUserById();
                        break;
                    case "4":
                        userService.searchByEmail();
                        break;
                    case "5":
                        userService.updateUser();
                        break;
                    case "6":
                        userService.deleteUser();
                        break;
                    case "0":
                        running = false;
                        System.out.println("Выход из программы...");
                        break;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }

            userService.close();
            scanner.close();
        } catch (Exception e) {
            logger.error("Критическая ошибка в приложении", e);
            System.err.println("Произошла критическая ошибка: " + e.getMessage());
        } finally {
            HibernateUtil.shutdown();
            logger.info("Приложение user-service завершило работу");
        }
    }
}