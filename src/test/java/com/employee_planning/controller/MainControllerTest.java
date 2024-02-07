package com.employee_planning.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

    private final MainController mainController = new MainController();

    @Test
    void viewHomePage() {
        ModelAndView modelAndView = mainController.viewHomePage();
        assertEquals("home.html", modelAndView.getViewName());
    }
}