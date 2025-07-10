package com.michael.fu.hsf301assigment2.controller.customer;

import com.michael.fu.hsf301assigment2.session.CustomerSession;
import com.michael.fu.hsf301assigment2.controller.BaseController;
import com.michael.fu.hsf301assigment2.entity.*;
import com.michael.fu.hsf301assigment2.service.impl.CarRentalService;
import com.michael.fu.hsf301assigment2.service.impl.CarService;
import com.michael.fu.hsf301assigment2.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/customers")
public class CarController extends BaseController {

    private final CarService carService;
    private final CarRentalService carRentalService;
    private final CustomerService customerService;
    private final CustomerSession customerSession;

    @Autowired
    public CarController(CarService carService,
                         CarRentalService carRentalService,
                         CustomerService customerService,
                         CustomerSession customerSession) {
        this.carService = carService;
        this.carRentalService = carRentalService;
        this.customerService = customerService;
        this.customerSession = customerSession;
    }

    /**
     * Hiển thị danh sách xe với các bộ lọc: số chỗ, khoảng giá, ngày/giờ.
     */
    @GetMapping("/cars")
    public String listCars(
            @RequestParam(required = false) String pickupDate,
            @RequestParam(required = false) String pickupTime,
            @RequestParam(required = false) String returnDate,
            @RequestParam(required = false) String returnTime,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) String priceRange,
            Model model) {

        List<Car> cars = carService.findAll();

        // Lọc theo số chỗ
        if (capacity != null) {
            cars = cars.stream()
                    .filter(car -> car.getCapacity() == capacity)
                    .collect(Collectors.toList());
        }

        // Lọc theo khoảng giá
        if (priceRange != null && priceRange.contains("-")) {
            String[] parts = priceRange.split("-");
            try {
                long min = Long.parseLong(parts[0]);
                long max = Long.parseLong(parts[1]);
                cars = cars.stream()
                        .filter(car -> car.getRentPrice() >= min && car.getRentPrice() <= max)
                        .collect(Collectors.toList());
            } catch (NumberFormatException ignored) {
            }
        }

        // Truyền lại giá trị đã lọc để hiển thị trong form
        model.addAttribute("pickupDate", pickupDate);
        model.addAttribute("pickupTime", pickupTime);
        model.addAttribute("returnDate", returnDate);
        model.addAttribute("returnTime", returnTime);
        model.addAttribute("capacity", capacity);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("cars", cars);

        return "customer/cars";
    }

    /**
     * Xem chi tiết thông tin xe.
     */
    @GetMapping("/cars/{id}")
    public String viewCarDetail(@PathVariable("id") Long id, Model model) {
        Car car = carService.findById(id);
        if (car == null) {
            model.addAttribute("error", "Xe không tồn tại.");
            return "redirect:/api/v1/customer/cars";
        }

        model.addAttribute("car", car);
        return "customer/car-detail";
    }




}
