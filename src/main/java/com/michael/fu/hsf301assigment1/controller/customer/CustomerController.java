package com.michael.fu.hsf301assigment1.controller.customer;

import com.michael.fu.hsf301assigment1.controller.BaseController;
import com.michael.fu.hsf301assigment1.entity.Car;
import com.michael.fu.hsf301assigment1.service.impl.CarService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CustomerController extends BaseController {
    private final CarService carService;
    @Autowired
    public CustomerController(CarService carService) {
        this.carService = carService;
    }
    @GetMapping("/api/v1/customers/home")

    public String homePage(HttpServletRequest request, Model model) {
        model.addAttribute("httpServletRequest", request);
        return "customer/home";
    }



}
