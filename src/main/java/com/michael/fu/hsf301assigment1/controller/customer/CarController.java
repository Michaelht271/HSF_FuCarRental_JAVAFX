package com.michael.fu.hsf301assigment1.controller.customer;

import com.michael.fu.hsf301assigment1.entity.Car;
import com.michael.fu.hsf301assigment1.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CarController {


    private final CarRepository carRepository;
    @Autowired
    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("/api/v1/customer/cars")
    public String listCars(Model model) {
        List<Car> cars = carRepository.findAll();
        model.addAttribute("cars", cars);
        return "customer/cars";
    }
}