package com.michael.fu.hsf301assigment1.controller;

import com.michael.fu.hsf301assigment1.entity.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController extends BaseController{
    @GetMapping(path = {"/api/v1/public/", "/"})
    public String homePage() {
        return "public/home"; // hoặc tên view tương ứng
    }


    public String home() {
        logger.info("Received request to Home Page");
        return "public/home";
    }

    @GetMapping("/api/v1/public/login")
    public String login() {
        logger.info("Received request to Login Page");
        return "public/login";
    }
   

    @GetMapping("/api/v1/public/register")
    public String register(Model model) {
        logger.info("Received request to Register Page");
        model.addAttribute("customer", new Customer());
        return "public/register";
    }


}
