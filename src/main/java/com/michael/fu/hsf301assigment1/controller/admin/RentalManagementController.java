package com.michael.fu.hsf301assigment1.controller.admin;

import com.michael.fu.hsf301assigment1.controller.BaseController;
import com.michael.fu.hsf301assigment1.entity.CarRental;
import com.michael.fu.hsf301assigment1.service.impl.CarRentalService;
import com.michael.fu.hsf301assigment1.service.impl.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/admin/rentals")
public class RentalManagementController extends BaseController {

    private final CarService carService;
    private final CarRentalService carRentalService;

    @Autowired
    public RentalManagementController(CarService carService, CarRentalService carRentalService) {
        this.carService = carService;
        this.carRentalService = carRentalService;
    }

    // Trang hiển thị danh sách đơn thuê xe
    @GetMapping({"", "/"})
    public String rentals(Model model) {
        List<CarRental> rentals = carRentalService.findAll();
        model.addAttribute("rentals", rentals);
        return "admin/rentals";
    }

    // Cập nhật trạng thái đơn thuê (GET)
    @GetMapping("/update-status")
    public String updateRentalStatus(
            @RequestParam("customerId") Long customerId,
            @RequestParam("carId") Long carId,
            @RequestParam("status") String status
    ) {
        try {
            carRentalService.updateStatus(customerId, carId, status);
        } catch (IllegalArgumentException ex) {
            logger.error("Cập nhật trạng thái thất bại: {}", ex.getMessage());
            // Có thể thêm flash attribute thông báo lỗi nếu cần
        }
        return "redirect:/api/v1/admin/rentals";
    }
}
