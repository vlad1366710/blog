package com.blog.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для обработки запросов, связанных с информацией о сайте.
 */
@Controller
public class AboutController {

    // Логгер для класса AboutController
    private static final Logger logger = LoggerFactory.getLogger(AboutController.class);


    /**
     * Обрабатывает GET-запрос на страницу "О нас".
     *
     * @param error Параметр ошибки (опционально).
     * @param model Модель для передачи данных в представление.
     * @return Название представления "aboutUs".
     */
    @GetMapping("/aboutUs")
    public String aboutUs(@RequestParam(value = "error", required = false) String error, Model model) {
        // Логируем начало обработки запроса
        logger.info("Обработка GET-запроса на страницу 'О нас'");

        return "aboutUs";
    }
}

