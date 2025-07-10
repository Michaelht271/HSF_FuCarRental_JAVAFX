package com.michael.fu.hsf301assigment2.controller.customer;

import com.michael.fu.hsf301assigment2.controller.BaseController;
import com.michael.fu.hsf301assigment2.dto.CarRentalDTO;
import com.michael.fu.hsf301assigment2.entity.Customer;
import com.michael.fu.hsf301assigment2.session.CustomerSession;
import com.michael.fu.hsf301assigment2.service.impl.CarRentalService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/v1/customers")
public class CarRentalController extends BaseController {

    private final CustomerSession customerSession;
    private final CarRentalService carRentalService;

    public CarRentalController(CustomerSession customerSession,
                               CarRentalService carRentalService) {
        this.customerSession = customerSession;
        this.carRentalService = carRentalService;
    }

    @PostMapping("/cars/rental")
    public String rentCar( @ModelAttribute CarRentalDTO request,
            RedirectAttributes redirectAttributes) {

        logger.info("Nhận yêu cầu thuê xe: carId={}, pickup={}, return={}",
                request.getCarId(), request.getPickupDate(), request.getReturnDate(), request.getRentPrice());

        Customer sessionCustomer = customerSession.getCustomer();
        if (sessionCustomer == null) {
            logger.warn("Không có khách hàng đăng nhập trong session.");
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để tiếp tục.");
            return "redirect:/api/v1/public/login";
        }

        request.setCustomerEmail(sessionCustomer.getEmail());

        try {
            carRentalService.rentailCar(request);
            logger.info("Đặt xe thành công cho khách hàng: {}", request.getCustomerEmail());
            redirectAttributes.addFlashAttribute("success", "Đặt xe thành công!");
        } catch (Exception ex) {
            logger.error("Lỗi khi đặt xe: {}", ex.getMessage(), ex);
            redirectAttributes.addFlashAttribute("error", "Đặt xe thất bại: " + ex.getMessage());
        }

        return "redirect:/api/v1/customers/cars";
    }
}
