package com.employee_planning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/ines")
public class MainController {

    /**
     * Displays the main page.
     *
     * @return home.html
     */
    @GetMapping("/home")
    public ModelAndView viewHomePage() {
        return new ModelAndView("home.html");
    }
}
