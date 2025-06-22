package com.michael.fu.hsf301assigment1.controller.customer;

import com.michael.fu.hsf301assigment1.config.CustomerSession;
import com.michael.fu.hsf301assigment1.entity.CarRental;
import com.michael.fu.hsf301assigment1.entity.CarRentalStatus;
import com.michael.fu.hsf301assigment1.entity.Customer;
import com.michael.fu.hsf301assigment1.service.impl.CarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class BookingHistoryController {

    private final CarRentalService carRentalService;
    private final CustomerSession customerSession;

    @GetMapping("/bookings")
    public String viewBookingHistory(Model model) {
        Customer customer = customerSession.getCustomer();
        if (customer == null) {
            return "redirect:/api/v1/public/login";
        }

        List<CarRental> bookings = carRentalService.findByCustomer(customer);

        long total = bookings.size();
        long booked = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.BOOKED).count();
        long pickedUp = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.PICKED_UP).count();
        long returned = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.RETURNED).count();
        long canceled = bookings.stream().filter(b -> b.getStatus() == CarRentalStatus.CANCELED).count();

        model.addAttribute("bookings", bookings);
        model.addAttribute("totalBookings", total);
        model.addAttribute("pendingBookings", booked);
        model.addAttribute("activeBookings", pickedUp);
        model.addAttribute("completedBookings", returned);
        model.addAttribute("cancelledBookings", canceled);

        return "customer/bookings";
    }
}
